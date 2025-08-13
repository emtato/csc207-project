package use_case.fetch_review;

import entity.Review;

import java.util.ArrayList;

public class FetchReviewOutputData {
    private final ArrayList<Review> reviews;

    public FetchReviewOutputData(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
