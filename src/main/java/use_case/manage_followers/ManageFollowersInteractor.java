package use_case.manage_followers;

import java.util.ArrayList;

import entity.User;

public class ManageFollowersInteractor implements ManageFollowersInputBoundary {
    private final ManageFollowersUserDataAccessInterface userDataAccessObject;
    private final ManageFollowersOutputBoundary presenter;

    public ManageFollowersInteractor(ManageFollowersUserDataAccessInterface userDataAccessInterface,
                                     ManageFollowersOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void executeRemoveFollower(ManageFollowersInputData manageFollowersInputData) {
        userDataAccessObject.removeFollower(manageFollowersInputData.getUsername(),
                manageFollowersInputData.getRemovedUsername());
        final User user = userDataAccessObject.get(manageFollowersInputData.getUsername());
        final ArrayList<User> followers = new ArrayList<>(user.getFollowerAccounts().values());
        final ArrayList<User> requesters = new ArrayList<>(user.getRequesterAccounts().values());
        final ManageFollowersOutputData outputData = new ManageFollowersOutputData(followers, requesters);
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void executeRemoveRequester(ManageFollowersInputData manageFollowersInputData) {
        userDataAccessObject.removeFollowRequester(manageFollowersInputData.getUsername(),
                manageFollowersInputData.getRemovedUsername());
        final User user = userDataAccessObject.get(manageFollowersInputData.getUsername());
        final ArrayList<User> followers = new ArrayList<>(user.getFollowerAccounts().values());
        final ArrayList<User> requesters = new ArrayList<>(user.getRequesterAccounts().values());
        final ManageFollowersOutputData outputData = new ManageFollowersOutputData(followers, requesters);
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void executeAcceptRequester(ManageFollowersInputData manageFollowersInputData) {
        userDataAccessObject.addFollowing(manageFollowersInputData.getRemovedUsername(),
                manageFollowersInputData.getUsername());
        final User user = userDataAccessObject.get(manageFollowersInputData.getUsername());
        final ArrayList<User> followers = new ArrayList<>(user.getFollowerAccounts().values());
        final ArrayList<User> requesters = new ArrayList<>(user.getRequesterAccounts().values());
        final ManageFollowersOutputData outputData = new ManageFollowersOutputData(followers, requesters);
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }
}
