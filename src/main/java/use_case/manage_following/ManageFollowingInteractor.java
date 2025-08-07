package use_case.manage_following;

import java.util.ArrayList;

import entity.User;

public class ManageFollowingInteractor implements ManageFollowingInputBoundary {
    private final ManageFollowingUserDataAccessInterface userDataAccessObject;
    private final ManageFollowingOutputBoundary presenter;

    public ManageFollowingInteractor(ManageFollowingUserDataAccessInterface userDataAccessInterface,
                                     ManageFollowingOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void executeUnfollow(ManageFollowingInputData manageFollowingInputData) {
        final User user = userDataAccessObject.get(manageFollowingInputData.getUsername());
        if (user.getRequestedAccounts().containsKey(manageFollowingInputData.getOtherUsername())) {
            userDataAccessObject.removeFollowRequest(manageFollowingInputData.getUsername(),
                    manageFollowingInputData.getOtherUsername());
        }
        else {
            userDataAccessObject.removeFollowing(manageFollowingInputData.getUsername(),
                    manageFollowingInputData.getOtherUsername());
        }
        final User updatedUser = userDataAccessObject.get(manageFollowingInputData.getUsername());
        final ArrayList<User> following = new ArrayList<>(updatedUser.getFollowingAccounts().values());
        final ArrayList<User> requested = new ArrayList<>(updatedUser.getRequestedAccounts().values());
        final ManageFollowingOutputData outputData = new ManageFollowingOutputData(following, requested, "Unfollow");
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void executeFollow(ManageFollowingInputData manageFollowingInputData) {
        if (userDataAccessObject.canRequestFollow(manageFollowingInputData.getUsername(),
                manageFollowingInputData.getOtherUsername())) {
            userDataAccessObject.addFollowRequest(manageFollowingInputData.getUsername(),
                    manageFollowingInputData.getOtherUsername());
            final User user = userDataAccessObject.get(manageFollowingInputData.getUsername());
            final ArrayList<User> following = new ArrayList<>(user.getFollowingAccounts().values());
            final ArrayList<User> requested = new ArrayList<>(user.getRequestedAccounts().values());
            final ManageFollowingOutputData outputData = new ManageFollowingOutputData(following, requested, "Request");
            presenter.prepareSuccessView(outputData);
        }
        else if (userDataAccessObject.canFollow(manageFollowingInputData.getUsername(),
                manageFollowingInputData.getOtherUsername())) {
            userDataAccessObject.addFollowing(manageFollowingInputData.getUsername(),
                    manageFollowingInputData.getOtherUsername());
            final User user = userDataAccessObject.get(manageFollowingInputData.getUsername());
            final ArrayList<User> following = new ArrayList<>(user.getFollowingAccounts().values());
            final ArrayList<User> requested = new ArrayList<>(user.getRequestedAccounts().values());
            final ManageFollowingOutputData outputData = new ManageFollowingOutputData(following, requested, "Follow");
            presenter.prepareSuccessView(outputData);
        }
        else {
            presenter.prepareFailView("Error occurred while following user");
        }
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }

}
