package use_case.profile;

import entity.Account;
import entity.Post;
import entity.User;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileInteractor implements ProfileInputBoundary{
    private final ProfileUserDataAccessInterface userDataAccessObject;
    private final ProfileOutputBoundary presenter;

    public ProfileInteractor(ProfileUserDataAccessInterface userDataAccessInterface,
                            ProfileOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void executeViewProfile(ProfileInputData profileInputData) {
        final String username = profileInputData.getUsername();
        final User user = userDataAccessObject.get(username);
        final String displayName = user.getDisplayName();
        final String bio = user.getBio();
        final String profilePictureUrl = user.getProfilePictureUrl();
        final int numFollowers = user.getNumFollowers();
        final int numFollowing = user.getNumFollowing();
        final HashMap<Long, Post> posts = user.getUserPosts();

        final ProfileOutputData profileOutputData = new ProfileOutputData(username, displayName, bio, profilePictureUrl,
                numFollowers, numFollowing, posts);
        presenter.prepareSuccessView(profileOutputData);
    }

    @Override
    public void switchToEditProfileView(ProfileInputData inputData) {
        final String username = inputData.getUsername();
        final User user = userDataAccessObject.get(username);
        final String displayName = user.getDisplayName();
        final String bio = user.getBio();
        final String profilePictureUrl = user.getProfilePictureUrl();
        final ArrayList<String> preferences = user.getFoodPreferences();

        final SwitchToEditProfileViewOutputData outputData = new SwitchToEditProfileViewOutputData(
                username, displayName, bio, profilePictureUrl, preferences);
        presenter.switchToEditProfileView(outputData);
    }

    @Override
    public void switchToManageFollowingView() {
        presenter.switchToManageFollowingView();
    }

    @Override
    public void switchToManageFollowersView() {
        presenter.switchToManageFollowersView();
    }

}
