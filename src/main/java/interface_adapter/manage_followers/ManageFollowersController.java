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
     * Executes the Manage Followers Use Case.
     */
    public void execute() {
        final ManageFollowersInputData manageFollowersInputData = new ManageFollowersInputData();
        manageFollowersUseCaseInteractor.execute(manageFollowersInputData);
    }

    /**
     * Executes the "switch to Profile View" Use Case.
     */
    public void switchToProfileView() {
        manageFollowersUseCaseInteractor.switchToProfileView();
    }
}
