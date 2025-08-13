package data_access;

import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * In-memory implementation of the ClubsDataAccessObject. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryClubsDataAccessObject implements ClubsDataAccessObject {
    private static ClubsDataAccessObject instance;
    private final HashMap<Long, Club> clubs = new HashMap<>();
    private final PostCommentsLikesDataAccessObject postDAO;

    public InMemoryClubsDataAccessObject(PostCommentsLikesDataAccessObject postDAO) {
        this.postDAO = postDAO;
    }

    public static ClubsDataAccessObject getInstance(PostCommentsLikesDataAccessObject postDAO) {
        if (instance == null) {
            instance = new InMemoryClubsDataAccessObject(postDAO);
        }
        return instance;
    }

    @Override
    public void writeClub(long clubID, ArrayList<Account> members, String name, String description, ArrayList<Post> posts, ArrayList<String> tags) {
        Club club = new Club(name, description, members != null ? members : new ArrayList<>(),
                           new ArrayList<>(), posts != null ? posts : new ArrayList<>(),
                           clubID, tags != null ? tags : new ArrayList<>());
        clubs.put(clubID, club);
    }

    @Override
    public Club getClub(long clubID) {
        return clubs.get(clubID);
    }

    @Override
    public ArrayList<Club> getAllClubs() {
        return new ArrayList<>(clubs.values());
    }

    /**
     * Checks if a club with the given name already exists
     * @param clubName The name to check
     * @return true if a club with this name exists, false otherwise
     */
    public boolean clubExists(String clubName) {
        for (Club club : clubs.values()) {
            if (club.getName().equalsIgnoreCase(clubName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove a member from a club
     * @param username The username of the member to remove
     * @param clubID The ID of the club
     */
    public void removeMemberFromClub(String username, long clubID) {
        Club club = clubs.get(clubID);
        if (club != null) {
            ArrayList<Account> updatedMembers = new ArrayList<>(club.getMembers());
            updatedMembers.removeIf(member -> member.getUsername().equals(username));

            // Create a new club instance with updated members
            Club updatedClub = new Club(
                club.getName(),
                club.getDescription(),
                updatedMembers,
                club.getFoodPreferences(),
                club.getPosts(),
                club.getId(),
                club.getTags()
            );
            clubs.put(clubID, updatedClub);

            // Update user's club membership
            FileUserDataAccessObject userDAO = (FileUserDataAccessObject) FileUserDataAccessObject.getInstance();
            userDAO.removeClubFromUser(username, String.valueOf(clubID));
        }
    }

    @Override
    public void deleteClub(long clubID) {
        clubs.remove(clubID);
    }
}
