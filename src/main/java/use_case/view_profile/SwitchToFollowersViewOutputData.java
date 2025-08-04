package use_case.view_profile;

import entity.User;

import java.util.ArrayList;

public class SwitchToFollowersViewOutputData {
    private final String username;
    private final ArrayList<User> followers;
    public SwitchToFollowersViewOutputData(String username, ArrayList<User> followers) {
        this.username = username;
        this.followers = followers;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }
}
