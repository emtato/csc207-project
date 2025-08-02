package use_case.settings;

/**
 * Output Data for the Settings Use Case.
 */
public class SettingsOutputData {
    private final boolean isPublic;
    public SettingsOutputData(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public boolean isPublic() {
        return isPublic;
    }
}
