package view.map;

import app.AppProperties;
import data_access.places.GooglePlacesAPI;
import data_access.places.RestaurantMapper;
import entity.Restaurant;
import interface_adapter.map.MapViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// TODO: differentiate Google Reviews and app reviews
// TODO: GUI and apply API

public class MapView {
    private final MapViewModel mapViewModel;

    public MapView(MapViewModel viewModel, Restaurant restaurant) {
        this.mapViewModel = viewModel;
        // GooglePlacesAPI googlePlacesAPI = new GooglePlacesAPI(System.getenv("PLACES_API_KEY"));
        AppProperties appProps = new AppProperties();
        GooglePlacesAPI googlePlacesAPI = new GooglePlacesAPI(appProps.getProperties().getProperty("PLACES_API_KEY"));

        try {

            // Use name and location to search
            String query = restaurant.getCuisines() + "food near " + restaurant.getLocation();
            List<HashMap<String, Object>> results = googlePlacesAPI.searchText(query, null);

            if (!results.isEmpty()) {
                List<Restaurant> restaurants = new ArrayList<>();
                for (HashMap<String, Object> result : results) {
                    // Restaurant apiRestaurant = RestaurantMapper.fromPlace(results.get(0));
                    Restaurant apiRestaurant = RestaurantMapper.fromPlace(results.get(results.size() - 1));
                    restaurants.add(apiRestaurant);
                    // FIXME: delete this line:
                    System.out.println(result.toString());
                }
                updateViewModel(restaurants.get(0));

                //               Restaurant apiRestaurant = RestaurantMapper.fromPlace(results.get(0));
 //               updateViewModel(apiRestaurant);
            } else {
                updateViewModel(restaurant); // fallback
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            updateViewModel(restaurant); // fallback in error case
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

    public static void main(String[] args) {
        MapView mapView = new MapView(new MapViewModel(), new Restaurant(new ArrayList<String>(Arrays
                .asList("Indian", "Spicy Foods")), "Toronto"));
    }
}
