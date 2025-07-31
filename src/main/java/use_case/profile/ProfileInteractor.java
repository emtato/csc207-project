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
        final String displayName = userDataAccessObject.getDisplayName(username);
        final String bio = userDataAccessObject.getBio(username);
        final Image profilePicture = userDataAccessObject.getProfilePicture(username);
        final int numFollowers = userDataAccessObject.getNumFollowers(username);
        final int numFollowing = userDataAccessObject.getNumFollowing(username);
        final HashMap<Long, Post> posts = userDataAccessObject.getPosts(username);

        final ProfileOutputData profileOutputData = new ProfileOutputData(username, displayName, bio, profilePicture,
                numFollowers, numFollowing, posts);
        presenter.prepareSuccessView(profileOutputData);
    }

    @Override
    public void switchToEditProfileView(SwitchToEditProfileViewInputData inputData) {
        final String username = inputData.getUsername();
        final String displayName = userDataAccessObject.getDisplayName(username);
        final String bio = userDataAccessObject.getBio(username);
        final Image profilePicture = userDataAccessObject.getProfilePicture(username);
        final ArrayList<String> preferences = userDataAccessObject.getPreferences(username);

        final SwitchToEditProfileViewOutputData outputData = new SwitchToEditProfileViewOutputData(
                username, displayName, bio, profilePicture, preferences);
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
