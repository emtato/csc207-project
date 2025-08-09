package data_access.places;

import entity.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMapper {
    public static Restaurant fromPlace(HashMap<String, Object> place) {
        String name = (String) place.getOrDefault("name", "");
        String address = (String) place.getOrDefault("formattedAddress", "");
        String phone = (String) place.getOrDefault("internationalPhoneNumber", "");
        String location = place.getOrDefault("location", "").toString();

        ArrayList<String> cuisines = new ArrayList<>();
        Object primary = place.get("primaryType");
        if (primary != null) cuisines.add(primary.toString());

        int priceLevel = -1;
        Object plObj = place.get("priceLevel");
        if (plObj instanceof Number) priceLevel = ((Number) plObj).intValue();
        String priceRange;
        switch (priceLevel) {
            case 0 -> priceRange = "$";
            case 1 -> priceRange = "$$";
            case 2 -> priceRange = "$$$";
            case 3 -> priceRange = "$$$$";
            default -> priceRange = "?";
        }

        Restaurant r = new Restaurant(cuisines, location);
        // set fields — adjust to your Restaurant API (setName/setAddress/etc)
        r.setName(name);
        r.setAddress(address);
        r.setPhone(phone);
        r.setPriceRange(priceRange);
        // optionally set reviews if available
        return r;
    }
}
