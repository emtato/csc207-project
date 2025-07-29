package interface_adapter.settings;

import interface_adapter.ViewManagerModel;
import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;

/**
 * The Presenter for the Change Settings Use Case.
 */
public class SettingsPresenter implements SettingsOutputBoundary {
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    public SettingsPresenter(ViewManagerModel viewManagerModel,
                           SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(SettingsOutputData response) {
        // TODO: On success, do somethings.
    }

    @Override
    public void prepareFailView(String error) {
        final SettingsState settingsState = settingsViewModel.getState();
        // TODO: On failure, do something
        //settingsState.setUsernameError(error);
        //settingsViewModel.firePropertyChanged();
    }

}
