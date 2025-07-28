package view;

import data_access.places.GooglePlacesAPI;
import entity.Restaurant;
import entity.Review;
import interface_adapter.ViewManagerModel;
import interface_adapter.map.MapViewModel;

// TODO: use API and complete view

public class MapView {
    private ViewManagerModel viewManagerModel;
    private GooglePlacesAPI googlePlacesAPI;

    public MapView(MapViewModel viewModel, Restaurant restaurant, Review review) {

    }

    public String getViewName() {
        return "map view";
    }
}
