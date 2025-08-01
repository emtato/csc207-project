package use_case.logout;

import use_case.UserDataAccessInterface;

/**
 * DAO for the Logout Use Case.
 */
public interface LogoutUserDataAccessInterface extends UserDataAccessInterface {

    /**
     * Returns the username of the curren user of the application.
     * @return the username of the current user
     */
    String getCurrentUsername();

    /**
     * Sets the username indicating who is the current user of the application.
     * @param username the new current username
     */
    void setCurrentUsername(String username);
}
