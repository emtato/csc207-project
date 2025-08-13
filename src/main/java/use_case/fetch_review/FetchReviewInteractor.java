package use_case.fetch_review;

import data_access.PostCommentsLikesDataAccessObject;
import entity.Review;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchReviewInteractor implements FetchReviewInputBoundary {

    private final PostCommentsLikesDataAccessObject postDAO;
    private final FetchReviewOutputBoundary presenter;

    public FetchReviewInteractor(PostCommentsLikesDataAccessObject postDAO, FetchReviewOutputBoundary presenter) {
        this.postDAO = postDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(FetchReviewInputData inputData) {
        getRandomFeedReviews(inputData);
    }

    public void getRandomFeedReviews(FetchReviewInputData inputData) {
        // IMPORTANT: fetch available review IDs, not post IDs
        List<Long> availableIDs = postDAO.getAvailableReviews();
        if (availableIDs == null || availableIDs.isEmpty()) {
            presenter.prepareFailView("No reviews found");
            return;
        }

        Collections.shuffle(availableIDs);
        List<Review> result = new ArrayList<>();
        for (int i = 0; i < Math.min(inputData.getNumberOfReviews(), availableIDs.size()); i++) {
            Review r = postDAO.getReview(availableIDs.get(i));
            if (r != null) {
                result.add(r);
            }
        }

        if (result.isEmpty()) {
            presenter.prepareFailView("No reviews found");
        } else {
            presenter.prepareSuccessView(new FetchReviewOutputData((ArrayList<Review>) result));
        }
    }
    public void getAvailableReviewsID() {
        //
    }
}
