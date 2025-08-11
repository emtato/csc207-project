package data_access.places;

import entity.Restaurant;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMapper {
    /**
     * Convert a Google Places-like result map into a Restaurant entity,
     * including parsing website URI and price level.
     */
    public static Restaurant fromPlace(HashMap<String, Object> place) {
        String name = (String) place.getOrDefault("name", "");
        String address = (String) place.getOrDefault("formattedAddress", "");
        String phone = (String) place.getOrDefault("internationalPhoneNumber", "");
        String location = place.getOrDefault("location", "").toString();

        // Parse website URI if provided and valid
        URI uri = null;
        Object websiteObj = place.get("websiteUri");
        if (websiteObj != null) {
            String websiteStr = websiteObj.toString().trim();
            if (!websiteStr.isEmpty()) {
                try {
                    uri = new URI(websiteStr);
                } catch (URISyntaxException e) {
                    // try to be forgiving: if it's missing scheme, prepend http://
                    try {
                        uri = new URI("http://" + websiteStr);
                    } catch (URISyntaxException ex) {
                        // give up and leave uri null
                    }
                }
            }
        }

        // cuisines
        ArrayList<String> cuisines = new ArrayList<>();
        Object primary = place.get("primaryType");
        if (primary != null) cuisines.add(primary.toString());

        // price level -> human readable priceRange
        int priceLevel = -1;
        Object plObj = place.get("priceLevel");
        if (plObj instanceof Number) {
            priceLevel = ((Number) plObj).intValue();
        } else if (plObj != null) {
            try {
                priceLevel = Integer.parseInt(plObj.toString());
            } catch (NumberFormatException ignored) {}
        }

        String priceRange;
        switch (priceLevel) {
            case 0 -> priceRange = "$";
            case 1 -> priceRange = "$$";
            case 2 -> priceRange = "$$$";
            case 3 -> priceRange = "$$$$";
            default -> priceRange = "?";
        }

        // Build Restaurant (assumes Restaurant has setters used below)
        Restaurant r = new Restaurant(cuisines, location);
        r.setName(name);
        r.setAddress(address);
        r.setPhone(phone);
        r.setPriceRange(priceRange);
        r.setURI(uri);

        return r;
    }
}
