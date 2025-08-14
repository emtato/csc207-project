package use_case.create_club;

import entity.Account;
import entity.Club;
import entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // added

public class CreateClubInteractor implements CreateClubInputBoundary {
    private final CreateClubClubsDataAccessInterface clubsGateway;
    private final CreateClubUserDataAccessInterface userGateway;
    private final CreateClubOutputBoundary createClubPresenter;

    public CreateClubInteractor(
            CreateClubClubsDataAccessInterface clubsGateway,
            CreateClubUserDataAccessInterface userGateway,
            CreateClubOutputBoundary createClubPresenter) {
        this.clubsGateway = clubsGateway;
        this.userGateway = userGateway;
        this.createClubPresenter = createClubPresenter;
    }

    @Override
    public void execute(CreateClubInputData inputData) {
        String title = inputData.getTitle();
        String description = inputData.getDescription();
        String imageUrl = inputData.getImageUrl();
        List<String> tags = inputData.getTags();
        List<String> memberUsernames = inputData.getMemberUsernames();
        try {
            System.out.println("[CreateClub][DEBUG] Requested create: title='" + title + "' imageUrl='" + imageUrl + "'");
            System.out.println("[CreateClub][DEBUG] Initial member usernames list: " + memberUsernames);

            // Input validation
            if (title == null || title.trim().isEmpty()) {
                createClubPresenter.prepareFailView("Title cannot be empty");
                return;
            }

            if (description == null || description.trim().isEmpty()) {
                createClubPresenter.prepareFailView("Description cannot be empty");
                return;
            }

            if (clubsGateway.clubExists(title)) {
                createClubPresenter.prepareFailView("A club with this name already exists");
                return;
            }

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

            System.out.println("[CreateClub][DEBUG] Deduped usernames: " + dedupedUsernames);

            ArrayList<Account> members = new ArrayList<>();
            ArrayList<String> invalidUsernames = new ArrayList<>();

            for (String username : dedupedUsernames) {
                User user = userGateway.get(username);
                Account member = user instanceof Account ? (Account) user : null;
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
            ArrayList<String> tagList = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
            System.out.println("[CreateClub][DEBUG] Persisting club id=" + clubId + " with members=" + members.stream().map(Account::getUsername).collect(Collectors.toList()) + " imageUrl='" + imageUrl + "'");
            clubsGateway.writeClub(
                clubId,
                members,
                title,
                description,
                imageUrl,
                new ArrayList<>(),  // New club starts with no posts
                tagList
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
                userGateway.save(member);
            }

            // Get the created club and prepare success view
            Club createdClub = clubsGateway.getClub(clubId);
            System.out.println("[CreateClub][DEBUG] Club created successfully. Stored club imageUrl='" + createdClub.getImageUrl() + "'");
            createClubPresenter.prepareSuccessView(new CreateClubOutputData(createdClub, true, null));

        } catch (Exception e) {
            createClubPresenter.prepareFailView("Error creating club: " + e.getMessage());
        }
    }

    @Override
    public void showMemberSelection(List<Account> currentMembers, String creatorUsername) {
        try {
            // Invalidate user cache (if supported) to ensure we see latest users
            try {
                userGateway.getClass().getMethod("invalidateCache").invoke(userGateway);
                System.out.println("[CreateClub][DEBUG] Invoked userGateway.invalidateCache()");
            } catch (Exception ignore) {
                System.out.println("[CreateClub][DEBUG] invalidateCache not available on userGateway");
            }

            ArrayList<Account> allUsers = userGateway.getAllUsers();
            System.out.println("[CreateClub][DEBUG] Total users fetched from DB = " + (allUsers == null ? 0 : allUsers.size()));
            if (allUsers != null) {
                for (Account a : allUsers) {
                    System.out.println("[CreateClub][DEBUG] User candidate: " + a.getUsername());
                }
            }
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
            System.out.println("[CreateClub][DEBUG] Available (non-member) users count = " + availableUsers.size());

            createClubPresenter.prepareMemberSelectionView(availableUsers);

        } catch (Exception e) {
            createClubPresenter.prepareFailView("Error loading users: " + e.getMessage());
        }
    }
}
