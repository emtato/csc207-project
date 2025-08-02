package use_case.settings;

/**
 * Input Boundary for actions which are related to changing the settings.
 */
public interface SettingsInputBoundary {
    /**
     * Executes the change privacy use case.
     * @param settingsInputData the input data
     */
    void executePrivacyToggle(SettingsInputData settingsInputData);
}
