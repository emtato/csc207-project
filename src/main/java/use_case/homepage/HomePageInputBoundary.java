package use_case.homepage;

public interface HomePageInputBoundary {

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

    /**
     * Switches to the Settings View.
     */
    void switchToSettingsView();
}
