package data_access.places;

import entity.Restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Data access interface for searching restaurants (e.g. via Google Places).
 * Implementations should translate the seed/query into calls to the external
 * provider, map the provider result into {@link Restaurant} entities and return
 * them. Implementations may perform network I/O and therefore declare IO/interrupt
 * exceptions.
 */
public interface RestaurantSearchDataAccessInterface {

    /**
     * Search for restaurants using a "seed" object containing fields such as
     * location and cuisines. The implementation should construct an appropriate
     * provider query from fields on {@code seed}.
     *
     * @param seed a Restaurant object whose fields convey search constraints
     *             (e.g. cuisines, location). Implementations should treat null
     *             or empty fields as wildcards.
     * @return a list of matching Restaurant entities
     * @throws IOException if a network or IO error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    ArrayList<Restaurant> search(Restaurant seed) throws IOException, InterruptedException;

    /**
     * Search using a free-form text query (e.g. "sushi near Toronto").
     * Optional parameters can be supplied in the {@code params} map (radius,
     * language, openNow, etc.) if the underlying provider supports them.
     *
     * @param query  free-text query
     * @param params optional provider-specific parameters
     * @return a list of Restaurant entities
     * @throws IOException if a network or IO error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    ArrayList<Restaurant> searchText(String query, Map<String, Object> params)
            throws IOException, InterruptedException;
}
