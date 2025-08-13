package interface_adapter.fetch_review;

import use_case.fetch_review.FetchReviewInputBoundary;
import use_case.fetch_review.FetchReviewInputData;
import use_case.fetch_review.FetchReviewInteractor;

public class FetchReviewController {

    private final FetchReviewInputBoundary interactor;

    public FetchReviewController(FetchReviewInputBoundary interactor) {
        this.interactor = interactor;
    }

//    public void fetch(long postID) {
//        interactor.execute(new FetchPostInputData(postID));
//    }

    public void getRandomFeedReviews(int count) {
        final FetchReviewInputData data = new FetchReviewInputData(count);
        interactor.getRandomFeedReviews(data); //using one implementation only
    }


    public void getAvailableReviews() {
        ((FetchReviewInteractor) interactor).getAvailableReviewsID();
    }
}
