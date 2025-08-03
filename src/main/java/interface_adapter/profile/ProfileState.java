package interface_adapter.profile;


import entity.Post;

import java.util.HashMap;

/**
 * The state for the Profile View Model.
 */
public class ProfileState {
    private String username = "";
    private String displayName = "";
    private String profilePictureUrl = "https://i.imgur.com/eA9NeJ1.jpeg";
    private String bio = "";
    private int numFollowers = 0;
    private int numFollowing = 0;
    private HashMap<Long, Post> posts = new HashMap<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(int numFollowers) {
        this.numFollowers = numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }

    public void setNumFollowing(int numFollowing) {
        this.numFollowing = numFollowing;
    }

    public HashMap<Long, Post> getPosts() {
        return posts;
    }

    public void setPosts(HashMap<Long, Post> posts) {
        this.posts = posts;
    }
}
