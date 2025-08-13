package use_case.view_profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data_access.PostCommentsLikesDataAccessObject;
import entity.Post;
import entity.User;

public class ProfileInteractor implements ProfileInputBoundary {
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
        final String targetUsername = profileInputData.getTargetUsername();
        final User currentUser = userDataAccessObject.get(username);
        final User targetUser = userDataAccessObject.get(targetUsername);
        if (targetUser != null && (username.equals(targetUsername) || targetUser.isPublic()
                || currentUser.getFollowingAccounts().containsKey(targetUsername))) {
            final String displayName = targetUser.getDisplayName();
            final String bio = targetUser.getBio();
            final String profilePictureUrl = targetUser.getProfilePictureUrl();
            final int numFollowers = targetUser.getNumFollowers();
            final int numFollowing = targetUser.getNumFollowing();
            final ArrayList<Long> posts = targetUser.getUserPosts();
            final Map<Long, Post> postsMap = new HashMap<>();
            for (Long postId : posts) {
                postsMap.put(postId, postCommentsLikesDataAccessObject.getPost(postId));
            }

            final ProfileOutputData profileOutputData = new ProfileOutputData(username, displayName, bio,
                    profilePictureUrl, numFollowers, numFollowing, postsMap, targetUsername);
            presenter.prepareSuccessView(profileOutputData);
        }
        else if (targetUser == null) {
            presenter.prepareFailView("User not found");
        }
        else {
            presenter.prepareFailView("Account is private");
        }
    }

    @Override
    public void switchToEditProfileView(ProfileInputData inputData) {
        final String username = inputData.getUsername();
        final User user = userDataAccessObject.get(username);
        final String displayName = user.getDisplayName();
        final String bio = user.getBio();
        final String profilePictureUrl = user.getProfilePictureUrl();
        final ArrayList<String> preferences = user.getFoodPreferences();
        final String location = user.getLocation();

        final SwitchToEditProfileViewOutputData outputData = new SwitchToEditProfileViewOutputData(
                username, displayName, bio, profilePictureUrl, preferences, location);
        presenter.switchToEditProfileView(outputData);
    }

    @Override
    public void switchToManageFollowingView(ProfileInputData inputData) {
        final String username = inputData.getUsername();
        final User user = userDataAccessObject.get(username);
        final ArrayList<User> following = new ArrayList<>(user.getFollowingAccounts().values());
        final ArrayList<User> requested = new ArrayList<>(user.getRequestedAccounts().values());
        final SwitchToFollowingViewOutputData outputData = new SwitchToFollowingViewOutputData(username, following,
                requested);
        presenter.switchToManageFollowingView(outputData);
    }

    @Override
    public void switchToManageFollowersView(ProfileInputData inputData) {
        final String username = inputData.getUsername();
        final User user = userDataAccessObject.get(username);
        final ArrayList<User> followers = new ArrayList<>(user.getFollowerAccounts().values());
        final ArrayList<User> requesters = new ArrayList<>(user.getRequesterAccounts().values());
        final SwitchToFollowersViewOutputData outputData = new SwitchToFollowersViewOutputData(username, followers,
                requesters);
        presenter.switchToManageFollowersView(outputData);
    }

}
