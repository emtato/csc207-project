package interface_adapter.manage_followers;

import use_case.manage_followers.ManageFollowersInputBoundary;
import use_case.manage_followers.ManageFollowersInputData;

/**
 * Controller for the Manage Followers Use Case.
 */
public class ManageFollowersController {
    private final ManageFollowersInputBoundary manageFollowersUseCaseInteractor;

    public ManageFollowersController(ManageFollowersInputBoundary manageFollowersUseCaseInteractor) {
        this.manageFollowersUseCaseInteractor = manageFollowersUseCaseInteractor;
    }

    /**
     * Executes the Remove Follower Use Case.
     */
    public void executeRemoveFollower(String username, String followerUsername) {
        final ManageFollowersInputData manageFollowersInputData =
                new ManageFollowersInputData(username, followerUsername);
        manageFollowersUseCaseInteractor.executeRemoveFollower(manageFollowersInputData);
    }

    /**
     * Executes the Remove Follower Use Case.
     */
    public void executeRemoveRequester(String username, String requesterUsername) {
        final ManageFollowersInputData manageFollowersInputData =
                new ManageFollowersInputData(username, requesterUsername);
        manageFollowersUseCaseInteractor.executeRemoveRequester(manageFollowersInputData);
    }

    /**
     * Executes the Accept Follow Request Use Case.
     */
    public void executeAcceptRequester(String username, String requesterUsername) {
        final ManageFollowersInputData manageFollowersInputData =
                new ManageFollowersInputData(username, requesterUsername);
        manageFollowersUseCaseInteractor.executeAcceptRequester(manageFollowersInputData);
    }

    /**
     * Executes the "switch to Profile View" Use Case.
     */
    public void switchToProfileView() {
        manageFollowersUseCaseInteractor.switchToProfileView();
    }
}
