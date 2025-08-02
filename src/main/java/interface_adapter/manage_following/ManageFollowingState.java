package interface_adapter.manage_following;

import entity.User;

import java.util.ArrayList;

/**
 * The state for the Manage Following View Model.
 */
public class ManageFollowingState {
    private String username;
    private ArrayList<User> following;

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
}
