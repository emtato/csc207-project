package use_case.map;

import data_access.places.RestaurantSearchDataAccessInterface;
import entity.Restaurant;

import java.io.IOException;
import java.util.ArrayList;

public class SearchRestaurantInteractor implements SearchRestaurantInputBoundary {
    private final RestaurantSearchDataAccessInterface restaurantSearchDAO;
    private final SearchRestaurantOutputBoundary presenter;

    public SearchRestaurantInteractor(RestaurantSearchDataAccessInterface restaurantSearchDAO,
                                      SearchRestaurantOutputBoundary presenter) {
        this.restaurantSearchDAO = restaurantSearchDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchRestaurantInputData data) {
        try {
            ArrayList<Restaurant> results = restaurantSearchDAO.search(data.getSeed());
            if (results.isEmpty()) {
                presenter.presentError("No restaurants found.");
            } else {
                presenter.present(new SearchRestaurantOutputData(results));
            }
        } catch (IOException e) {
            presenter.presentError("Failed to search restaurants: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupted status
            presenter.presentError("Restaurant search was interrupted.");
        }
    }
}
