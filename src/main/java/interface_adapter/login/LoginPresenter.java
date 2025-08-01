package interface_adapter.login;

import app.Session;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.LoggedInState;
import interface_adapter.change_password.LoggedInViewModel;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ProfileViewModel profileViewModel;
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel,
                          ProfileViewModel profileViewModel,
                          SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.profileViewModel = profileViewModel;
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, switch to the logged in view.
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.setState(loggedInState);
        this.loggedInViewModel.firePropertyChanged();
        Session.setCurrentUsername(response.getUsername());

        // Update state of other views
        final ProfileState profileState = profileViewModel.getState();
        profileState.setUsername(response.getUsername());
        profileState.setDisplayName(response.getDisplayName());
        profileState.setProfilePictureUrl(response.getProfilePictureUrl());
        profileState.setBio(response.getBio());
        profileState.setNumFollowers(response.getNumFollowers());
        profileState.setNumFollowing(response.getNumFollowing());
        profileState.setPosts(response.getPosts());
        profileViewModel.setState(profileState);
        this.profileViewModel.firePropertyChanged();
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setUsername(response.getUsername());
        settingsState.setPublic(response.isPublic());
        settingsViewModel.setState(settingsState);
        settingsViewModel.firePropertyChanged();

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
