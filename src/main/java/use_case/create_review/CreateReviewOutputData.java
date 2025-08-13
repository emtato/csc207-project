package use_case.create_review;

public class CreateReviewOutputData {
    private final long reviewID;
    private final String time;

    public CreateReviewOutputData(long reviewID, String time) {
        this.reviewID = reviewID;
        this.time = time;
    }

    public long getReviewID() { return reviewID; }
    public String getTime() { return time; }
}
