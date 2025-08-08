package use_case.manage_followers;

/**
 * The Input Data for the Manage Followers Use Case.
 */
public class ManageFollowersInputData {
    private final String username;
    private final String removedUsername;

    public ManageFollowersInputData(String username, String removedUsername) {
        this.username = username;
        this.removedUsername = removedUsername;
    }

    public String getUsername() {
        return username;
    }

    public String getRemovedUsername() {
        return removedUsername;
    }
}
