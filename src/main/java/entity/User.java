package entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getUsername();

    String getDisplayName();
    String getProfilePictureUrl();
    String getBio();
    int getNumFollowers();
    int getNumFollowing();
    HashMap<Long, Post> getUserPosts();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    ArrayList<String> getFoodPreferences();

    void setDisplayName(String newDisplayName);

    void setBio(String newBio);

    void setFoodPreferences(ArrayList<String> newPreferences);

    void setProfilePictureUrl(String newProfilePictureUrl);

}
