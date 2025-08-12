package use_case.delete_account;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.*;
import org.junit.jupiter.api.Test;
import use_case.create_post.CreatePostInputData;
import use_case.create_post.CreatePostInteractor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteAccountInteractorTest {
    @Test
    void deleteAccountSuccessTest() {

        // Create user with username Caf Feine
        final String username = "Caf Feine";
        final String password = "password";
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);

        // Create a user that Caf Feine will follow
        final String otherUsername = "Latte";
        final String otherPassword = "password!";
        final User otherUser = factory.create(otherUsername, otherPassword);

        // Create a post for the user
        ArrayList<Long> posts = new ArrayList<>();
        posts.add(1L);
        user.setUserPosts(posts);

        user.getFollowingAccounts().put(otherUsername, otherUser);
        otherUser.getFollowerAccounts().put(username, user);

        // Add user to the repository
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(otherUser);

        // Add post to the repository
        PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject =
                InMemoryPostCommentLikesDataAccessObject.getInstance();

        postCommentsLikesDataAccessObject.writePost(103332, new Account("hi", "hi"), "post title", "test",
                "description", new HashMap<>(), new ArrayList<>(List.of("aa")),
                new ArrayList<>(List.of("aa")), "2025-08-07 02:12 AM", new ArrayList<>());

        DeleteAccountInputData inputData = new DeleteAccountInputData(username);

        // This creates a successPresenter that tests whether the test case is as we expect.
        DeleteAccountOutputBoundary successPresenter = new DeleteAccountOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteAccountOutputData outputData) {
                assertEquals(username, outputData.getUsername());
                assertNull(userRepository.get(username));
                assertNull(userRepository.get(otherUsername).getFollowerAccounts().get(username));
                assertNull(postCommentsLikesDataAccessObject.getPost(1L));
            }
            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        DeleteAccountInputBoundary interactor = new DeleteAccountInteractor(userRepository,
                postCommentsLikesDataAccessObject, successPresenter);
        interactor.execute(inputData);
    }
}
