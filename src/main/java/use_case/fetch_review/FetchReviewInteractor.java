package use_case.fetch_review;

import data_access.PostCommentsLikesDataAccessObject;
import entity.Post;
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
        // not used for now
    }

    /**
     * Fetch random reviews by:
     *  - getting all available post IDs
     *  - fetching each post with getPost(id)
     *  - keeping only posts that are reviews (type == "review" or instanceof Review)
     */
    public void getRandomFeedReviews(FetchReviewInputData inputData) {
        ArrayList<Long> availableIDs = postDAO.getAvailablePosts();
        if (availableIDs == null || availableIDs.isEmpty()) {
            presenter.prepareFailView("No reviews found");
            return;
        }

        Collections.shuffle(availableIDs);
        List<Review> result = new ArrayList<>();

        for (Long id : availableIDs) {
            Post p = postDAO.getPost(id);
            if (p == null) continue;
            // accept if it's typed as review
            if (p instanceof Review || "review".equalsIgnoreCase(p.getType())) {
                // safe cast if instance of Review, else try to coerce
                if (p instanceof Review) {
                    result.add((Review) p);
                } else {
                    // create Review wrapper if needed (rare, depends on DAO)
                    Review r = new Review(p.getUser(), p.getID(), p.getTitle(), p.getDescription());
                    r.setTags(p.getTags());
                    r.setDateTimeFromString(p.getDateTimeToString());
                    result.add(r);
                }
            }
            if (result.size() >= inputData.getNumberOfReviews()) break;
        }

        if (result.isEmpty()) {
            presenter.prepareFailView("No reviews found");
        } else {
            FetchReviewOutputData data = new FetchReviewOutputData(new ArrayList<>(result));
            presenter.prepareSuccessView(data);
        }
    }

    public void getAvailableReviewsID() {
        ArrayList<Long> availableIDs = postDAO.getAvailablePosts();
        if (availableIDs == null) {
            presenter.prepareFailView("No reviews found");
            return;
        }
        ArrayList<Long> matches = new ArrayList<>();
        for (Long id : availableIDs) {
            Post p = postDAO.getPost(id);
            if (p == null) continue;
            if (p instanceof Review || "review".equalsIgnoreCase(p.getType())) matches.add(id);
        }
    }
}
