package interface_adapter.manage_followers;

import entity.User;

import java.util.ArrayList;

/**
 * The state for the Manage Followers View Model.
 */
public class ManageFollowersState {
    private String username;
    private ArrayList<User> followers;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }
}
