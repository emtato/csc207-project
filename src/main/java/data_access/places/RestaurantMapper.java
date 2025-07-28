package data_access.places;

import entity.Restaurant;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMapper {

    /**
     * Converts a Places API response map into a Restaurant object.
     *
     * @param place A HashMap containing place details from GooglePlacesAPI
     * @return A Restaurant object
     */
    public static Restaurant fromPlace(HashMap<String, Object> place) {
        String name = (String) place.getOrDefault("name", "");
        String address = (String) place.getOrDefault("formattedAddress", "");
        String phone = (String) place.getOrDefault("internationalPhoneNumber", "");
        String location = (String) place.getOrDefault("location", "");

        // Use primaryType as a single cuisine
        ArrayList<String> cuisines = new ArrayList<>();
        if (place.containsKey("primaryType") && place.get("primaryType") != null) {
            cuisines.add((String) place.get("primaryType"));
        }

        return new Restaurant(name, address, phone, location, cuisines);
    }
}
