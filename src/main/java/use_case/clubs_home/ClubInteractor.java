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
            System.out.println("DEBUG: Fetching clubs data for user: " + username);
            Account currentUser = (Account) userDataAccessObject.get(username);
            if (currentUser == null) {
                System.out.println("DEBUG: User not found: " + username);
                return new ClubOutputData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, "User not found");
            }
            ArrayList<String> userClubIds = currentUser.getClubs();
            if (userClubIds == null) {
                userClubIds = new ArrayList<>();
                currentUser.setClubs(userClubIds);
                userDataAccessObject.save(currentUser);
            }
            System.out.println("DEBUG: Retrieved user's club IDs from Account: " + userClubIds);

            boolean reconstructed = false;
            if (userClubIds.isEmpty()) {
                System.out.println("DEBUG: User club list empty; attempting reconstruction from club member rosters");
                ArrayList<Club> scan = clubsDataAccessObject.getAllClubs();
                for (Club c : scan) {
                    if (c.getMembers().stream().anyMatch(m -> m.getUsername().equals(username))) {
                        String cid = String.valueOf(c.getId());
                        if (!userClubIds.contains(cid)) {
                            userClubIds.add(cid);
                            reconstructed = true;
                            System.out.println("DEBUG: Reconstructed membership for club ID: " + cid);
                        }
                    }
                }
                if (reconstructed) {
                    currentUser.setClubs(userClubIds);
                    userDataAccessObject.save(currentUser);
                    System.out.println("DEBUG: Reconstruction complete. Updated user clubs: " + userClubIds);
                } else {
                    System.out.println("DEBUG: Reconstruction found no memberships");
                }
            }

            ArrayList<Club> allClubs = clubsDataAccessObject.getAllClubs();
            System.out.println("DEBUG: Total clubs found: " + allClubs.size());
            ArrayList<Club> memberClubs = new ArrayList<>();
            ArrayList<Club> nonMemberClubs = new ArrayList<>();
            ArrayList<Post> announcements = new ArrayList<>();

            for (Club club : allClubs) {
                String cid = String.valueOf(club.getId());
                System.out.println("DEBUG: Processing club: " + club.getName() + " (ID: " + cid + ")");
                System.out.println("DEBUG: Checking if " + cid + " is in user's clubs: " + userClubIds.contains(cid));
                if (userClubIds.contains(cid)) {
                    memberClubs.add(club);
                    ArrayList<Post> clubPosts = club.getPosts();
                    if (clubPosts != null) {
                        int before = announcements.size();
                        for (Post p : clubPosts) {
                            if (p != null && p.getType() != null && p.getType().trim().equalsIgnoreCase("announcement")) {
                                announcements.add(p);
                            }
                        }
                        System.out.println("DEBUG: Added " + (announcements.size() - before) + " announcements from club: " + club.getName());
                    }
                } else {
                    nonMemberClubs.add(club);
                }
            }
            System.out.println("DEBUG: Final counts - Member clubs: " + memberClubs.size() + ", Non-member clubs: " + nonMemberClubs.size() + ", Announcements: " + announcements.size());
            ClubOutputData out = new ClubOutputData(memberClubs, nonMemberClubs, announcements, true, null);
            clubPresenter.prepareSuccessView(out);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
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
