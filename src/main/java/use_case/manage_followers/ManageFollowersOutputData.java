package use_case.manage_followers;

import entity.User;

import java.util.ArrayList;

/**
 * Output Data for the Manage Followers Use Case.
 */
public class ManageFollowersOutputData {
    private final ArrayList<User> followers;
    public ManageFollowersOutputData(ArrayList<User> followers) {
        this.followers = followers;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }
}
