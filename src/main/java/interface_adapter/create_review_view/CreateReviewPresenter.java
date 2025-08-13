package interface_adapter.create_review_view;

import use_case.create_review.CreateReviewOutputBoundary;
import use_case.create_review.CreateReviewOutputData;

import java.util.ArrayList;

public class CreateReviewPresenter implements CreateReviewOutputBoundary {
    private final CreateReviewViewModel createReviewViewModel;

    public CreateReviewPresenter(CreateReviewViewModel createReviewViewModel) {
        this.createReviewViewModel = createReviewViewModel;
    }

    @Override
    public void prepareSuccessView(CreateReviewOutputData outputData) {
        CreateReviewState state = createReviewViewModel.getState();
        state.setTitle("");
        state.setBody("");
        state.setRating(-1);
        state.setTags(new ArrayList<>());
        state.setReviewId(outputData.getReviewID());
        createReviewViewModel.setState(state);
        createReviewViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(CreateReviewOutputData outputData) {
        CreateReviewState state = createReviewViewModel.getState();
        state.setError("Failed to create review");
        createReviewViewModel.setState(state);
        createReviewViewModel.firePropertyChanged();
    }
}
