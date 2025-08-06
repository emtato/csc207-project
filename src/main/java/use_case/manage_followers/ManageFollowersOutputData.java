package use_case.manage_followers;

import entity.User;

import java.util.ArrayList;

/**
 * Output Data for the Manage Followers Use Case.
 */
public class ManageFollowersOutputData {
    private final ArrayList<User> followers;
    private final ArrayList<User> requesters;
    public ManageFollowersOutputData(ArrayList<User> followers, ArrayList<User> requesters) {
        this.followers = followers;
        this.requesters = requesters;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public ArrayList<User> getRequesters() {
        return requesters;
    }
}
