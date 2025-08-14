package use_case.map;

import entity.Restaurant;
import java.util.ArrayList;

public class SearchRestaurantOutputData {
    private final ArrayList<Restaurant> restaurants;

    public SearchRestaurantOutputData(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }
}
