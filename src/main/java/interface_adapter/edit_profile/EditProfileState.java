package interface_adapter.edit_profile;

import javax.swing.*;
import java.awt.Image;
import java.util.ArrayList;

/**
 * The state for the Edit Profile View Model.
 */
public class EditProfileState {
    private String username = "";
    private String displayName = "";
    private String bio = "";
    private String profilePictureUrl = "https://i.imgur.com/eA9NeJ1.jpeg";
    private ArrayList<String> preferences = new ArrayList<>();
    private String newDisplayName;
    private String newBio;
    private String newProfilePictureUrl;
    private ArrayList<String> newPreferences;

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

    public String getNewProfilePictureUrl() {
        return newProfilePictureUrl;
    }

    public void setNewProfilePictureUrl(String newProfilePictureUrl) {
        this.newProfilePictureUrl = newProfilePictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getNewPreferences() {
        return newPreferences;
    }

    public void setNewPreferences(ArrayList<String> newPreferences) {
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(ArrayList<String> preferences) {
        this.preferences = preferences;
    }
}
