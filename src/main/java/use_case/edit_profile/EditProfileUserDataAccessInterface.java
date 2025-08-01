package use_case.edit_profile;

import entity.User;
import use_case.UserDataAccessInterface;

import java.util.ArrayList;

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

}
