package entity;

import data_access.DataStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Review {
    private final Account user;
    private final long reviewID;
    private final String title;
    private int rating;
    private String reviewText;

    private boolean isRestaurantReview; // Review of a Restaurant
    private boolean isRecipeReview; // Review of a Recipe

    private LocalDateTime dateTime;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");

    
    public Review(Account user, long reviewID, String title, String reviewText) {
        this.user = user;
        this.reviewID = reviewID;
        this.title = title;
        this.reviewText = reviewText;
        dateTime = LocalDateTime.now();
        rating = -1;
        this.isRestaurantReview = false;
        this.isRecipeReview = false;
    }

    public Account getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    // ratings are using the 5-star system
    // 0<=rating<=5
    public void setRating(int rating) {
        if(rating >= 0 && rating <= 5){
            this.rating = rating;
        }
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isRestaurantReview() { return isRestaurantReview; }

    public void setRestaurantReview(boolean restaurantReview) { isRestaurantReview = restaurantReview; }

    public boolean isRecipeReview() {
        return isRecipeReview;
    }

    public void setRecipeReview(boolean recipeReview) {
        isRecipeReview = recipeReview;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    /**
     * Returns the post's timestamp formatted as "yyyy-MM-dd  HH:mm:ss".
     *
     * @return Formatted timestamp string
     */
    public String getDateTimeToString() {
        return dateTimeFormatter.format(dateTime);
    }

    /**
     * Sets the post's timestamp using a formatted datetime string.
     *
     * @param dateTime A string representing datetime in "yyyy-MM-dd  HH:mm:ss" format
     */
    public void setDateTimeFromString(String dateTime) {
        this.dateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    /**
     * Returns the post's date formatted as "EEE, dd MMM yyyy".
     *
     * @return Formatted date string
     */
    public String getDateToString() {
        return dateFormatter.format(dateTime);
    }


    public long getReviewID() {
        return reviewID;
    }

    @Override
    public String toString() {
        return "ReviewID=" + reviewID + ", Title=" + title + ", User=" + user.getUsername();
    }

    public ArrayList<Comment> getComments() {
        DataStorage dataStorage = new DataStorage();
        return dataStorage.getComments(reviewID);
    }
}
