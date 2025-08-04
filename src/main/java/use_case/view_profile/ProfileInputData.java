package use_case.view_profile;

/**
 * The Input Data for the Profile Use Case.
 */
public class ProfileInputData {
    private final String username;
    public ProfileInputData(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
