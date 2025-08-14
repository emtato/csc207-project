package use_case.create_review;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Post;
import entity.Review;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CreateReviewInteractorTest {

    @Test
    void successTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();
        UserDataAccessObject userDao = InMemoryUserDataAccessObject.getInstance();

        // Save dummy user into memory db
        Account user = new Account("username", "password");
        userDao.save(user);

        CreateReviewOutputBoundary successPresenter = new CreateReviewOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateReviewOutputData outputData) {
                long reviewID = outputData.getReviewID();
                Post post = dao.getPost(reviewID);
                assertEquals("username", post.getUser().getUsername());
                assertEquals("review title", post.getTitle());
                assertEquals("review", post.getType());
                assertEquals("review body", post.getDescription());
                Review review = (Review) post;
                assertEquals(4.5, review.getRating());
                assertEquals(new ArrayList<>(Arrays.asList("tag1")), post.getTags());

                dao.deletePost(post.getID());
            }

            @Override
            public void prepareFailView(CreateReviewOutputData outputData) {
                fail("Use case failure is unexpected.");
            }
        };

        CreateReviewInteractor interactor = new CreateReviewInteractor(dao, userDao, successPresenter);

        CreateReviewInputData inputData = new CreateReviewInputData(
                user,
                "review title",
                "review body",
                4.5,
                new ArrayList<>(Arrays.asList("tag1"))
        );

        interactor.execute(inputData);
    }

    @Test
    void failTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();
        UserDataAccessObject userDao = InMemoryUserDataAccessObject.getInstance();

        CreateReviewOutputBoundary failPresenter = new CreateReviewOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateReviewOutputData outputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailView(CreateReviewOutputData outputData) {
                assertEquals(0L, outputData.getReviewID());
            }
        };

        CreateReviewInteractor interactor = new CreateReviewInteractor(dao, userDao, failPresenter);

        // This user is not saved in userDao → should fail
        CreateReviewInputData inputData = new CreateReviewInputData(
                new Account("ghost", "pw"),
                "title",
                "body",
                5.0,
                new ArrayList<>()
        );

        interactor.execute(inputData);
    }
}
