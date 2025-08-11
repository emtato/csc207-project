package use_case.view_profile;

/**
 * The Input Data for the Profile Use Case.
 */
public class ProfileInputData {
    private final String username;
    private final String targetUsername;

    public ProfileInputData(String username, String targetUsername) {
        this.username = username;
        this.targetUsername = targetUsername;
    }

    public String getUsername() {
        return username;
    }

    public String getTargetUsername() {
        return targetUsername;
    }
}
