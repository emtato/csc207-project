package use_case.manage_followers;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessObject;
import entity.CreateAccount;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ManageFollowersInteractorTest {
    @Test
    void removeFollowerSuccessTest() {

        // Create user with username Caf Feine
        final String username = "Caf Feine";
        final String password = "password";
        final ArrayList<User> followerAccountsUser1 = new ArrayList<>();
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);

        // Create user with username Decaf
        final String username2 = "Decaf";
        final String password2 = "password2";
        final ArrayList<User> followingAccountsUser2 = new ArrayList<>();
        final User user2 = factory.create(username2, password2);

        // Add users to the user repository and add a following from user2 to user1
        ManageFollowersInputData inputData = new ManageFollowersInputData(username, username2);
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.addFollowing(username2, username);
        followerAccountsUser1.add(user2);
        followingAccountsUser2.add(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ManageFollowersOutputBoundary successPresenter = new ManageFollowersOutputBoundary() {
            @Override
            public void prepareSuccessView(ManageFollowersOutputData outputData) {
                followerAccountsUser1.remove(user2);
                followingAccountsUser2.remove(user);
                assertEquals(followerAccountsUser1, outputData.getFollowers());
                assertEquals(followerAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getFollowerAccounts().values()));
                assertEquals(followingAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getFollowingAccounts().values()));
            }
            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToProfileView() {
                fail("This use case is unexpected.");
            }
        };

        ManageFollowersInputBoundary interactor = new ManageFollowersInteractor(userRepository, successPresenter);
        interactor.executeRemoveFollower(inputData);
    }

    @Test
    void removeRequesterSuccessTest() {

        // Create private user with username Caf Feine
        final String username = "Caf Feine";
        final String password = "password";
        final ArrayList<User> requesterAccountsUser1 = new ArrayList<>();
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);
        user.setPublic(false);

        // Create user with username Decaf
        final String username2 = "Decaf";
        final String password2 = "password2";
        final ArrayList<User> requestedAccountsUser2 = new ArrayList<>();
        final User user2 = factory.create(username2, password2);

        // Add users to the user repository and add a request from user2 to user1
        ManageFollowersInputData inputData = new ManageFollowersInputData(username, username2);
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.addFollowRequest(username2, username);
        requesterAccountsUser1.add(user2);
        requestedAccountsUser2.add(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ManageFollowersOutputBoundary successPresenter = new ManageFollowersOutputBoundary() {
            @Override
            public void prepareSuccessView(ManageFollowersOutputData outputData) {
                requesterAccountsUser1.remove(user2);
                requestedAccountsUser2.remove(user);
                assertEquals(requesterAccountsUser1, outputData.getRequesters());
                assertEquals(requesterAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getRequesterAccounts().values()));
                assertEquals(requestedAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getRequestedAccounts().values()));
            }
            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToProfileView() {
                fail("This use case is unexpected.");
            }
        };

        ManageFollowersInputBoundary interactor = new ManageFollowersInteractor(userRepository, successPresenter);
        interactor.executeRemoveRequester(inputData);
    }

    @Test
    void acceptRequesterSuccessTest() {

        // Create private user with username Caf Feine
        final String username = "Caf Feine";
        final String password = "password";
        final ArrayList<User> requesterAccountsUser1 = new ArrayList<>();
        final ArrayList<User> followerAccountsUser1 = new ArrayList<>();
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);
        user.setPublic(false);

        // Create user with username Decaf
        final String username2 = "Decaf";
        final String password2 = "password2";
        final ArrayList<User> requestedAccountsUser2 = new ArrayList<>();
        final ArrayList<User> followingAccountsUser2 = new ArrayList<>();
        final User user2 = factory.create(username2, password2);

        // Add users to the user repository and add a request from user2 to user1
        ManageFollowersInputData inputData = new ManageFollowersInputData(username, username2);
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.addFollowRequest(username2, username);
        requesterAccountsUser1.add(user2);
        requestedAccountsUser2.add(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ManageFollowersOutputBoundary successPresenter = new ManageFollowersOutputBoundary() {
            @Override
            public void prepareSuccessView(ManageFollowersOutputData outputData) {
                requesterAccountsUser1.remove(user2);
                requestedAccountsUser2.remove(user);
                followerAccountsUser1.add(user2);
                followingAccountsUser2.add(user);
                assertEquals(requesterAccountsUser1, outputData.getRequesters());
                assertEquals(followerAccountsUser1, outputData.getFollowers());
                assertEquals(requesterAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getRequesterAccounts().values()));
                assertEquals(requestedAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getRequestedAccounts().values()));
                assertEquals(followerAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getFollowerAccounts().values()));
                assertEquals(followingAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getFollowingAccounts().values()));
            }
            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToProfileView() {
                fail("This use case is unexpected.");
            }
        };

        ManageFollowersInputBoundary interactor = new ManageFollowersInteractor(userRepository, successPresenter);
        interactor.executeAcceptRequester(inputData);
    }
}
