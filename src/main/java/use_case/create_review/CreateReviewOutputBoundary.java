package use_case.create_review;

public interface CreateReviewOutputBoundary {
    void prepareSuccessView(CreateReviewOutputData outputData);
    void prepareFailView(CreateReviewOutputData outputData);
}
