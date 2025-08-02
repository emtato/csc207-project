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
}
