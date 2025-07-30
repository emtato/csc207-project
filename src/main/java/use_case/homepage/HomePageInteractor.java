package use_case.homepage;

public class HomePageInteractor implements HomePageInputBoundary{

private final HomePageOutputBoundary homePageOutputBoundary;

    public HomePageInteractor(HomePageOutputBoundary homePageOutputBoundary) {
        this.homePageOutputBoundary = homePageOutputBoundary;
    }

//    @Override
//    public void executeViewProfile() {
//        final HomePageOutputData homePageOutputData = new HomePageOutputData();
//    }

    @Override
    public void switchToSignUpView() {
        homePageOutputBoundary.switchToSignUpView();
    }

    @Override
    public void switchToLogInView() {
        homePageOutputBoundary.switchToLogInView();
    }

    @Override
    public void switchToClubView() {
        homePageOutputBoundary.switchToClubView();
    }

    @Override
    public void switchToSettingsView() {
        homePageOutputBoundary.switchToSettingsView();
    }
}
