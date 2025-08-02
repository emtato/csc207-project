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
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setPublic(response.isPublic());
        settingsViewModel.setState(settingsState);
        settingsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final SettingsState settingsState = settingsViewModel.getState();
    }

}
