package interface_adapter.map;

import entity.Review;
import java.util.ArrayList;

public class MapState {
    private String name;
    private String address;
    private String phone;
    private String location;
    private ArrayList<String> cuisines = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private String priceRange;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(ArrayList<String> cuisines) {
        this.cuisines = cuisines;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }
}
