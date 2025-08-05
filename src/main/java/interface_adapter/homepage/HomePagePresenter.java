package interface_adapter.homepage;

import interface_adapter.ViewManagerModel;
import interface_adapter.clubs.ClubViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.toggle_settings.SettingsViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.homepage.HomePageOutputBoundary;

public class HomePagePresenter implements HomePageOutputBoundary {

    private final HomePageViewModel homePageViewModel;
    private final SignupViewModel signupViewModel;
    private final LoginViewModel loginViewModel;
    private final ClubViewModel clubViewModel;
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    public HomePagePresenter(ViewManagerModel viewManagerModel,
                             HomePageViewModel homePageViewModel,
                             SignupViewModel signupViewModel,
                             LoginViewModel loginViewModel,
                             ClubViewModel clubViewModel, SettingsViewModel settingsViewModel) {
        this.homePageViewModel = homePageViewModel;
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
        this.clubViewModel = clubViewModel;
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareHomePageView() {
        viewManagerModel.setState(homePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToSignUpView() {
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToLogInView() {
        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToClubView() {
        viewManagerModel.setState(clubViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToSettingsView(){
        viewManagerModel.setState(settingsViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }


}
