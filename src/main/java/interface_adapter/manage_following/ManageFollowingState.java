package interface_adapter.manage_following;

import entity.User;

import java.util.ArrayList;

/**
 * The state for the Manage Following View Model.
 */
public class ManageFollowingState {
    private String username = "";
    private String otherUsername = "";
    private ArrayList<User> following = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<User> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<User> following) {
        this.following = following;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }
}
