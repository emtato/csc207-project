package use_case.edit_profile;

import java.util.ArrayList;

import entity.User;
import use_case.UserDataAccessInterface;

/**
 * DAO for the Edit Profile Use Case.
 */
public interface EditProfileUserDataAccessInterface extends UserDataAccessInterface {
    /**
     * Updates the display name.
     * @param user the user to save
     * @param newDisplayName the new display name to save
     */
    void updateDisplayName(User user, String newDisplayName);

    /**
     * Updates the bio.
     * @param user the user to save
     * @param newBio the new bio to save
     */
    void updateBio(User user, String newBio);

    /**
     * Updates the profile picture.
     *
     * @param user                 the user to save
     * @param newProfilePictureUrl the new profile picture url to save
     */
    void updateProfilePictureUrl(User user, String newProfilePictureUrl);

    /**
     * Updates the user's preferences.
     * @param user the user to save
     * @param newPreferences the new preferences to save
     */
    void updatePreferences(User user, ArrayList<String> newPreferences);

    /**
     * Updates the user's location. If null, it's based on IP address.
     * @param user the user to save
     * @param newLocation the new location to save
     */
    void updateLocation(User user, String newLocation);
}
