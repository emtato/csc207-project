package use_case;

import entity.User;

/**
 * General DAO for getting.
 */
public interface UserDataAccessInterface {
    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);
}
