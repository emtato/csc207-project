package use_case.manage_followers;

import use_case.UserDataAccessInterface;

/**
 * DAO for the Manage Followers Use Case.
 */
public interface ManageFollowersUserDataAccessInterface extends UserDataAccessInterface {
    /**
     * Removes a user from the given user's followers list.
     * @param currentUsername the user to remove from
     * @param removedUsername the user to remove
     */
    void removeFollower(String currentUsername, String removedUsername);

    /**
     * Removes a user from the given user's requesters list.
     * @param currentUsername the user to remove from
     * @param removedUsername the user to remove
     */
    void removeFollowRequester(String currentUsername, String removedUsername);

    /**
     * Adds a following from one user to another one.
     * @param username the user doing the following
     * @param otherUsername the user being followed
     */
    void addFollowing(String username, String otherUsername);
}
