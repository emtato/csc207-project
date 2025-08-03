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
    void setUsername(String username);

    String getDisplayName();
    void setDisplayName(String newDisplayName);

    String getProfilePictureUrl();
    void setProfilePictureUrl(String newProfilePictureUrl);

    String getBio();
    void setBio(String newBio);

    int getNumFollowers();
    int getNumFollowing();

    HashMap<String, User> getFollowerAccounts();
    void setFollowerAccounts(HashMap<String, User> followerAccounts);

    HashMap<String, User> getFollowingAccounts();
    void setFollowingAccounts(HashMap<String, User> followingAccounts);

    HashMap<Long, Post> getUserPosts();
    void setUserPosts(HashMap<Long, Post> userPosts);

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();
    void setPassword(String newPassword);

    ArrayList<String> getFoodPreferences();
    void setFoodPreferences(ArrayList<String> newPreferences);

    boolean isPublic();
    void setPublic(boolean isPublic);

    boolean isNotificationsEnabled();
    void setNotificationsEnabled(boolean notificationsEnabled);
}
