package interface_adapter.map;

// import places.*;
import use_case.map.MapInputBoundary;


public class MapController {

    private final MapInputBoundary inputBoundary;

    public MapController(MapInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    // Switch to different pages: home page, explore, club, settings
    public void switchToHomePageView() {inputBoundary.switchToHomePageView();}
    public void switchToExploreView() {inputBoundary.switchToExploreView();}
    public void switchToClubView() {inputBoundary.switchToClubView();}
    public void switchToSettingsView() {inputBoundary.switchToSettingsView();}
    public void switchToProfileView() {inputBoundary.switchToProfileView();}

}
