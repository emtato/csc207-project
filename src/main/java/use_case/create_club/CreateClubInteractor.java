package use_case.create_club;

import data_access.FileClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;

import java.util.ArrayList;
import java.util.List;

public class CreateClubInteractor implements CreateClubInputBoundary {
    private final FileClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private final CreateClubOutputBoundary createClubPresenter;

    public CreateClubInteractor(
            FileClubsDataAccessObject clubsDataAccessObject,
            UserDataAccessObject userDataAccessObject,
            CreateClubOutputBoundary createClubPresenter) {
        this.clubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.createClubPresenter = createClubPresenter;
    }

    @Override
    public void create(String title, String description, String imageUrl, List<String> tags, List<String> memberUsernames) {
        try {
            // Input validation
            if (title == null || title.trim().isEmpty()) {
                createClubPresenter.prepareFailView("Title cannot be empty");
                return;
            }

            if (description == null || description.trim().isEmpty()) {
                createClubPresenter.prepareFailView("Description cannot be empty");
                return;
            }

            // Check if club with same name exists
            if (clubsDataAccessObject.clubExists(title)) {
                createClubPresenter.prepareFailView("A club with this name already exists");
                return;
            }

            // Convert usernames to Account objects and validate
            ArrayList<Account> members = new ArrayList<>();
            for (String username : memberUsernames) {
                Account member = (Account) userDataAccessObject.get(username);
                if (member != null) {
                    members.add(member);
                }
            }

            if (members.isEmpty()) {
                createClubPresenter.prepareFailView("Club must have at least one member");
                return;
            }

            // Generate unique club ID
            long clubId = System.currentTimeMillis();

            // Create the club
            clubsDataAccessObject.writeClub(
                clubId,
                members,
                title,
                description,
                new ArrayList<>(),  // New club starts with no posts
                    (ArrayList<String>) tags
            );

            // Update each member's club list
            String clubIdStr = String.valueOf(clubId);
            for (Account member : members) {
                ArrayList<String> clubList = member.getClubs();
                if (clubList == null) {
                    clubList = new ArrayList<>();
                }
                clubList.add(clubIdStr);
                member.setClubs(clubList);
                userDataAccessObject.save(member);
            }

            // Get the created club and prepare success view
            Club createdClub = clubsDataAccessObject.getClub(clubIdStr);
            createClubPresenter.prepareSuccessView(new CreateClubOutputData(createdClub, true, null));

        } catch (Exception e) {
            createClubPresenter.prepareFailView("Error creating club: " + e.getMessage());
        }
    }

    @Override
    public void showMemberSelection(List<Account> currentMembers, String creatorUsername) {
        try {
            // Get all users except those already in the club
            ArrayList<Account> allUsers = userDataAccessObject.getAllUsers();
            ArrayList<Account> availableUsers = new ArrayList<>();

            for (Account user : allUsers) {
                // Skip users already in the club and the creator (who is automatically a member)
                if (!currentMembers.contains(user) && !user.getUsername().equals(creatorUsername)) {
                    availableUsers.add(user);
                }
            }

            createClubPresenter.prepareMemberSelectionView(availableUsers);

        } catch (Exception e) {
            createClubPresenter.prepareFailView("Error loading users: " + e.getMessage());
        }
    }
}
