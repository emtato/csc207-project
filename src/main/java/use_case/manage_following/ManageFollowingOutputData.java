package use_case.manage_following;

import java.util.ArrayList;

import entity.User;

/**
 * Output Data for the Manage Following Use Case.
 */
public class ManageFollowingOutputData {
    private final ArrayList<User> following;
    private final ArrayList<User> requested;
    private final String actionPerformed;

    public ManageFollowingOutputData(ArrayList<User> following, ArrayList<User> requested, String actionPerformed) {
        this.following = following;
        this.requested = requested;
        this.actionPerformed = actionPerformed;
    }

    public ArrayList<User> getFollowing() {
        return following;
    }

    public ArrayList<User> getRequested() {
        return requested;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }
}
