package use_case.fetch_review;

public interface FetchReviewOutputBoundary {
    void prepareSuccessView(FetchReviewOutputData data);
    void prepareFailView(String errorMessage);

}
