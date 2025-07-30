package use_case.profile;

public class SwitchToEditProfileViewInputData {
    private final String username;
    public SwitchToEditProfileViewInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
