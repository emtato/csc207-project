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
}
