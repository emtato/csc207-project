package use_case.fetch_review;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Account;
import entity.Review;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FetchReviewInteractorTest {

    @Test
    void successTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // write dummy review
        dao.writeReview(
                203332,
                new Account("hi", "hi"),
                "review title",
                "review body",
                new ArrayList<>(Arrays.asList("tag1")),
                "2025-08-07 02:12 a.m."
        );

        FetchReviewOutputBoundary successPresenter = new FetchReviewOutputBoundary() {
            @Override
            public void prepareSuccessView(FetchReviewOutputData data) {
                assertEquals(1, data.getReviews().size());
                Review r = data.getReviews().get(0);
                assertEquals("hi", r.getUser().getUsername());
                assertEquals("review title", r.getTitle());
                assertEquals("review body", r.getDescription());
                assertEquals(new ArrayList<>(Arrays.asList("tag1")), r.getTags());
                assertEquals("2025-08-07 02:12 AM", r.getDateTimeToString());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        FetchReviewInputBoundary interactor = new FetchReviewInteractor(dao, successPresenter);
        interactor.getRandomFeedReviews(new FetchReviewInputData(1));

        // cleanup
        try { dao.deleteReview(203332); } catch (Exception ignored) {}
    }

    @Test
    void failTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // ensure no reviews exist
        try { dao.deleteReview(203332); } catch (Exception ignored) {}

        FetchReviewOutputBoundary failPresenter = new FetchReviewOutputBoundary() {
            @Override
            public void prepareSuccessView(FetchReviewOutputData data) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No reviews found", error);
            }
        };

        FetchReviewInputBoundary interactor = new FetchReviewInteractor(dao, failPresenter);
        interactor.getRandomFeedReviews(new FetchReviewInputData(1));
    }
}
