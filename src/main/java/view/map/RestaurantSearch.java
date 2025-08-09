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

public class RestaurantSearch {
    private final GooglePlacesAPI googlePlacesAPI;

    public RestaurantSearch(Restaurant restaurant) {
        // GooglePlacesAPI googlePlacesAPI = new GooglePlacesAPI(System.getenv("PLACES_API_KEY"));
        AppProperties appProps = new AppProperties();
        this.googlePlacesAPI = new GooglePlacesAPI(appProps.getProperties().getProperty("PLACES_API_KEY"));

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
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


// ========================================================
    public RestaurantSearch() throws IOException {
        AppProperties appProps = new AppProperties();
        this.googlePlacesAPI = new GooglePlacesAPI(appProps.getProperties().getProperty("PLACES_API_KEY"));
    }

    public List<Restaurant> search(Restaurant seed) {
        AppProperties appProps = new AppProperties();
        GooglePlacesAPI googlePlacesAPI = new GooglePlacesAPI(appProps.getProperties().getProperty("PLACES_API_KEY"));
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            String cuisineStr = "";
            if (seed.getCuisines() != null && !seed.getCuisines().isEmpty()) {
                cuisineStr = String.join(" ", seed.getCuisines()); // better query text than ArrayList.toString()
            }
            String query = (cuisineStr.isBlank() ? "food" : cuisineStr + " food")
                    + " near " + (seed.getLocation() == null ? "" : seed.getLocation());

            List<HashMap<String, Object>> results = googlePlacesAPI.searchText(query, null);
            for (HashMap<String, Object> place : results) {
                Restaurant r = RestaurantMapper.fromPlace(place);
                restaurants.add(r);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return restaurants;
    }
// ==========================================================


    public static void main(String[] args) {
        RestaurantSearch mapView = new RestaurantSearch(new Restaurant(new ArrayList<String>(Arrays
                .asList("Indian", "Spicy Foods")), "Toronto"));
    }
}
