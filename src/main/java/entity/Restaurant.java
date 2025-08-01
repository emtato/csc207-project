package entity;

import java.util.ArrayList;

public class Restaurant {
    private final String name;
    private final String address;
    private final String phone;
    private final String location;
    private final ArrayList<String> cuisines;
    private final ArrayList<entity.Review> reviews;
    private final String priceRange;

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

    // TODO: add rating to Recipe
    public float rating(ArrayList<entity.Review> reviews) {
        int sum = 0;
        int countWithStars = 0; // reviews that used the star rating system
        for (entity.Review review : reviews) {
            if(review.getRating() >= 0 && review.getRating() <= 5) {
                sum += review.getRating();
                countWithStars++;
            }
        }
        return (float) sum / countWithStars;
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
