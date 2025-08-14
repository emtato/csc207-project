package interface_adapter.create_review_view;

import use_case.create_review.CreateReviewInputBoundary;
import use_case.create_review.CreateReviewInputData;

public class CreateReviewController {
    private final CreateReviewInputBoundary interactor;

    public CreateReviewController(CreateReviewInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(CreateReviewInputData inputData) {
        interactor.execute(inputData);
    }
}
