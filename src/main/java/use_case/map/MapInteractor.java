package use_case.map;

public class MapInteractor implements MapInputBoundary {

private final MapOutputBoundary mapOutputBoundary;

    public MapInteractor(MapOutputBoundary mapOutputBoundary) {
        this.mapOutputBoundary = mapOutputBoundary;
    }

    // @Override
    public void execute() {
        final MapOutputData mapOutputData = new MapOutputData();
    }

    @Override
    public void switchToHomePageView() { mapOutputBoundary.switchToHomePageView();
    }

    @Override
    public void switchToExploreView() {
        mapOutputBoundary.switchToExploreView();
    }

    @Override
    public void switchToClubView() {
        mapOutputBoundary.switchToClubView();
    }

    @Override
    public void switchToSettingsView() {
        mapOutputBoundary.switchToSettingsView();
    }
}
