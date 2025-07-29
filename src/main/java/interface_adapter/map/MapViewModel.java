package interface_adapter.map;

import entity.Review;
import interface_adapter.ViewModel;

import java.util.ArrayList;


public class MapViewModel extends ViewModel<MapState> {

    public MapViewModel() {
        super("map view");
        setState(new MapState());
    }

    public void setName(String name) {
        getState().setName(name);
    }

    public void setAddress(String address) {
        getState().setAddress(address);
    }

    public void setPhone(String phone) {
        getState().setPhone(phone);
    }

    public void setLocation(String location) {
        getState().setLocation(location);
    }

    public void setCuisines(ArrayList<String> cuisines) {
        getState().setCuisines(cuisines);
    }

    public void setReviews(ArrayList<Review> reviews) {
        getState().setReviews(reviews);
    }

    public void setPriceRange(String priceRange) {
        getState().setPriceRange(priceRange);
    }
}
