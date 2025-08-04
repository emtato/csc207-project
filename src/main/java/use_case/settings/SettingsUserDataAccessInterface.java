package use_case.settings;

import entity.User;
import use_case.UserDataAccessInterface;

/**
 * DAO for the Settings Use Case.
 */
public interface SettingsUserDataAccessInterface extends UserDataAccessInterface {
    void setPrivacy(User user, boolean privacy);
    void setNotificationStatus(User user, boolean enabled);
}
