package use_case.view_profile;

import entity.User;

import java.util.ArrayList;

public class SwitchToFollowingViewOutputData {
    private final String username;
    private final ArrayList<User> following;
    public SwitchToFollowingViewOutputData(String username, ArrayList<User> following) {
        this.username = username;
        this.following = following;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<User> getFollowing() {
        return following;
    }
}
