package interface_adapter.manage_following;

import use_case.manage_followers.ManageFollowersInputData;
import use_case.manage_following.ManageFollowingInputBoundary;
import use_case.manage_following.ManageFollowingInputData;

/**
 * Controller for the Manage Following Use Case.
 */
public class ManageFollowingController {
    private final ManageFollowingInputBoundary manageFollowingUseCaseInteractor;

    public ManageFollowingController(ManageFollowingInputBoundary manageFollowingUseCaseInteractor) {
        this.manageFollowingUseCaseInteractor = manageFollowingUseCaseInteractor;
    }

    /**
     * Executes the Manage Following Use Case.
     */
    public void executeUnfollow(String username, String followingUsername) {
        final ManageFollowingInputData manageFollowingInputData =
                new ManageFollowingInputData(username, followingUsername);
        manageFollowingUseCaseInteractor.executeUnfollow(manageFollowingInputData);
    }

    /**
     * Executes the "switch to Profile View" Use Case.
     */
    public void switchToProfileView() {
        manageFollowingUseCaseInteractor.switchToProfileView();
    }
}
