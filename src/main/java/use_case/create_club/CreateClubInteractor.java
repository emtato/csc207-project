package use_case.create_club;

import entity.Account;
import entity.Club;
import entity.User;

import java.util.ArrayList;
import java.util.List;

public class CreateClubInteractor implements CreateClubInputBoundary {
    private final ClubWriteOperations clubWrite; // was ClubCreationWrite
    private final CreateClubUserDataAccessInterface userGateway;
    private final CreateClubOutputBoundary createClubPresenter;

    public CreateClubInteractor(
            ClubWriteOperations clubWrite,
            CreateClubUserDataAccessInterface userGateway,
            CreateClubOutputBoundary createClubPresenter) {
        this.clubWrite = clubWrite;
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
            // Input validation
            if (title == null || title.trim().isEmpty()) {
                createClubPresenter.prepareFailView("Title cannot be empty");
                return;
            }

            if (description == null || description.trim().isEmpty()) {
                createClubPresenter.prepareFailView("Description cannot be empty");
                return;
            }

            if (clubWrite.clubExists(title)) {
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
            ArrayList<String> tagList = tags == null ? new ArrayList<>() : new ArrayList<>(tags);

            // Persist the club (no read-back needed for creation use case)
            clubWrite.writeClub(
                clubId,
                members,
                title,
                description,
                imageUrl,
                new ArrayList<>(),
                tagList
            );

            // Update each member's club list
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

            // Construct the created club object directly (avoids lookup dependency)
            Club createdClub = new Club(
                    title,
                    description,
                    imageUrl,
                    members,
                    new ArrayList<>(), // food preferences placeholder
                    new ArrayList<>(), // posts
                    clubId,
                    tagList
            );
            createClubPresenter.prepareSuccessView(new CreateClubOutputData(createdClub, true, null));

        } catch (Exception e) {
            createClubPresenter.prepareFailView("Error creating club: " + e.getMessage());
        }
    }
}
