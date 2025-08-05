package use_case.delete_account;

import use_case.UserDataAccessInterface;

/**
 * DAO for the Delete Account Use Case.
 */
public interface DeleteAccountUserDataAccessInterface extends UserDataAccessInterface {
    /**
     * Deletes the account of the user with the given username.
     * @param username the username of the user to delete
     */
    void deleteAccount(String username);
}
