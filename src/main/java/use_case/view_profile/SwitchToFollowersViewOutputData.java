package use_case.view_profile;

import java.util.ArrayList;

import entity.User;

public class SwitchToFollowersViewOutputData {
    private final String username;
    private final ArrayList<User> followers;
    private final ArrayList<User> requesters;

    public SwitchToFollowersViewOutputData(String username, ArrayList<User> followers, ArrayList<User> requesters) {
        this.username = username;
        this.followers = followers;
        this.requesters = requesters;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public ArrayList<User> getRequesters() {
        return requesters;
    }
}
