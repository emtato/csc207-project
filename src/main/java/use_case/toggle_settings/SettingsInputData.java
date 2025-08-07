package use_case.toggle_settings;

/**
 * The Input Data for the Settings Use Case.
 */
public class SettingsInputData {
    private final String username;
    private final boolean isOn;

    public SettingsInputData(String username, boolean isOn) {
        this.username = username;
        this.isOn = isOn;
    }

    public boolean isOn() {
        return isOn;
    }

    public String getUsername() {
        return username;
    }
}
