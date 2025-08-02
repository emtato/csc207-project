package use_case.manage_followers;

/**
 * The Input Data for the Manage Followers Use Case.
 */
public class ManageFollowersInputData {
    private final String username;
    private final String removedFollower;
    public ManageFollowersInputData(String username, String removedFollower) {
        this.username = username;
        this.removedFollower = removedFollower;
    }

    public String getUsername() {
        return username;
    }

    public String getRemovedFollower() {
        return removedFollower;
    }
}
