package use_case.settings;

/**
 * Input Boundary for actions which are related to changing the settings.
 */
public interface SettingsInputBoundary {
    /**
     * Executes the change settings use case.
     * @param settingsInputData the input data
     */
    void execute(SettingsInputData settingsInputData);

    // TODO: switching views for the menu bar
}
