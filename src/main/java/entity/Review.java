package entity;

import java.util.ArrayList;

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
        this.setReview(true);
        this.setType("review");
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

    // Restaurant or Recipe
    // TODO: use method
    public float averageRating(ArrayList<Review> reviews) {
        double sum = 0;
        int countWithStars = 0; // reviews that used the star rating system
        for (entity.Review review : reviews) {
            if(review.getRating() >= 0 && review.getRating() <= 5) {
                sum += review.getRating();
                countWithStars++;
            }
        }
        if (countWithStars > 0) {
            return (float) sum / countWithStars;
        }
        return 0;
    }

    public boolean isRestaurantReview() { return isRestaurantReview; }

    public void setRestaurantReview(boolean restaurantReview) { isRestaurantReview = restaurantReview; }

    public boolean isRecipeReview() {
        return isRecipeReview;
    }

    public void setRecipeReview(boolean recipeReview) {
        isRecipeReview = recipeReview;
    }

    @Override
    public String toString() {
        return super.toString() + ", Rating=" + rating + ", Type=" + (isRestaurantReview ? "Restaurant" : isRecipeReview ? "Recipe" : "Generic");
    }

}
