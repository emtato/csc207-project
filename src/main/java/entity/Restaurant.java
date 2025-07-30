package entity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Restaurant {
    private String name;
    private String address;
    private  String phone;
    private final String location;
    private final ArrayList<String> cuisines;
    private final ArrayList<entity.Review> reviews;
    private final String priceRange;

    public Restaurant(ArrayList<String> cuisines, String location) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.location = location;
        this.cuisines = cuisines;
        this.reviews = new ArrayList<>();
        this.priceRange = "";
    }
    // TODO: add methods
    public float rating(ArrayList<entity.Review> reviews) {
        int sum = 0;
        for (entity.Review review : reviews) {
            sum += review.getRating();
        }
        return (float) sum / reviews.size();
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
}
