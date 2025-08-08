package interface_adapter.manage_following;

import java.util.ArrayList;

import entity.User;

/**
 * The state for the Manage Following View Model.
 */
public class ManageFollowingState {
    private String username = "";
    private String otherUsername = "";
    private ArrayList<User> requested = new ArrayList<>();
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

    public ArrayList<User> getRequested() {
        return requested;
    }

    public void setRequested(ArrayList<User> requested) {
        this.requested = requested;
    }
}
