package view;

import data_access.places.GooglePlacesAPI;
import data_access.places.RestaurantMapper;
import entity.Restaurant;
import entity.Review;
import interface_adapter.ViewManagerModel;
import interface_adapter.map.MapViewModel;
import interface_adapter.profile.ProfileViewModel;

import javax.swing.*;
import javax.swing.JLabel;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO: add buttons and other components to frame, test if works

public class MapView {
    final JLabel title;

    private final MapViewModel mapViewModel;

    public MapView(MapViewModel viewModel, Restaurant restaurant) {
        this.mapViewModel = viewModel;
        this.mapViewModel.addPropertyChangeListener((PropertyChangeListener) this);
        GooglePlacesAPI googlePlacesAPI = new GooglePlacesAPI(System.getenv("PLACES_API_KEY"));

        title = new JLabel("Map");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMinimumSize(new Dimension(1000, 50));

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setMaximumSize(new Dimension(1200, 300));
        mainPanel.setMinimumSize(new Dimension(1200, 300));

        try {
            // Use name and location to search
            String query = restaurant.getName() + " near " + restaurant.getLocation();
            List<HashMap<String, Object>> results = googlePlacesAPI.searchText(query, null);

            if (!results.isEmpty()) {
                Restaurant apiRestaurant = RestaurantMapper.fromPlace(results.get(0));
                updateViewModel(apiRestaurant);
            } else {
                updateViewModel(restaurant); // fallback
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            updateViewModel(restaurant); // fallback in error case

            //Buttons

            final JPanel generalButtons = new JPanel();
            generalButtons.setAlignmentY(Component.CENTER_ALIGNMENT);

            JButton homeButton = new JButton("Home");
            generalButtons.add(homeButton);

            JButton exploreButton = new JButton("Explore");
            generalButtons.add(exploreButton);

            JButton clubButton = new JButton("Club");
            generalButtons.add(clubButton);

            JButton settingsButton = new JButton("Settings");
            generalButtons.add(settingsButton);

            JButton profileButton = new JButton("Profile");
            generalButtons.add(profileButton);
        }
    }

    private void updateViewModel(Restaurant restaurant) {
        mapViewModel.setName(restaurant.getName());
        mapViewModel.setAddress(restaurant.getAddress());
        mapViewModel.setPhone(restaurant.getPhone());
        mapViewModel.setLocation(restaurant.getLocation());
        mapViewModel.setCuisines(restaurant.getCuisines());
        mapViewModel.setReviews(restaurant.getReviews());
        mapViewModel.setPriceRange(restaurant.getPriceRange());
    }

    public String getViewName() {
        return "map view";
    }

}
