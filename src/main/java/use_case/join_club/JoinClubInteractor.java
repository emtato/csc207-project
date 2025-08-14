package use_case.join_club;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;

public class JoinClubInteractor implements JoinClubInputBoundary {
    private final ClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private final JoinClubOutputBoundary presenter;

    public JoinClubInteractor(ClubsDataAccessObject clubsDataAccessObject,
                              UserDataAccessObject userDataAccessObject,
                              JoinClubOutputBoundary presenter) {
        this.clubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(JoinClubInputData inputData) {
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

            // Add user to club if not already a member
            boolean already = club.getMembers().stream().anyMatch(a -> a.getUsername().equals(username));
            if (!already) {
                ArrayList<Account> members = club.getMembers();
                members.add(user);
                clubsDataAccessObject.writeClub(club.getId(), members, club.getName(), club.getDescription(), club.getImageUrl(), club.getPosts(), club.getTags());
            }

            // Update user club list
            ArrayList<String> userClubs = user.getClubs();
            if (userClubs == null) {
                userClubs = new ArrayList<>();
            }
            String clubIdStr = String.valueOf(clubId);
            if (!userClubs.contains(clubIdStr)) {
                userClubs.add(clubIdStr);
                user.setClubs(userClubs);
                userDataAccessObject.save(user);
            }

            // Build updated lists (reuse listing logic inline)
            ArrayList<Club> allClubs = clubsDataAccessObject.getAllClubs();
            ArrayList<Club> memberClubs = new ArrayList<>();
            ArrayList<Club> nonMemberClubs = new ArrayList<>();
            ArrayList<Post> announcements = new ArrayList<>();
            for (Club c : allClubs) {
                if (userClubs.contains(String.valueOf(c.getId()))) {
                    memberClubs.add(c);
                    ArrayList<Post> posts = c.getPosts();
                    if (posts != null) {
                        for (Post p : posts) {
                            if (p != null && p.getType() != null && p.getType().trim().equalsIgnoreCase("announcement")) {
                                announcements.add(p);
                            }
                        }
                    }
                } else {
                    nonMemberClubs.add(c);
                }
            }

            presenter.prepareSuccessView(new JoinClubOutputData(memberClubs, nonMemberClubs, announcements, true, null));
        } catch (Exception e) {
            presenter.prepareFailView("Error joining club: " + e.getMessage());
        }
    }
}
