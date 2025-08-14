package interface_adapter.fetch_review;

import interface_adapter.homepage.HomePageState;
import interface_adapter.homepage.HomePageViewModel;
import use_case.fetch_review.FetchReviewOutputBoundary;
import use_case.fetch_review.FetchReviewOutputData;

public class FetchReviewPresenter implements FetchReviewOutputBoundary {
    private final HomePageViewModel homePageViewModel;

    public FetchReviewPresenter(HomePageViewModel homePageViewModel) {
        this.homePageViewModel = homePageViewModel;
    }

    public void prepareSuccessView(FetchReviewOutputData data) {
        final HomePageState state = this.homePageViewModel.getState();
        state.setReviews(data.getReviews());
    }

    public void prepareFailView(String errorMessage) {
        System.err.println("Failed to fetch review: " + errorMessage);
    }
}
