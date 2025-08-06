package interface_adapter.manage_followers;

import entity.User;

import java.util.ArrayList;

/**
 * The state for the Manage Followers View Model.
 */
public class ManageFollowersState {
    private String username;
    private ArrayList<User> followers;
    private ArrayList<User> requesters;

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

    public ArrayList<User> getRequesters() {
        return requesters;
    }

    public void setRequesters(ArrayList<User> requesters) {
        this.requesters = requesters;
    }
}
