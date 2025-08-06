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

    boolean canFollow(String username, String otherUsername);

    void addFollowing(String username, String otherUsername);

    void removeFollowRequest(String currentUsername, String removedUsername);

    boolean canRequestFollow(String username, String otherUsername);

    void addFollowRequest(String username, String otherUsername);
}
