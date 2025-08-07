package use_case.toggle_settings;

import entity.User;
import use_case.UserDataAccessInterface;

/**
 * DAO for the Settings Use Case.
 */
public interface SettingsUserDataAccessInterface extends UserDataAccessInterface {
    /**
     * Sets a user's privacy setting to the given privacy setting.
     * @param user the user to change
     * @param privacy true if setting to public, false if setting to private
     */
    void setPrivacy(User user, boolean privacy);

    /**
     * Switches to the Manage Following View.
     * @param user the user to change
     * @param enabled true if enabling notifications, false if disabling
     */
    void setNotificationStatus(User user, boolean enabled);
}
