package use_case.edit_profile;

import java.awt.Image;
import java.util.ArrayList;

/**
 * The Input Data for the Edit Profile Use Case.
 */
public class EditProfileInputData {
    private final String username;
    private final String newDisplayName;
    private final String newBio;
    private final String newProfilePictureUrl;
    private final ArrayList<String> newPreferences;

    public EditProfileInputData(String username, String newDisplayName, String newBio, String newProfilePictureUrl,
                                ArrayList<String> newPreferences) {
        this.username = username;
        this.newDisplayName = newDisplayName;
        this.newBio = newBio;
        this.newProfilePictureUrl = newProfilePictureUrl;
        this.newPreferences = newPreferences;
    }

    public String getNewDisplayName() {
        return newDisplayName;
    }

    public String getNewBio() {
        return newBio;
    }

    public String getNewProfilePictureUrl() {
        return newProfilePictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getNewPreferences() {
        return newPreferences;
    }
}
