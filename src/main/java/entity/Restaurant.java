package entity;

import java.net.URI;
import java.util.ArrayList;

public class Restaurant {
    private String name;
    private String address;
    private String phone;
    private final String location;
    private final ArrayList<String> cuisines;
    private final ArrayList<entity.Review> reviews;
    private String priceRange;
    private URI uri;

    // Restaurants based on cuisines and location
    public Restaurant(ArrayList<String> cuisines, String location) {
        this.name = null;
        this.address = null;
        this.phone = null;
        this.location = location;
        this.cuisines = cuisines;
        this.reviews = new ArrayList<>();
        this.priceRange = null;
        this.uri = null;
    }


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<String> getCuisines() {
        return cuisines;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public URI getURI() {
        return uri;
    }

    public void setName(String name) { this.name = name;
    }
    public void setAddress(String address) { this.address = address;
    }
    public void setPhone(String phone) { this.phone = phone;
    }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange;
    }
    public void setURI(URI uri) { this.uri = uri; }
}
