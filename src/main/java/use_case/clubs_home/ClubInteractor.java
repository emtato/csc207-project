package use_case.clubs_home;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;
import use_case.create_club.CreateClubInputData;

import java.util.ArrayList;

public class ClubInteractor implements ClubInputBoundary {
    private final ClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private final ClubOutputBoundary clubPresenter;

    public ClubInteractor(
            ClubsDataAccessObject clubsDataAccessObject,
            UserDataAccessObject userDataAccessObject,
            ClubOutputBoundary clubPresenter) {
        this.clubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.clubPresenter = clubPresenter;
    }

    @Override
    public ClubOutputData getClubsData(String username) {
        try {
            ArrayList<Club> allClubs = clubsDataAccessObject.getAllClubs();
            ArrayList<Club> memberClubs = new ArrayList<>();
            ArrayList<Club> nonMemberClubs = new ArrayList<>();
            ArrayList<Post> announcements = new ArrayList<>();

            Account currentUser = (Account) userDataAccessObject.get(username);
            ArrayList<String> userClubIds = currentUser != null && currentUser.getClubs() != null ?
                    currentUser.getClubs() : new ArrayList<>();

            // Sort clubs into member and non-member lists
            for (Club club : allClubs) {
                if (userClubIds.contains(String.valueOf(club.getId()))) {
                    memberClubs.add(club);
                    // Collect announcements from member clubs
                    if (club.getPosts() != null) {
                        announcements.addAll(club.getPosts());
                    }
                } else {
                    nonMemberClubs.add(club);
                }
            }

            ClubOutputData outputData = new ClubOutputData(memberClubs, nonMemberClubs, announcements, true, null);
            clubPresenter.prepareSuccessView(outputData);
            return outputData;
        } catch (Exception e) {
            clubPresenter.prepareFailView("Error fetching clubs data: " + e.getMessage());
            return new ClubOutputData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, e.getMessage());
        }
    }

    @Override
    public ClubOutputData joinClub(ClubInputData clubInputData) {
        try {
            String username = clubInputData.getUsername();
            String clubId = clubInputData.getClubIdToJoin();

            // Convert clubId to long before calling getClub
            Club club = clubsDataAccessObject.getClub(Long.parseLong(clubId));
            if (club == null) {
                throw new IllegalArgumentException("Club not found");
            }

            // Add user to club members
            ArrayList<Account> members = club.getMembers();
            members.add(new Account(username, ""));
            clubsDataAccessObject.writeClub(
                club.getId(),
                members,
                club.getName(),
                club.getDescription(),
                club.getPosts(),
                club.getTags()
            );

            // Update user's club membership
            Account user = (Account) userDataAccessObject.get(username);
            if (user != null) {
                ArrayList<String> clubList = user.getClubs();
                if (clubList == null) {
                    clubList = new ArrayList<>();
                }
                clubList.add(clubId);
                user.setClubs(clubList);
                userDataAccessObject.save(user);
            }

            // Return updated clubs data
            return getClubsData(username);
        } catch (Exception e) {
            clubPresenter.prepareFailView("Error joining club: " + e.getMessage());
            return new ClubOutputData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, e.getMessage());
        }
    }

    @Override
    public ClubOutputData createClub(CreateClubInputData createClubInputData) {
        try {
            // Generate a unique club ID using current timestamp
            long clubId = System.currentTimeMillis();

            // Convert usernames to Account objects
            ArrayList<Account> members = new ArrayList<>();
            for (String username : createClubInputData.getMemberUsernames()) {
                Account member = (Account) userDataAccessObject.get(username);
                if (member != null) {
                    members.add(member);
                }
            }

            // Create club
            clubsDataAccessObject.writeClub(
                clubId,
                members,
                createClubInputData.getTitle(),
                createClubInputData.getDescription(),
                new ArrayList<>(),  // New club starts with no posts
                new ArrayList<>(createClubInputData.getTags())  // Convert List to ArrayList
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

            // Return updated clubs data for the creating user
            return getClubsData(members.get(0).getUsername());
        } catch (Exception e) {
            clubPresenter.prepareFailView("Error creating club: " + e.getMessage());
            return new ClubOutputData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, e.getMessage());
        }
    }
}
