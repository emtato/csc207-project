package interface_adapter.toggle_settings;

import use_case.toggle_settings.SettingsInputBoundary;
import use_case.toggle_settings.SettingsInputData;

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

    /**
     * Executes the Change Notifications status Use Case.
     */
    public void executeNotificationsToggle(String username, boolean notificationsEnabled) {
        final SettingsInputData settingsInputData = new SettingsInputData(username, notificationsEnabled);
        settingsUseCaseInteractor.executeNotificationsToggle(settingsInputData);
    }


}
