package use_case.manage_following;

import entity.User;

import java.util.ArrayList;

public class ManageFollowingInteractor implements ManageFollowingInputBoundary{
    private final ManageFollowingUserDataAccessInterface userDataAccessObject;
    private final ManageFollowingOutputBoundary presenter;

    public ManageFollowingInteractor(ManageFollowingUserDataAccessInterface userDataAccessInterface,
                                     ManageFollowingOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void executeUnfollow(ManageFollowingInputData manageFollowingInputData) {
        userDataAccessObject.removeFollowing(manageFollowingInputData.getUsername(),
                manageFollowingInputData.getOtherUsername());
        final User user = userDataAccessObject.get(manageFollowingInputData.getUsername());
        final ArrayList<User> following = new ArrayList<>(user.getFollowingAccounts().values());
        final ManageFollowingOutputData outputData = new ManageFollowingOutputData(following);
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void executeFollow(ManageFollowingInputData manageFollowingInputData) {
        if (userDataAccessObject.canFollow(manageFollowingInputData.getUsername(),
                manageFollowingInputData.getOtherUsername())){
            userDataAccessObject.addFollowing(manageFollowingInputData.getUsername(),
                    manageFollowingInputData.getOtherUsername());
            final User user = userDataAccessObject.get(manageFollowingInputData.getUsername());
            final ArrayList<User> following = new ArrayList<>(user.getFollowingAccounts().values());
            final ManageFollowingOutputData outputData = new ManageFollowingOutputData(following);
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
