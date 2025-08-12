package use_case.manage_following;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessObject;
import entity.CreateAccount;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ManageFollowingInteractorTest {

    @Test
    void unfollowSuccessTest() {

        // Create user with username Caf Feine
        final String username = "Caf Feine";
        final String password = "password";
        final ArrayList<User> followingAccountsUser1 = new ArrayList<>();
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);

        // Create user with username Decaf
        final String username2 = "Decaf";
        final String password2 = "password2";
        final ArrayList<User> followerAccountsUser2 = new ArrayList<>();
        final User user2 = factory.create(username2, password2);

        // Add users to the user repository and add a following from user to user2
        ManageFollowingInputData inputData = new ManageFollowingInputData(username, username2);
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.addFollowing(username, username2);
        followingAccountsUser1.add(user2);
        followerAccountsUser2.add(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ManageFollowingOutputBoundary successPresenter = new ManageFollowingOutputBoundary() {
            @Override
            public void prepareSuccessView(ManageFollowingOutputData outputData) {
                followingAccountsUser1.remove(user2);
                followerAccountsUser2.remove(user);
                assertEquals(followingAccountsUser1, outputData.getFollowing());
                assertEquals(followingAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getFollowingAccounts().values()));
                assertEquals(followerAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getFollowerAccounts().values()));
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

        ManageFollowingInputBoundary interactor = new ManageFollowingInteractor(userRepository, successPresenter);
        interactor.executeUnfollow(inputData);
    }

    @Test
    void followSuccessTest() {

        // Create user with username Caf
        final String username = "Caf";
        final String password = "password";
        final ArrayList<User> followingAccountsUser1 = new ArrayList<>();
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);

        // Create user with username Feine
        final String username2 = "Feine";
        final String password2 = "password2";
        final ArrayList<User> followerAccountsUser2 = new ArrayList<>();
        final User user2 = factory.create(username2, password2);

        // Add users to the user repository and add a following from user to user2
        ManageFollowingInputData inputData = new ManageFollowingInputData(username, username2);
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(user2);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ManageFollowingOutputBoundary successPresenter = new ManageFollowingOutputBoundary() {
            @Override
            public void prepareSuccessView(ManageFollowingOutputData outputData) {
                followingAccountsUser1.add(user2);
                followerAccountsUser2.add(user);
                assertEquals(followingAccountsUser1, outputData.getFollowing());
                assertEquals(followingAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getFollowingAccounts().values()));
                assertEquals(followerAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getFollowerAccounts().values()));
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

        ManageFollowingInputBoundary interactor = new ManageFollowingInteractor(userRepository, successPresenter);
        interactor.executeFollow(inputData);
    }

    @Test
    void followNonexistentUserFailureTest() {

        // Create user with username Caf
        final String username = "Caf";
        final String password = "password";
        final ArrayList<User> followingAccountsUser1 = new ArrayList<>();
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);

        // Add users to the user repository and add a following from user to user2
        ManageFollowingInputData inputData = new ManageFollowingInputData(username, "nonexistent_username");
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ManageFollowingOutputBoundary successPresenter = new ManageFollowingOutputBoundary() {
            @Override
            public void prepareSuccessView(ManageFollowingOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Error occurred while following user", error);
            }

            @Override
            public void switchToProfileView() {
                fail("This use case is unexpected.");
            }
        };

        ManageFollowingInputBoundary interactor = new ManageFollowingInteractor(userRepository, successPresenter);
        interactor.executeFollow(inputData);
    }

    @Test
    void requestSuccessTest() {

        // Create user with username Caf
        final String username = "Caf";
        final String password = "password";
        final ArrayList<User> followingAccountsUser1 = new ArrayList<>();
        final ArrayList<User> requestedAccountsUser1 = new ArrayList<>();
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);

        // Create user with username Feine
        final String username2 = "Feine";
        final String password2 = "password2";
        final ArrayList<User> followerAccountsUser2 = new ArrayList<>();
        final ArrayList<User> requesterAccountsUser2 = new ArrayList<>();
        final User user2 = factory.create(username2, password2);
        user2.setPublic(false);

        // Add users to the user repository and add a following from user to user2
        ManageFollowingInputData inputData = new ManageFollowingInputData(username, username2);
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(user2);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ManageFollowingOutputBoundary successPresenter = new ManageFollowingOutputBoundary() {
            @Override
            public void prepareSuccessView(ManageFollowingOutputData outputData) {
                requestedAccountsUser1.add(user2);
                requesterAccountsUser2.add(user);
                assertEquals(followingAccountsUser1, outputData.getFollowing());
                assertEquals(requestedAccountsUser1, outputData.getRequested());
                assertEquals(followingAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getFollowingAccounts().values()));
                assertEquals(followerAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getFollowerAccounts().values()));
                assertEquals(requestedAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getRequestedAccounts().values()));
                assertEquals(requesterAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getRequesterAccounts().values()));
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

        ManageFollowingInputBoundary interactor = new ManageFollowingInteractor(userRepository, successPresenter);
        interactor.executeFollow(inputData);
    }

    @Test
    void removeRequestSuccessTest() {

        // Create user with username Caf
        final String username = "Caf";
        final String password = "password";
        final ArrayList<User> followingAccountsUser1 = new ArrayList<>();
        final ArrayList<User> requestedAccountsUser1 = new ArrayList<>();
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);

        // Create user with username Feine
        final String username2 = "Feine";
        final String password2 = "password2";
        final ArrayList<User> followerAccountsUser2 = new ArrayList<>();
        final ArrayList<User> requesterAccountsUser2 = new ArrayList<>();
        final User user2 = factory.create(username2, password2);
        user2.setPublic(false);

        // Add users to the user repository and add a following from user to user2
        ManageFollowingInputData inputData = new ManageFollowingInputData(username, username2);
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.addFollowRequest(username, username2);
        requestedAccountsUser1.add(user2);
        requesterAccountsUser2.add(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ManageFollowingOutputBoundary successPresenter = new ManageFollowingOutputBoundary() {
            @Override
            public void prepareSuccessView(ManageFollowingOutputData outputData) {
                requestedAccountsUser1.remove(user2);
                requesterAccountsUser2.remove(user);
                assertEquals(followingAccountsUser1, outputData.getFollowing());
                assertEquals(requestedAccountsUser1, outputData.getRequested());
                assertEquals(followingAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getFollowingAccounts().values()));
                assertEquals(followerAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getFollowerAccounts().values()));
                assertEquals(requestedAccountsUser1,
                        new ArrayList<>(userRepository.get(username).getRequestedAccounts().values()));
                assertEquals(requesterAccountsUser2,
                        new ArrayList<>(userRepository.get(username2).getRequesterAccounts().values()));
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

        ManageFollowingInputBoundary interactor = new ManageFollowingInteractor(userRepository, successPresenter);
        interactor.executeUnfollow(inputData);
    }
}
