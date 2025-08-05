package interface_adapter.toggle_settings;

import interface_adapter.ViewManagerModel;
import use_case.toggle_settings.SettingsOutputBoundary;
import use_case.toggle_settings.SettingsOutputData;

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
    public void preparePrivacySuccessView(SettingsOutputData response) {
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setIsPublic(response.isOn());
        settingsViewModel.setState(settingsState);
        settingsViewModel.firePropertyChanged("privacy changed");
    }

    @Override
    public void prepareNotificationsSuccessView(SettingsOutputData response) {
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setNotificationsEnabled(response.isOn());
        settingsViewModel.setState(settingsState);
        settingsViewModel.firePropertyChanged("notifications changed");
    }

    @Override
    public void prepareFailView(String error) {
        final SettingsState settingsState = settingsViewModel.getState();
    }

}
