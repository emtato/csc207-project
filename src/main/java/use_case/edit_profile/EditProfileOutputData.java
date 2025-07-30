package use_case.edit_profile;

import java.awt.Image;

/**
 * Output Data for the Edit Profile Use Case.
 */
public class EditProfileOutputData {
    private final String newDisplayName;
    private final String newBio;
    private final Image newProfilePicture;
    private final String newPreferences;

    public EditProfileOutputData(String newDisplayName, String newBio, Image newProfilePicture, String newPreferences) {
        this.newDisplayName = newDisplayName;
        this.newBio = newBio;
        this.newProfilePicture = newProfilePicture;
        this.newPreferences = newPreferences;
    }

    public String getNewDisplayName() {
        return newDisplayName;
    }

    public String getNewBio() {
        return newBio;
    }

    public Image getNewProfilePicture() {
        return newProfilePicture;
    }

    public String getNewPreferences() {
        return newPreferences;
    }
}
