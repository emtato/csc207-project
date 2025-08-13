package use_case.fetch_review;

public interface FetchReviewInputBoundary {
    void execute(FetchReviewInputData inputData);
    void getRandomFeedReviews(FetchReviewInputData inputData);
}
