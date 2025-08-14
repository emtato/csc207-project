package use_case.leave_club;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;

public class LeaveClubInteractor implements LeaveClubInputBoundary {
    private final ClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private final LeaveClubOutputBoundary presenter;

    public LeaveClubInteractor(ClubsDataAccessObject clubsDataAccessObject,
                               UserDataAccessObject userDataAccessObject,
                               LeaveClubOutputBoundary presenter) {
        this.clubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(LeaveClubInputData inputData) {
        try {
            String username = inputData.getUsername();
            long clubId = inputData.getClubId();

            Club club = clubsDataAccessObject.getClub(clubId);
            if (club == null) {
                presenter.prepareFailView("Club not found");
                return;
            }
            Account user = (Account) userDataAccessObject.get(username);
            if (user == null) {
                presenter.prepareFailView("User not found");
                return;
            }

            // Remove user from club members
            ArrayList<Account> members = club.getMembers();
            boolean removed = members.removeIf(m -> m.getUsername().equals(username));
            if (removed) {
                clubsDataAccessObject.writeClub(club.getId(), members, club.getName(), club.getDescription(), club.getImageUrl(), club.getPosts(), club.getTags());
            }

            // Update user's club list
            ArrayList<String> userClubs = user.getClubs();
            if (userClubs != null) {
                userClubs.remove(String.valueOf(clubId));
                user.setClubs(userClubs);
                userDataAccessObject.save(user);
            }

            // Rebuild lists
            ArrayList<Club> allClubs = clubsDataAccessObject.getAllClubs();
            ArrayList<Club> memberClubs = new ArrayList<>();
            ArrayList<Club> nonMemberClubs = new ArrayList<>();
            ArrayList<Post> announcements = new ArrayList<>();
            ArrayList<String> updatedUserClubs = user.getClubs();
            if (updatedUserClubs == null) {
                updatedUserClubs = new ArrayList<>();
            }
            for (Club c : allClubs) {
                String cid = String.valueOf(c.getId());
                if (updatedUserClubs.contains(cid)) {
                    memberClubs.add(c);
                    if (c.getPosts() != null) {
                        for (Post p : c.getPosts()) {
                            if (p != null && p.getType() != null && p.getType().trim().equalsIgnoreCase("announcement")) {
                                announcements.add(p);
                            }
                        }
                    }
                } else {
                    nonMemberClubs.add(c);
                }
            }

            presenter.prepareSuccessView(new LeaveClubOutputData(club, memberClubs, nonMemberClubs, announcements, true, null));
        } catch (Exception e) {
            presenter.prepareFailView("Error leaving club: " + e.getMessage());
        }
    }
}
