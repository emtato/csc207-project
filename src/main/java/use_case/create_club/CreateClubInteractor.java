package use_case.create_club;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;

import java.util.ArrayList;
import java.util.List;

public class CreateClubInteractor implements CreateClubInputBoundary {
    private final ClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private final CreateClubOutputBoundary createClubPresenter;

    public CreateClubInteractor(
            ClubsDataAccessObject clubsDataAccessObject,
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

            // Defensive null handling for memberUsernames
            if (memberUsernames == null) {
                memberUsernames = new ArrayList<>();
            }

            // Remove duplicate usernames while preserving order
            List<String> dedupedUsernames = new ArrayList<>();
            for (String u : memberUsernames) {
                if (u != null) {
                    String trimmed = u.trim();
                    if (!trimmed.isEmpty() && !dedupedUsernames.contains(trimmed)) {
                        dedupedUsernames.add(trimmed);
                    }
                }
            }

            ArrayList<Account> members = new ArrayList<>();
            ArrayList<String> invalidUsernames = new ArrayList<>();

            for (String username : dedupedUsernames) {
                Account member = (Account) userDataAccessObject.get(username);
                if (member != null) {
                    // Avoid duplicates by username
                    boolean alreadyAdded = false;
                    for (Account existing : members) {
                        if (existing.getUsername().equals(member.getUsername())) {
                            alreadyAdded = true;
                            break;
                        }
                    }
                    if (!alreadyAdded) {
                        members.add(member);
                    }
                } else {
                    invalidUsernames.add(username);
                }
            }

            if (!invalidUsernames.isEmpty()) {
                createClubPresenter.prepareFailView("Invalid member username(s): " + String.join(", ", invalidUsernames));
                return;
            }

            if (members.isEmpty()) {
                createClubPresenter.prepareFailView("Club must have at least one valid member");
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

            // Update each member's club list (avoid duplicate club IDs)
            String clubIdStr = String.valueOf(clubId);
            for (Account member : members) {
                ArrayList<String> clubList = member.getClubs();
                if (clubList == null) {
                    clubList = new ArrayList<>();
                }
                if (!clubList.contains(clubIdStr)) {
                    clubList.add(clubIdStr);
                }
                member.setClubs(clubList);
                userDataAccessObject.save(member);
            }

            // Get the created club and prepare success view
            Club createdClub = clubsDataAccessObject.getClub(clubId);
            createClubPresenter.prepareSuccessView(new CreateClubOutputData(createdClub, true, null));

        } catch (Exception e) {
            createClubPresenter.prepareFailView("Error creating club: " + e.getMessage());
        }
    }

    @Override
    public void showMemberSelection(List<Account> currentMembers, String creatorUsername) {
        try {
            ArrayList<Account> allUsers = userDataAccessObject.getAllUsers();
            ArrayList<Account> availableUsers = new ArrayList<>();

            for (Account user : allUsers) {
                boolean alreadyMember = false;
                for (Account existing : currentMembers) {
                    if (existing.getUsername().equals(user.getUsername())) { // compare by username
                        alreadyMember = true;
                        break;
                    }
                }
                if (!alreadyMember && !user.getUsername().equals(creatorUsername)) {
                    availableUsers.add(user);
                }
            }

            createClubPresenter.prepareMemberSelectionView(availableUsers);

        } catch (Exception e) {
            createClubPresenter.prepareFailView("Error loading users: " + e.getMessage());
        }
    }
}
