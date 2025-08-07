package interface_adapter.manage_following;

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
     * Executes the Unfollow Use Case.
     * @param username the user doing the unfollowing
     * @param followingUsername the usesr being unfollowed
     */
    public void executeUnfollow(String username, String followingUsername) {
        final ManageFollowingInputData manageFollowingInputData =
                new ManageFollowingInputData(username, followingUsername);
        manageFollowingUseCaseInteractor.executeUnfollow(manageFollowingInputData);
    }

    /**
     * Executes the Follow Use Case.
     * @param username the user doing the following
     * @param usernameToFollow the user being followed
     */
    public void executeFollow(String username, String usernameToFollow) {
        final ManageFollowingInputData manageFollowingInputData =
                new ManageFollowingInputData(username, usernameToFollow);
        manageFollowingUseCaseInteractor.executeFollow(manageFollowingInputData);
    }

    /**
     * Executes the "switch to Profile View" Use Case.
     */
    public void switchToProfileView() {
        manageFollowingUseCaseInteractor.switchToProfileView();
    }
}
