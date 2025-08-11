package interface_adapter.login;

import app.Session;
import interface_adapter.ViewManagerModel;
import interface_adapter.toggle_settings.SettingsState;
import interface_adapter.toggle_settings.SettingsViewModel;
import interface_adapter.view_profile.ProfileState;
import interface_adapter.view_profile.ProfileViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ProfileViewModel profileViewModel;
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel,
                          ProfileViewModel profileViewModel,
                          SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.profileViewModel = profileViewModel;
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        Session.setCurrentUsername(response.getUsername());
        Session.setCurrentAccount();

        // Update state of other views
        final ProfileState profileState = profileViewModel.getState();
        profileState.setUsername(response.getUsername());
        profileState.setCurrentUsername(response.getUsername());
        profileState.setDisplayName(response.getDisplayName());
        profileState.setProfilePictureUrl(response.getProfilePictureUrl());
        profileState.setBio(response.getBio());
        profileState.setNumFollowers(response.getNumFollowers());
        profileState.setNumFollowing(response.getNumFollowing());
        profileState.setPosts(response.getPosts());
        profileViewModel.setState(profileState);
        this.profileViewModel.firePropertyChanged("My Profile Viewed");
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setUsername(response.getUsername());
        settingsState.setNewPassword(response.getPassword());
        settingsState.setNotificationsEnabled(response.isNotificationsEnabled());
        settingsState.setIsPublic(response.isPublic());
        settingsViewModel.setState(settingsState);
        settingsViewModel.firePropertyChanged("privacy changed");
        settingsViewModel.firePropertyChanged("notifications changed");
        settingsViewModel.firePropertyChanged("password changed");

        this.viewManagerModel.setState("homepage view");
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChanged();
    }
}
