package interface_adapter.homepage;

import use_case.homepage.HomePageInputBoundary;

public class HomePageController {
    private final HomePageInputBoundary inputBoundary;

    public HomePageController(HomePageInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void switchToLoginView() {inputBoundary.switchToLogInView();}

    public void switchToSignUpView() {inputBoundary.switchToSignUpView();}

    public void switchToClubView() {inputBoundary.switchToClubView();}
}
