package use_case.manage_following;

import use_case.UserDataAccessInterface;

/**
 * DAO for the Manage Following Use Case.
 */
public interface ManageFollowingUserDataAccessInterface extends UserDataAccessInterface {
    /**
     * Removes a user from the given user's following list.
     * @param currentUsername the user to remove from
     * @param removedUsername the user to remove
     */
    void removeFollowing(String currentUsername, String removedUsername);

    /**
     * Checks whether a user can follow another one.
     * @param username the user doing the following
     * @param otherUsername the user being followed
     * @return true if they can follow, false if not
     */
    boolean canFollow(String username, String otherUsername);

    /**
     * Adds a following from one user to another one.
     * @param username the user doing the following
     * @param otherUsername the user being followed
     */
    void addFollowing(String username, String otherUsername);

    /**
     * Removes a follow request from the user to another one.
     * @param currentUsername the user doing the removing
     * @param removedUsername the user being removed
     */
    void removeFollowRequest(String currentUsername, String removedUsername);

    /**
     * Checks whether a user can request to follow another one.
     * @param username the user doing the requesting
     * @param otherUsername the user being requested
     * @return true if they can request, false if not
     */
    boolean canRequestFollow(String username, String otherUsername);

    /**
     * Adds a follow request from a user to another one.
     * @param username the user doing the requesting
     * @param otherUsername the user being requested
     */
    void addFollowRequest(String username, String otherUsername);
}
