package use_case.like_post;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LikePostInteractorTest {

    @Test
    void likeTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        final LikePostInteractor interactor = new LikePostInteractor(dao);
        final LikePostInputData inputData = new LikePostInputData(103332, true);

        //create example post with the same postid to like
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put("steps", new ArrayList<>(Arrays.asList("hi")));
        dao.writePost(103332, new Account("hi", "hi"), "post title", "recipe", "description", map, new ArrayList<>(Arrays.asList("aa")), new ArrayList<>(Arrays.asList("aa")), "2025-08-07 02:12 AM", new ArrayList<>());

        interactor.execute(inputData);
        assertEquals(1, dao.getPost(103332).getLikes());
    }
}
//    @Test
//    void unlikeTest() {
//        final LoginInputData inputData = new LoginInputData("Pauline", "password");
//        final LoginUserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
//        final PostCommentsLikesDataAccessObject dataRepository = InMemoryPostCommentLikesDataAccessObject.getInstance();
//
//        // For the success test, we need to add Pauline to the data access repository before we log in.
//        UserFactory factory = new CreateAccount();
//        User user = factory.create("Pauline", "password");
//        userRepository.save(user);
//
//        // This creates a successPresenter that tests whether the test case is as we expect.
//        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
//            @Override
//            public void prepareSuccessView(LoginOutputData user) {
//                assertEquals("Pauline", userRepository.getCurrentUsername());
//            }
//
//            @Override
//            public void prepareFailView(String error) {
//                fail("Use case failure is unexpected.");
//            }
//        };
//
//        LoginInputBoundary interactor = new LoginInteractor(userRepository, dataRepository, successPresenter);
//        userRepository.setCurrentUsername(null);
//        assertEquals(null, userRepository.getCurrentUsername());
//
//        interactor.execute(inputData);
//    }
//}
