package use_case.manage_following;

import entity.User;

import java.util.ArrayList;

/**
 * Output Data for the Manage Following Use Case.
 */
public class ManageFollowingOutputData {
    private final ArrayList<User> following;
    public ManageFollowingOutputData(ArrayList<User> following) {
        this.following = following;
    }

    public ArrayList<User> getFollowing() {
        return following;
    }
}
