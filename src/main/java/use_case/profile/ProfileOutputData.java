package use_case.profile;

import entity.Post;
import java.util.HashMap;

/**
 * Output Data for the Profile Use Case.
 */
public class ProfileOutputData {
    private final String username;
    private final String displayName;
    private final String bio;
    private final String profilePictureUrl;
    private final int numFollowers;
    private final int numFollowing;
    private final HashMap<Long, Post> posts;


    public ProfileOutputData(String username, String displayName, String bio, String profilePictureUrl,
                             int numFollowers, int numFollowing, HashMap<Long, Post> posts) {
        this.username = username;
        this.displayName = displayName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
        this.posts = posts;
    }

    public String getUsername() {
        return username;
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

    public int getNumFollowers() {
        return numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }

    public HashMap<Long, Post> getPosts() {
        return posts;
    }
}
