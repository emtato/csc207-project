package use_case.change_password;

import entity.User;
import use_case.UserDataAccessInterface;

/**
 * The interface of the DAO for the Change Password Use Case.
 */
public interface ChangePasswordUserDataAccessInterface extends UserDataAccessInterface {

    /**
     * Updates the system to record this user's password.
     * @param user the user whose password is to be updated
     */
    void changePassword(User user);
}
