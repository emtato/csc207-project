package use_case.homepage;

public interface HomePageOutputBoundary {

    /**
     * Prepares the Home Page View.
     */
    void prepareHomePageView();

    /**
     * Switches to the Signup View.
     */
    void switchToSignUpView();

    /**
     * Switches to the Login In View.
     */
    void switchToLogInView();

    /**
     * Switches to the Club View.
     */
    void switchToClubView();
}
