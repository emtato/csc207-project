package use_case.toggle_settings;

/**
 * Output Data for the Settings Use Case.
 */
public class SettingsOutputData {
    private final boolean isOn;
    public SettingsOutputData(boolean isOn) {
        this.isOn = isOn;
    }
    public boolean isOn() {
        return isOn;
    }
}
