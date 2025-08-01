package interface_adapter.settings;

import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInputData;

/**
 * Controller for the Change Settings Use Case.
 */
public class SettingsController {
    private final SettingsInputBoundary settingsUseCaseInteractor;

    public SettingsController(SettingsInputBoundary settingsUseCaseInteractor) {
        this.settingsUseCaseInteractor = settingsUseCaseInteractor;
    }

    /**
     * Executes the Change Privacy Use Case.
     */
    public void executePrivacyToggle(String username, boolean privacy) {
        final SettingsInputData settingsInputData = new SettingsInputData(username, privacy);
        settingsUseCaseInteractor.executePrivacyToggle(settingsInputData);
    }

}
