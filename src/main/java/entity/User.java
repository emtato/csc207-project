package entity;

import java.util.ArrayList;
import java.util.Map;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getUsername();

    /**
     * Sets the username of the user.
     * @param username the new username of the user.
     */
    void setUsername(String username);

    /**
     * Returns the display name of the user.
     * @return the display name of the user.
     */
    String getDisplayName();

    /**
     * Sets the display name of the user.
     * @param newDisplayName the new display name of the user.
     */
    void setDisplayName(String newDisplayName);

    /**
     * Returns the profile picture url of the user.
     * @return the profile picture url of the user.
     */
    String getProfilePictureUrl();

    /**
     * Sets the pfp url of the user.
     * @param newProfilePictureUrl the new pfp url of the user.
     */
    void setProfilePictureUrl(String newProfilePictureUrl);

    /**
     * Returns the bio of the user.
     * @return the bio of the user.
     */
    String getBio();

    /**
     * Sets the bio of the user.
     * @param newBio the new bio of the user.
     */
    void setBio(String newBio);

    /**
     * Returns the location of the user.
     * @return the location of the user.
     */
    String getLocation();

    /**
     * Sets the bio of the user.
     * @param newLocation the new bio of the user.
     */
    void setLocation(String newLocation);

    /**
     * Returns the number of followers of the user.
     * @return the number of followers of the user.
     */
    int getNumFollowers();

    /**
     * Returns the number of users followed by the user.
     * @return the number of users followed by the user.
     */
    int getNumFollowing();

    /**
     * Returns a map of the user's followers.
     * @return the followers of the user.
     */
    Map<String, User> getFollowerAccounts();

    /**
     * Sets the followers of the user.
     * @param followerAccounts the new followers of the user.
     */
    void setFollowerAccounts(Map<String, User> followerAccounts);

    /**
     * Returns the accounts following the user.
     * @return the accounts following the user.
     */
    Map<String, User> getFollowingAccounts();

    /**
     * Sets the following users of the user.
     * @param followingAccounts the new users followed by the user.
     */
    void setFollowingAccounts(Map<String, User> followingAccounts);

    /**
     * Returns the users requesting to follow the user.
     * @return the users requesting to follow the user.
     */
    Map<String, User> getRequesterAccounts();

    /**
     * Sets the map of users requesting to followw the user.
     * @param requesterAccounts the new requesters to follow of the user.
     */
    void setRequesterAccounts(Map<String, User> requesterAccounts);

    /**
     * Returns the users that the user has requested to follow.
     * @return the users that the user has requested to follow.
     */
    Map<String, User> getRequestedAccounts();

    /**
     * Sets the map of users that the user has requested to follow.
     * @param requestedAccounts the new requested users.
     */
    void setRequestedAccounts(Map<String, User> requestedAccounts);

    /**
     * Returns the IDs of the user's posts.
     * @return the IDs of the user's posts.
     */
    ArrayList<Long> getUserPosts();

    /**
     * Sets the new posts of the user.
     * @param userPosts the new IDs of posts.
     */
    void setUserPosts(ArrayList<Long> userPosts);

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Sets the password of the user.
     * @param newPassword the new password of the user.
     */
    void setPassword(String newPassword);

    /**
     * Returns the preference tags of the user.
     * @return the preference tags of the user.
     */
    ArrayList<String> getFoodPreferences();

    /**
     * Sets the food preference tags of the user.
     * @param newPreferences the new list of preference tags.
     */
    void setFoodPreferences(ArrayList<String> newPreferences);

    /**
     * Returns whether the user's account is public.
     * @return true if the account is public, false if private.
     */
    boolean isPublic();

    /**
     * Sets the privacy of the user.
     * @param isPublic whether the user's privacy is public.
     */
    void setPublic(boolean isPublic);

    /**
     * Returns whether the user has notifications enabled.
     * @return true if the user has notifications enabled, false if not.
     */
    boolean isNotificationsEnabled();

    /**
     * Sets the notification enabled status of the user.
     * @param notificationsEnabled whether the user has notifications enabled.
     */
    void setNotificationsEnabled(boolean notificationsEnabled);
}
