package use_case.view_profile;

import java.util.ArrayList;

/**
 * Output Data for the Switch to Edit Profile View Use Case.
 */
public class SwitchToEditProfileViewOutputData {
    private final String username;
    private final String displayName;
    private final String bio;
    private final String profilePictureUrl;
    private final ArrayList<String> preferences;

    public SwitchToEditProfileViewOutputData(String username, String displayName, String bio, String profilePictureUrl,
                                             ArrayList<String> preferences) {
        this.username = username;
        this.displayName = displayName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.preferences = preferences;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public String getUsername() {
        return username;
    }
}
