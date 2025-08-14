package interface_adapter.create_review_view;

import java.util.ArrayList;

public class CreateReviewState {
    private String title = "";
    private String body = "";
    private double rating = -1;
    private ArrayList<String> tags = new ArrayList<>();
    private String error = "";
    private Long reviewId = null;

    public CreateReviewState(CreateReviewState copy) {
        this.title = copy.title;
        this.body = copy.body;
        this.rating = copy.rating;
        this.tags = new ArrayList<>(copy.tags);
        this.error = copy.error;
        this.reviewId = copy.reviewId;
    }

    public CreateReviewState() {}

    public void setTitle(String title) { this.title = title; }
    public void setBody(String body) { this.body = body; }
    public void setRating(double rating) { this.rating = rating; }
    public void setTags(ArrayList<String> tags) { this.tags = tags; }
    public void setError(String error) { this.error = error; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public double getRating() { return rating; }
    public ArrayList<String> getTags() { return tags; }
    public String getError() { return error; }
    public Long getReviewId() { return reviewId; }
}
