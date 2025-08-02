package use_case.settings;

/**
 * The Input Data for the Settings Use Case.
 */
public class SettingsInputData {
    private final String username;
    private final boolean privacy;
    public SettingsInputData(String username, boolean privacy) {
        this.username = username;
        this.privacy = privacy;
    }
    public boolean isPublic() {
        return privacy;
    }

    public String getUsername() {
        return username;
    }
}
