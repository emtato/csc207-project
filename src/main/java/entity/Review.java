package entity;

// If the Post is a Review
public class Review extends Post {
    private double rating;
    private boolean isRestaurantReview; // Review of a Restaurant
    private boolean isRecipeReview; // Review of a Recipe

    public Review(Account user, long reviewID, String title, String reviewText) {
        super(user, reviewID, title, reviewText);
        rating = -1;
        this.isRestaurantReview = false;
        this.isRecipeReview = false;
    }

    public double getRating() {
        return rating;
    }

    // ratings are using the 5-star system
    // 0<=rating<=5
    public void setRating(double rating) {
        if (rating >= 0 && rating <= 5) {
            this.rating = rating;
        }
    }

    public boolean isRestaurantReview() { return isRestaurantReview; }

    public void setRestaurantReview(boolean restaurantReview) { isRestaurantReview = restaurantReview; }

    public boolean isRecipeReview() {
        return isRecipeReview;
    }

    public void setRecipeReview(boolean recipeReview) {
        isRecipeReview = recipeReview;
    }

}
