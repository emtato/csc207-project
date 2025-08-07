package interface_adapter.view_profile;

import java.util.HashMap;
import java.util.Map;

import entity.Post;

/**
 * The state for the Profile View Model.
 */
public class ProfileState {
    private String username = "";
    private String displayName = "";
    private String profilePictureUrl = "https://i.imgur.com/eA9NeJ1.jpeg";
    private String bio = "";
    private int numFollowers;
    private int numFollowing;
    private Map<Long, Post> posts = new HashMap<>();

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

    public Map<Long, Post> getPosts() {
        return posts;
    }

    /**
     * Sets the user's posts to the given map of posts, or an empty map if null.
     * @param posts the posts to set
     */
    public void setPosts(Map<Long, Post> posts) {
        if (posts == null) {
            this.posts = new HashMap<>();
        }
        else {
            // Remove any null posts from the HashMap
            posts.values().removeIf(post -> post == null);
            this.posts = posts;
        }
    }
}
