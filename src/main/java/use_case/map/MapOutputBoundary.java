package use_case.map;

public interface MapOutputBoundary {

    /**
     * Prepares the Map View.
     */
    void prepareMapView();

    /**
     * Switches to the Home Page View.
     */
    void switchToHomePageView();

    /**
     * Switches to the Explore View.
     */
    void switchToExploreView();

    /**
     * Switches to the Club View.
     */
    void switchToClubView();

    /**
     * Switches to the Settings View.
     */
    void switchToSettingsView();

    /**
     * Switches to the Profile View.
     */
    void switchToProfileView();
}
