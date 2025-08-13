package use_case.list_clubs;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;

public class ListClubsInteractor implements ListClubsInputBoundary {
    private final ClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private final ListClubsOutputBoundary presenter;

    public ListClubsInteractor(ClubsDataAccessObject clubsDataAccessObject,
                               UserDataAccessObject userDataAccessObject,
                               ListClubsOutputBoundary presenter) {
        this.clubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(ListClubsInputData inputData) {
        try {
            String username = inputData.getUsername();
            Account currentUser = (Account) userDataAccessObject.get(username);
            if (currentUser == null) {
                presenter.prepareFailView("User not found");
                return;
            }
            ArrayList<String> userClubIds = currentUser.getClubs();
            if (userClubIds == null) {
                userClubIds = new ArrayList<>();
                currentUser.setClubs(userClubIds);
                userDataAccessObject.save(currentUser);
            }

            boolean reconstructed = false;
            if (userClubIds.isEmpty()) {
                ArrayList<Club> scan = clubsDataAccessObject.getAllClubs();
                for (Club c : scan) {
                    if (c.getMembers().stream().anyMatch(m -> m.getUsername().equals(username))) {
                        String cid = String.valueOf(c.getId());
                        if (!userClubIds.contains(cid)) {
                            userClubIds.add(cid);
                            reconstructed = true;
                        }
                    }
                }
                if (reconstructed) {
                    currentUser.setClubs(userClubIds);
                    userDataAccessObject.save(currentUser);
                }
            }

            ArrayList<Club> allClubs = clubsDataAccessObject.getAllClubs();
            ArrayList<Club> memberClubs = new ArrayList<>();
            ArrayList<Club> nonMemberClubs = new ArrayList<>();
            ArrayList<Post> announcements = new ArrayList<>();

            for (Club club : allClubs) {
                String cid = String.valueOf(club.getId());
                if (userClubIds.contains(cid)) {
                    memberClubs.add(club);
                    ArrayList<Post> clubPosts = club.getPosts();
                    if (clubPosts != null) {
                        for (Post p : clubPosts) {
                            if (p != null && p.getType() != null && p.getType().trim().equalsIgnoreCase("announcement")) {
                                announcements.add(p);
                            }
                        }
                    }
                } else {
                    nonMemberClubs.add(club);
                }
            }

            presenter.prepareSuccessView(new ListClubsOutputData(memberClubs, nonMemberClubs, announcements, true, null));
        } catch (Exception e) {
            presenter.prepareFailView("Error fetching clubs data: " + e.getMessage());
        }
    }
}

