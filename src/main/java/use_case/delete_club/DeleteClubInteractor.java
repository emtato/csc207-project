package use_case.delete_club;

import use_case.club.ClubAnnouncementCollector;
import use_case.club.ClubAnnouncementResult;
import use_case.club.ClubReadOperations;
import use_case.create_club.ClubWriteOperations;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;
import java.util.ArrayList;

public class DeleteClubInteractor implements DeleteClubInputBoundary {
    private final ClubReadOperations clubRead;
    private final ClubWriteOperations clubWrite;
    private final UserDataAccessObject userDAO;
    private final DeleteClubOutputBoundary presenter;

    public DeleteClubInteractor(ClubReadOperations clubRead,
                                ClubWriteOperations clubWrite,
                                UserDataAccessObject userDAO,
                                DeleteClubOutputBoundary presenter) {
        this.clubRead = clubRead;
        this.clubWrite = clubWrite;
        this.userDAO = userDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteClubInputData inputData) {
        try {
            long clubId = inputData.getClubId();
            String requester = inputData.getRequesterUsername();
            System.out.println("[DeleteClub][DEBUG] Request to delete club=" + clubId + " by='" + requester + "'");
            Club club = clubRead.getClub(clubId);
            if (club == null) { System.out.println("[DeleteClub][DEBUG] Club not found: " + clubId); presenter.prepareFailView("Club not found"); return; }
            Account user = (Account) userDAO.get(requester);
            if (user == null) { System.out.println("[DeleteClub][DEBUG] User not found: " + requester); presenter.prepareFailView("User not found"); return; }
            boolean isMember = false;
            for (Account m : club.getMembers()) if (m.getUsername().equals(requester)) { isMember = true; break; }
            if (!isMember) { System.out.println("[DeleteClub][DEBUG] Not authorized - requester not member"); presenter.prepareFailView("Not authorized to delete this club"); return; }
            System.out.println("[DeleteClub][DEBUG] Pre-delete total clubs=" + clubRead.getAllClubs().size());
            clubWrite.deleteClub(clubId);
            Club still = clubRead.getClub(clubId);
            if (still != null) { System.out.println("[DeleteClub][DEBUG] Post-delete club still exists!"); presenter.prepareFailView("Failed to delete club (data store still returns it)"); return; }
            ArrayList<Club> allClubs = clubRead.getAllClubs();
            System.out.println("[DeleteClub][DEBUG] Post-delete total clubs=" + allClubs.size());
            Account refreshedUser = (Account) userDAO.get(requester);
            ClubAnnouncementResult res = ClubAnnouncementCollector.collect(refreshedUser, allClubs);
            ArrayList<Club> memberClubs = new ArrayList<>(res.memberClubs());
            ArrayList<Club> nonMemberClubs = new ArrayList<>(res.nonMemberClubs());
            ArrayList<Post> announcements = new ArrayList<>(res.announcements());
            System.out.println("[DeleteClub][DEBUG] Recomputed memberClubs=" + memberClubs.size() + " nonMemberClubs=" + nonMemberClubs.size());
            presenter.prepareSuccessView(new DeleteClubOutputData(memberClubs, nonMemberClubs, announcements, true, "Club deleted"));
        } catch (Exception e) { System.out.println("[DeleteClub][DEBUG] Exception: " + e.getMessage()); presenter.prepareFailView("Error deleting club: " + e.getMessage()); }
    }
}
