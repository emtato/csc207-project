package use_case.leave_club;

import use_case.club.ClubAnnouncementCollector;
import use_case.club.ClubAnnouncementResult;
import use_case.club.ClubReadOperations;
import use_case.club.ClubMembershipMutation;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;

public class LeaveClubInteractor implements LeaveClubInputBoundary {
    private final ClubReadOperations clubRead;
    private final ClubMembershipMutation membershipMutation;
    private final UserDataAccessObject userDataAccessObject;
    private final LeaveClubOutputBoundary presenter;

    public LeaveClubInteractor(ClubReadOperations clubRead,
                               ClubMembershipMutation membershipMutation,
                               UserDataAccessObject userDataAccessObject,
                               LeaveClubOutputBoundary presenter) {
        this.clubRead = clubRead;
        this.membershipMutation = membershipMutation;
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(LeaveClubInputData inputData) {
        try {
            String username = inputData.getUsername();
            long clubId = inputData.getClubId();

            Club club = clubRead.getClub(clubId);
            if (club == null) {
                presenter.prepareFailView("Club not found");
                return;
            }
            Account user = (Account) userDataAccessObject.get(username);
            if (user == null) {
                presenter.prepareFailView("User not found");
                return;
            }

            membershipMutation.removeMemberFromClub(username, clubId);
            club = clubRead.getClub(clubId);
            if (club == null) {
                presenter.prepareFailView("Club no longer exists");
                return;
            }

            user = (Account) userDataAccessObject.get(username);

            ArrayList<Club> allClubs = clubRead.getAllClubs();
            ClubAnnouncementResult res = ClubAnnouncementCollector.collect(user, allClubs);
            ArrayList<Club> memberClubs = new ArrayList<>(res.memberClubs());
            ArrayList<Club> nonMemberClubs = new ArrayList<>(res.nonMemberClubs());
            ArrayList<Post> announcements = new ArrayList<>(res.announcements());

            presenter.prepareSuccessView(new LeaveClubOutputData(club, memberClubs, nonMemberClubs, announcements, true, null));
        } catch (Exception e) {
            presenter.prepareFailView("Error leaving club: " + e.getMessage());
        }
    }
}
