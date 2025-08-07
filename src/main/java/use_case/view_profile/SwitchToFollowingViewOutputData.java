package use_case.view_profile;

import java.util.ArrayList;

import entity.User;

public class SwitchToFollowingViewOutputData {
    private final String username;
    private final ArrayList<User> following;
    private final ArrayList<User> requested;

    public SwitchToFollowingViewOutputData(String username, ArrayList<User> following, ArrayList<User> requested) {
        this.username = username;
        this.following = following;
        this.requested = requested;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<User> getFollowing() {
        return following;
    }

    public ArrayList<User> getRequested() {
        return requested;
    }
}
