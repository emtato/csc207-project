package use_case.create_club;

import entity.Account;
import entity.Post;
import java.util.ArrayList;

/**
 * Consolidated write-side operations for clubs (create/update + existence + deletion).
 */
public interface ClubWriteOperations {
    void writeClub(long clubID, ArrayList<Account> members, String name, String description, String imageUrl, ArrayList<Post> posts, ArrayList<String> tags);
    boolean clubExists(String clubName);
    void deleteClub(long clubID);
}

