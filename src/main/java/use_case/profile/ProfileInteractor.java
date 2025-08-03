package use_case.profile;

import data_access.PostCommentsLikesDataAccessObject;
import entity.Post;
import entity.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileInteractor implements ProfileInputBoundary{
    private final ProfileUserDataAccessInterface userDataAccessObject;
    private final PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject;
    private final ProfileOutputBoundary presenter;

    public ProfileInteractor(ProfileUserDataAccessInterface userDataAccessInterface,
                            PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject,
                            ProfileOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.postCommentsLikesDataAccessObject = postCommentsLikesDataAccessObject;
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
        final ArrayList<Long> posts = user.getUserPosts();
        final HashMap<Long, Post> postsMap = new HashMap<>();
        for (Long postId : posts) {
            postsMap.put(postId, postCommentsLikesDataAccessObject.getPost(postId));
        }

        final ProfileOutputData profileOutputData = new ProfileOutputData(username, displayName, bio, profilePictureUrl,
                numFollowers, numFollowing, postsMap);
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
    public void switchToManageFollowingView(ProfileInputData inputData) {
        final String username = inputData.getUsername();
        final User user = userDataAccessObject.get(username);
        final ArrayList<User> following = new ArrayList<>(user.getFollowingAccounts().values());
        final SwitchToFollowingViewOutputData outputData = new SwitchToFollowingViewOutputData(username, following);
        presenter.switchToManageFollowingView(outputData);
    }

    @Override
    public void switchToManageFollowersView(ProfileInputData inputData) {
        final String username = inputData.getUsername();
        final User user = userDataAccessObject.get(username);
        final ArrayList<User> followers = new ArrayList<>(user.getFollowerAccounts().values());
        final SwitchToFollowersViewOutputData outputData = new SwitchToFollowersViewOutputData(username, followers);
        presenter.switchToManageFollowersView(outputData);
    }

}
