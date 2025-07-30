package interface_adapter.edit_profile;

import java.awt.Image;

/**
 * The state for the Edit Profile View Model.
 */
public class EditProfileState {
    private String username;
    private String displayName;
    private String bio;
    private Image profilePicture;
    private String preferences;
    private String newDisplayName;
    private String newBio;
    private Image newProfilePicture;
    private String newPreferences;

    public String getNewDisplayName() {
        return newDisplayName;
    }

    public void setNewDisplayName(String newDisplayName) {
        this.newDisplayName = newDisplayName;
    }

    public String getNewBio() {
        return newBio;
    }

    public void setNewBio(String newBio) {
        this.newBio = newBio;
    }

    public Image getNewProfilePicture() {
        return newProfilePicture;
    }

    public void setNewProfilePicture(Image newProfilePicture) {
        this.newProfilePicture = newProfilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPreferences() {
        return newPreferences;
    }

    public void setNewPreferences(String newPreferences) {
        this.newPreferences = newPreferences;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
