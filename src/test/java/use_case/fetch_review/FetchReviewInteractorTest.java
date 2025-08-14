package use_case.fetch_review;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Account;
import entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FetchReviewInteractorTest {

    @BeforeEach
    void resetDao() {
        ((InMemoryPostCommentLikesDataAccessObject)
                InMemoryPostCommentLikesDataAccessObject.getInstance())
                .clearAll();
    }

    @Test
    void successTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // write dummy review via writePost with postType = "review"
        HashMap<String, ArrayList<String>> contents = new HashMap<>();
        // include rating if your DAO reads it from contents
        contents.put("rating", new ArrayList<>(Arrays.asList("-1"))); // or "4.0"

        dao.writePost(
                203332L,
                new Account("hi", "hi"),
                "review title",
                "review", // postType indicating a review
                "review body",
                contents,
                new ArrayList<>(Arrays.asList("tag1")),
                new ArrayList<>(), // images
                "2025-08-07 02:12 a.m.",
                new ArrayList<>() // clubs
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
        try {
            dao.deletePost(203332L);
        }
        catch (Exception ignored) {
        }
    }

    @Test
    void failTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // ensure no reviews exist by deleting known test id
        try {
            dao.deletePost(203332L);
        }
        catch (Exception ignored) {
        }

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
