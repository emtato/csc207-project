package use_case.manage_following;

/**
 * The Input Data for the Manage Following Use Case.
 */
public class ManageFollowingInputData {
    private final String username;
    private final String unfollowedUsername;
    public ManageFollowingInputData(String username, String unfollowedUsername) {
        this.username = username;
        this.unfollowedUsername = unfollowedUsername;
    }

    public String getUsername() {
        return username;
    }

    public String getUnfollowedUsername() {
        return unfollowedUsername;
    }
}
