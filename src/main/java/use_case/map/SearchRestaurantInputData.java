package use_case.map;

import entity.Restaurant;

public class SearchRestaurantInputData {
    private final Restaurant seed;

    public SearchRestaurantInputData(Restaurant seed) {
        this.seed = seed;
    }

    public Restaurant getSeed() {
        return seed;
    }
}
