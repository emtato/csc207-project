package use_case.profile;

import java.awt.Image;
import java.util.ArrayList;

/**
 * Output Data for the Switch to Edit Profile View Use Case.
 */
public class SwitchToEditProfileViewOutputData{
    private final String username;
    private final String displayName;
    private final String bio;
    private final Image profilePicture;
    private final ArrayList<String> preferences;

    public SwitchToEditProfileViewOutputData(String username, String displayName, String bio, Image profilePicture,
                                             ArrayList<String> preferences) {
        this.username = username;
        this.displayName = displayName;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.preferences = preferences;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public String getUsername() {
        return username;
    }
}
