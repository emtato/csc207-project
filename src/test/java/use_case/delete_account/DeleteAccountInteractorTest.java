package use_case.delete_account;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.CreateAccount;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

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

        user.getFollowingAccounts().put(otherUsername, otherUser);
        otherUser.getFollowerAccounts().put(username, user);

        // Add user to the repository
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(otherUser);

        PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject =
                InMemoryPostCommentLikesDataAccessObject.getInstance();

        DeleteAccountInputData inputData = new DeleteAccountInputData(username);

        // This creates a successPresenter that tests whether the test case is as we expect.
        DeleteAccountOutputBoundary successPresenter = new DeleteAccountOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteAccountOutputData outputData) {
                assertEquals(username, outputData.getUsername());
                assertNull(userRepository.get(username));
                assertNull(userRepository.get(otherUsername).getFollowerAccounts().get(username));
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
