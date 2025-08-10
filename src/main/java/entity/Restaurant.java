package entity;

import java.util.ArrayList;

public class Restaurant {
    private String name;
    private String address;
    private String phone;
    private final String location;
    private final ArrayList<String> cuisines;
    private final ArrayList<entity.Review> reviews;
    private String priceRange;

    // Restaurants based on cuisines and location
    public Restaurant(ArrayList<String> cuisines, String location) {
        this.name = null;
        this.address = null;
        this.phone = null;
        this.location = location;
        this.cuisines = cuisines;
        this.reviews = new ArrayList<>();
        this.priceRange = null;
    }

//    // TODO: if extra time, different search function (delete if not used)
//    // Restaurants based on name
//    public Restaurant(String name) {
//        this.name = name;
//        this.address = null;
//        this.phone = null;
//        this.location = null;
//        this.cuisines = new ArrayList<>();
//        this.reviews = new ArrayList<>();
//        this.priceRange = null;
//    }


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

    public void setName(String name) { this.name = name;
    }
    public void setAddress(String address) { this.address = address;
    }
    public void setPhone(String phone) { this.phone = phone;
    }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange;
    }
}
