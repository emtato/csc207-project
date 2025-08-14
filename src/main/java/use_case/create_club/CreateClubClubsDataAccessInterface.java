package use_case.create_club;

import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;

/**
 * Boundary interface (gateway) for persistence operations needed by the Create Club use case.
 * Defined in the use_case layer to keep the interactor independent of outer data access details.
 */
public interface CreateClubClubsDataAccessInterface {
    void writeClub(long clubID, ArrayList<Account> members, String name, String description, String imageUrl, ArrayList<Post> posts, ArrayList<String> tags);
    Club getClub(long clubID);
    boolean clubExists(String clubName);
}
