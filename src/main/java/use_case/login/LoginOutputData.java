package use_case.login;

import entity.Post;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final String displayName;
    private final String profilePictureUrl;
    private final String bio;
    private final int numFollowers;
    private final int numFollowing;
    private final HashMap<Long, Post> posts;
    private final boolean isPublic;
    private final boolean useCaseFailed;

    public LoginOutputData(String username, String displayName, String profilePictureUrl, String bio, int numFollowers,
                           int numFollowing, HashMap<Long, Post> posts, boolean isPublic, boolean useCaseFailed) {
        this.username = username;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
        this.posts = posts;
        this.isPublic = isPublic;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }

    public HashMap<Long, Post>getPosts() {
        return posts;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    public boolean isPublic() {
        return isPublic;
    }
}