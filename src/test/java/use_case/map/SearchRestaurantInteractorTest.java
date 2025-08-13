package use_case.map;

import data_access.places.RestaurantSearchDataAccessInterface;
import entity.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SearchRestaurantInteractorTest {

    // Fake DAO that returns one restaurant for testing
    static class FakeRestaurantSearchDAO implements RestaurantSearchDataAccessInterface {
        @Override
        public ArrayList<Restaurant> search(Restaurant seed) {
            ArrayList<Restaurant> results = new ArrayList<>();
            results.add(new Restaurant(new ArrayList<>(Arrays.asList("pizza")), "Toronto"));
            return results;
        }

        @Override
        public ArrayList<Restaurant> searchText(String query, Map<String, Object> params) {
            return new ArrayList<>();
        }
    }

    // Simple fake presenter to capture output
    static class FakePresenter implements SearchRestaurantOutputBoundary {
        ArrayList<Restaurant> lastResults = null;
        String lastError = null;

        @Override
        public void present(SearchRestaurantOutputData data) {
            lastResults = data.getRestaurants();
        }

        @Override
        public void presentError(String message) {
            lastError = message;
        }
    }

    @Test
    public void interactorSuccessTest() {
        RestaurantSearchDataAccessInterface dao = new FakeRestaurantSearchDAO();
        FakePresenter presenter = new FakePresenter();
        SearchRestaurantInteractor interactor = new SearchRestaurantInteractor(dao, presenter);

        Restaurant seed = new Restaurant(new ArrayList<>(Arrays.asList("pizza")), "Toronto");
        interactor.execute(new SearchRestaurantInputData(seed));

        assertNotNull(presenter.lastResults);
        assertEquals(1, presenter.lastResults.size());
        assertEquals("Toronto", presenter.lastResults.get(0).getLocation());
        assertNull(presenter.lastError);
    }

    @Test
    public void interactorFailureTest() {
        // DAO that returns no restaurants
        RestaurantSearchDataAccessInterface emptyDao = new RestaurantSearchDataAccessInterface() {
            @Override
            public ArrayList<Restaurant> search(Restaurant seed) {
                return new ArrayList<>();
            }
            @Override
            public ArrayList<Restaurant> searchText(String query, Map<String, Object> params) {
                return new ArrayList<>();
            }
        };

        FakePresenter presenter = new FakePresenter();
        SearchRestaurantInteractor interactor = new SearchRestaurantInteractor(emptyDao, presenter);

        Restaurant seed = new Restaurant(new ArrayList<>(Arrays.asList("sushi")), "Nowhere");
        interactor.execute(new SearchRestaurantInputData(seed));

        assertNull(presenter.lastResults);
        assertEquals("No restaurants found.", presenter.lastError);
    }
}
