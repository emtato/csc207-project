package use_case.profile;

import java.awt.Image;

/**
 * Output Data for the Switch to Edit Profile View Use Case.
 */
public class SwitchToEditProfileViewOutputData{
    private final String displayName;
    private final String bio;
    private final Image profilePicture;
    private final String preferences;

    public SwitchToEditProfileViewOutputData(String displayName, String bio, Image profilePicture, String preferences) {
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

    public String getPreferences() {
        return preferences;
    }
}
