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
     * Executes the Change Settings Use Case.
     */
    public void execute() {
        final SettingsInputData settingsInputData = new SettingsInputData();
        settingsUseCaseInteractor.execute(settingsInputData);
    }

}
