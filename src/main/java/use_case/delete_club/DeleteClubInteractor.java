package use_case.delete_club;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;
import java.util.ArrayList;

public class DeleteClubInteractor implements DeleteClubInputBoundary {
    private final ClubsDataAccessObject clubsDAO;
    private final UserDataAccessObject userDAO;
    private final DeleteClubOutputBoundary presenter;

    public DeleteClubInteractor(ClubsDataAccessObject clubsDAO,
                                UserDataAccessObject userDAO,
                                DeleteClubOutputBoundary presenter) {
        this.clubsDAO = clubsDAO;
        this.userDAO = userDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteClubInputData inputData) {
        try {
            long clubId = inputData.getClubId();
            String requester = inputData.getRequesterUsername();
            System.out.println("[DeleteClub][DEBUG] Request to delete club=" + clubId + " by='" + requester + "'");
            Club club = clubsDAO.getClub(clubId);
            if (club == null) { System.out.println("[DeleteClub][DEBUG] Club not found: " + clubId); presenter.prepareFailView("Club not found"); return; }
            Account user = (Account) userDAO.get(requester);
            if (user == null) { System.out.println("[DeleteClub][DEBUG] User not found: " + requester); presenter.prepareFailView("User not found"); return; }
            boolean isMember = false;
            for (Account m : club.getMembers()) if (m.getUsername().equals(requester)) { isMember = true; break; }
            if (!isMember) { System.out.println("[DeleteClub][DEBUG] Not authorized - requester not member"); presenter.prepareFailView("Not authorized to delete this club"); return; }
            System.out.println("[DeleteClub][DEBUG] Pre-delete total clubs=" + clubsDAO.getAllClubs().size());
            clubsDAO.deleteClub(clubId);
            Club still = clubsDAO.getClub(clubId);
            if (still != null) { System.out.println("[DeleteClub][DEBUG] Post-delete club still exists!"); presenter.prepareFailView("Failed to delete club (data store still returns it)"); return; }
            ArrayList<Club> allClubs = clubsDAO.getAllClubs();
            System.out.println("[DeleteClub][DEBUG] Post-delete total clubs=" + allClubs.size());
            ArrayList<Club> memberClubs = new ArrayList<>();
            ArrayList<Club> nonMemberClubs = new ArrayList<>();
            ArrayList<Post> announcements = new ArrayList<>();
            Account refreshedUser = (Account) userDAO.get(requester);
            ArrayList<String> userClubs = refreshedUser != null ? refreshedUser.getClubs() : new ArrayList<>();
            if (userClubs == null) userClubs = new ArrayList<>();
            for (Club c : allClubs) {
                String cid = String.valueOf(c.getId());
                if (userClubs.contains(cid)) {
                    memberClubs.add(c);
                    if (c.getPosts() != null) for (Post p : c.getPosts()) if (p != null && p.getType() != null && p.getType().trim().equalsIgnoreCase("announcement")) announcements.add(p);
                } else nonMemberClubs.add(c);
            }
            System.out.println("[DeleteClub][DEBUG] Recomputed memberClubs=" + memberClubs.size() + " nonMemberClubs=" + nonMemberClubs.size());
            presenter.prepareSuccessView(new DeleteClubOutputData(memberClubs, nonMemberClubs, announcements, true, "Club deleted"));
        } catch (Exception e) { System.out.println("[DeleteClub][DEBUG] Exception: " + e.getMessage()); presenter.prepareFailView("Error deleting club: " + e.getMessage()); }
    }
}
