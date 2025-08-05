package interface_adapter.map;

import interface_adapter.ViewManagerModel;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.explore.ExploreViewModel;
import interface_adapter.clubs.ClubViewModel;
import interface_adapter.view_profile.ProfileViewModel;
import interface_adapter.toggle_settings.SettingsViewModel;
import use_case.map.MapOutputBoundary;

public class MapPresenter implements MapOutputBoundary {

    private final MapViewModel mapViewModel;
    private final HomePageViewModel homePageViewModel;
    private final ExploreViewModel exploreViewModel;
    private final ClubViewModel clubViewModel;
    private final SettingsViewModel settingsViewModel;
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    public MapPresenter(ViewManagerModel viewManagerModel,
                        MapViewModel mapViewModel,
                        HomePageViewModel homePageViewModel,
                        ClubViewModel clubViewModel,
                        ExploreViewModel exploreViewModel,
                        SettingsViewModel settingsViewModel,
                        ProfileViewModel profileViewModel) {
        this.mapViewModel = mapViewModel;
        this.homePageViewModel = homePageViewModel;
        this.exploreViewModel = exploreViewModel;
        this.clubViewModel = clubViewModel;
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void prepareMapView() {
        viewManagerModel.setState(mapViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToHomePageView() {
        viewManagerModel.setState(homePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToExploreView() {
        viewManagerModel.setState(exploreViewModel.getViewName());
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

    @Override
    public void switchToProfileView(){
        viewManagerModel.setState(profileViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
