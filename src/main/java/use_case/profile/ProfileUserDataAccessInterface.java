package use_case.profile;

import entity.User;

/**
 * DAO for the Profile Use Case.
 */
public interface ProfileUserDataAccessInterface {
    //TODO
    // this method overlaps with other data access interfaces, so maybe ill make another interface
    // containing this method for other interfaces to extend
    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);
}
