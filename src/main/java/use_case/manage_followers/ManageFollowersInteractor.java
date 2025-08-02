package use_case.manage_followers;

import entity.User;

import java.util.ArrayList;

public class ManageFollowersInteractor implements ManageFollowersInputBoundary{
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
                manageFollowersInputData.getRemovedFollower());
        final User user = userDataAccessObject.get(manageFollowersInputData.getUsername());
        final ArrayList<User> followers = new ArrayList<>(user.getFollowerAccounts().values());
        final ManageFollowersOutputData outputData = new ManageFollowersOutputData(followers);
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }
}
