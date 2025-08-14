package use_case.map;

public interface SearchRestaurantOutputBoundary {
    void present(SearchRestaurantOutputData data);
    void presentError(String message);
}
