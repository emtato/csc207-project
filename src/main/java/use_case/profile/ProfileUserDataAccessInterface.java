package use_case.profile;

import entity.Post;
import entity.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DAO for the Profile Use Case.
 */
public interface ProfileUserDataAccessInterface {
    String getCurrentUserName();

    String getDisplayName(String username);

    String getBio(String username);

    Image getProfilePicture(String username);

    int getNumFollowers(String username);

    int getNumFollowing(String username);

    HashMap<Long, Post> getPosts(String username);

    ArrayList<String> getPreferences(String username);
}
