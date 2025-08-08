package use_case.manage_following;

/**
 * The Input Data for the Manage Following Use Case.
 */
public class ManageFollowingInputData {
    private final String username;
    private final String otherUsername;

    public ManageFollowingInputData(String username, String otherUsername) {
        this.username = username;
        this.otherUsername = otherUsername;
    }

    public String getUsername() {
        return username;
    }

    public String getOtherUsername() {
        return otherUsername;
    }
}
