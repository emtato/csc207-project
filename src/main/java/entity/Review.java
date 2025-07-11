package entity;

import java.time.LocalDateTime;

public class Review {
    private Account user;
    private int rating;
    private String reviewText;

    // TODO: somehow implement LocalDateTime date and the type of review (restaurant or recipe)
    
    public Review(Account user, int rating, String reviewText) {
        if (0 <= rating && rating <= 5) {
            this.user = user;
            this.rating = rating;
            this.reviewText = reviewText;
        }
    }

    public Account getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }
}
