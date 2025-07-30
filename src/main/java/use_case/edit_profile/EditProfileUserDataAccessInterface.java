package use_case.edit_profile;

import entity.User;

import java.awt.Image;

/**
 * DAO for the Edit Profile Use Case.
 */
public interface EditProfileUserDataAccessInterface {
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
     * @param user the user to save
     * @param newProfilePicture the new profile picture to save
     */
    void updateProfilePicture(User user, Image newProfilePicture);

    /**
     * Updates the user's preferences.
     * @param user the user to save
     * @param newPreferences the new preferences to save
     */
    void updatePreferences(User user, String newPreferences);

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
