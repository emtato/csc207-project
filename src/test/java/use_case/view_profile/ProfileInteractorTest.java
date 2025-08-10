package use_case.view_profile;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.CreateAccount;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ProfileInteractorTest {
    @Test
    void viewProfileSuccessTest() {
        ProfileInputData inputData = new ProfileInputData("Caf Feine", "Caf Feine");
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        PostCommentsLikesDataAccessObject dataRepository = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // Add Caf Feine to the user repository.
        UserFactory factory = new CreateAccount();
        User user = factory.create("Caf Feine", "password");
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ProfileOutputBoundary successPresenter = new ProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileOutputData outputData) {
                assertEquals("Caf Feine", outputData.getUsername());
                assertEquals("https://i.imgur.com/eA9NeJ1.jpeg", outputData.getProfilePictureUrl());
                assertEquals("", outputData.getBio());
                assertEquals("", outputData.getDisplayName());
                assertEquals(0, outputData.getNumFollowers());
                assertEquals(0, outputData.getNumFollowing());
                assertEquals(new HashMap<>(), outputData.getPosts());

                assertEquals("Caf Feine", userRepository.get("Caf Feine").getUsername());
                assertEquals("https://i.imgur.com/eA9NeJ1.jpeg",
                        userRepository.get("Caf Feine").getProfilePictureUrl());
                assertEquals("", userRepository.get("Caf Feine").getBio());
                assertEquals("", userRepository.get("Caf Feine").getDisplayName());
                assertEquals(0, userRepository.get("Caf Feine").getNumFollowers());
                assertEquals(0, userRepository.get("Caf Feine").getNumFollowing());
                assertEquals(new ArrayList<>(), userRepository.get("Caf Feine").getUserPosts());
            }
            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToEditProfileView(SwitchToEditProfileViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowingView(SwitchToFollowingViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowersView(SwitchToFollowersViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
        };

        ProfileInputBoundary interactor = new ProfileInteractor(userRepository, dataRepository, successPresenter);
        interactor.executeViewProfile(inputData);
    }

    @Test
    void viewProfileCustomUserSuccessTest() {
        final ProfileInputData inputData = new ProfileInputData("Caf Feine", "Caf Feine");
        final UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        final PostCommentsLikesDataAccessObject dataRepository = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // Add Caf to the user repository with custom account details
        final String username = "Caf Feine";
        final String password = "password";
        final String displayName = "Latte";
        final String bio = "I love you a latte!";
        final ArrayList<Long> userPosts = new ArrayList<>();
        final String profilePictureUrl = "https://i.imgur.com/zEM4Z3S_d.webp?maxwidth=520&shape=thumb&fidelity=high";
        final UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);
        user.setDisplayName(displayName);
        user.setBio(bio);
        user.setProfilePictureUrl(profilePictureUrl);
        user.setUserPosts(new ArrayList<>(userPosts));
        userRepository.save(user);
        final User user2 = factory.create("Decaf", "password123");
        userRepository.save(user2);
        userRepository.addFollowing(username, user2.getUsername());
        userRepository.addFollowing(user2.getUsername(), username);

        final HashMap<String, User> followerAccounts = new HashMap<>();
        final HashMap<String, User> followingAccounts = new HashMap<>();
        followerAccounts.put("Decaf", user2);
        followingAccounts.put("Decaf", user2);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ProfileOutputBoundary successPresenter = new ProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileOutputData outputData) {
                // Assert output data is as expected
                assertEquals(username, outputData.getUsername());
                assertEquals(username, outputData.getTargetUsername());
                assertEquals(profilePictureUrl, outputData.getProfilePictureUrl());
                assertEquals(bio, outputData.getBio());
                assertEquals(displayName, outputData.getDisplayName());
                assertEquals(followerAccounts.size(), outputData.getNumFollowers());
                assertEquals(followingAccounts.size(), outputData.getNumFollowing());
                assertEquals(userPosts, new ArrayList<>(outputData.getPosts().keySet()));

                // Assert data in the repository is as expected, and that the interactor did not change anything
                assertEquals(username, userRepository.get("Caf Feine").getUsername());
                assertEquals(profilePictureUrl, userRepository.get("Caf Feine").getProfilePictureUrl());
                assertEquals(bio, userRepository.get("Caf Feine").getBio());
                assertEquals(displayName, userRepository.get("Caf Feine").getDisplayName());
                assertEquals(followerAccounts.size(), userRepository.get("Caf Feine").getNumFollowers());
                assertEquals(followingAccounts.size(), userRepository.get("Caf Feine").getNumFollowing());
                assertEquals(userPosts, userRepository.get("Caf Feine").getUserPosts());
            }
            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToEditProfileView(SwitchToEditProfileViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowingView(SwitchToFollowingViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowersView(SwitchToFollowersViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
        };

        ProfileInputBoundary interactor = new ProfileInteractor(userRepository, dataRepository, successPresenter);
        interactor.executeViewProfile(inputData);
    }

    @Test
    void viewDifferentProfileSuccessTest() {
        final ProfileInputData inputData = new ProfileInputData("Caf Feine", "Decaf");
        final UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        final PostCommentsLikesDataAccessObject dataRepository = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // Add User1 to the user repository
        final UserFactory factory = new CreateAccount();
        final User user = factory.create("Caf Feine", "Password");
        userRepository.save(user);
        final User user2 = factory.create("Decaf", "Password");
        user2.setDisplayName("Display Name");
        user2.setBio("Bio");
        user2.setProfilePictureUrl("url.png");
        userRepository.save(user2);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ProfileOutputBoundary successPresenter = new ProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileOutputData outputData) {
                assertEquals("Caf Feine", outputData.getUsername());
                assertEquals("Decaf", outputData.getTargetUsername());
                assertEquals("Display Name", outputData.getDisplayName());
                assertEquals("Bio", outputData.getBio());
                assertEquals("url.png", outputData.getProfilePictureUrl());
            }
            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToEditProfileView(SwitchToEditProfileViewOutputData data) {
                fail("Use case failure is unexpected.");
            }
            @Override
            public void switchToManageFollowingView(SwitchToFollowingViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowersView(SwitchToFollowersViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
        };

        ProfileInputBoundary interactor = new ProfileInteractor(userRepository, dataRepository, successPresenter);
        interactor.executeViewProfile(inputData);
    }


    @Test
    void viewPrivateProfileFailureTest() {
        final ProfileInputData inputData = new ProfileInputData("Caf Feine", "Private_User");
        final UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        final PostCommentsLikesDataAccessObject dataRepository = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // Add User1 to the user repository
        final UserFactory factory = new CreateAccount();
        final User user = factory.create("Caf Feine", "Password");
        userRepository.save(user);
        final User privateUser = factory.create("Private_User", "Password");
        privateUser.setPublic(false);
        userRepository.save(privateUser);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ProfileOutputBoundary successPresenter = new ProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }
            @Override
            public void prepareFailView(String error) {
                assertEquals("Account is private", error);
            }

            @Override
            public void switchToEditProfileView(SwitchToEditProfileViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowingView(SwitchToFollowingViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowersView(SwitchToFollowersViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
        };

        ProfileInputBoundary interactor = new ProfileInteractor(userRepository, dataRepository, successPresenter);
        interactor.executeViewProfile(inputData);
    }

    @Test
    void viewNonexistentProfileFailureTest() {
        final ProfileInputData inputData = new ProfileInputData("Caf Feine", "Non_existent_User");
        final UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        final PostCommentsLikesDataAccessObject dataRepository = InMemoryPostCommentLikesDataAccessObject.getInstance();

        // Add User1 to the user repository
        final UserFactory factory = new CreateAccount();
        final User user = factory.create("Caf Feine", "Password");
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ProfileOutputBoundary successPresenter = new ProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }
            @Override
            public void prepareFailView(String error) {
                assertEquals("User not found", error);
            }

            @Override
            public void switchToEditProfileView(SwitchToEditProfileViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowingView(SwitchToFollowingViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
            @Override
            public void switchToManageFollowersView(SwitchToFollowersViewOutputData data) {
                fail("Switch view use case is unexpected.");
            }
        };

        ProfileInputBoundary interactor = new ProfileInteractor(userRepository, dataRepository, successPresenter);
        interactor.executeViewProfile(inputData);
    }
}
