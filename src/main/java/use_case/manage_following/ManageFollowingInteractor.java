package use_case.manage_following;

import entity.User;
import use_case.manage_followers.ManageFollowersOutputBoundary;
import use_case.manage_followers.ManageFollowersOutputData;
import use_case.manage_followers.ManageFollowersUserDataAccessInterface;

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
                manageFollowingInputData.getUnfollowedUsername());
        final User user = userDataAccessObject.get(manageFollowingInputData.getUsername());
        final ArrayList<User> following = new ArrayList<>(user.getFollowingAccounts().values());
        final ManageFollowingOutputData outputData = new ManageFollowingOutputData(following);
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }

}
