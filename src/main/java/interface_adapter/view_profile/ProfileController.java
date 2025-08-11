package interface_adapter.view_profile;

import use_case.view_profile.ProfileInputBoundary;
import use_case.view_profile.ProfileInputData;

/**
 * Controller for the Profile related Use Cases.
 */
public class ProfileController {
    private final ProfileInputBoundary profileUseCaseInteractor;

    public ProfileController(ProfileInputBoundary profileUseCaseInteractor) {
        this.profileUseCaseInteractor = profileUseCaseInteractor;
    }

    /**
     * Executes the View Profile Use Case.
     * @param username the username of the logged-in user
     * @param targetUsername the username of the user whose profile is being viewed
     */
    public void executeViewProfile(String username, String targetUsername) {
        final ProfileInputData profileInputData = new ProfileInputData(username, targetUsername);
        profileUseCaseInteractor.executeViewProfile(profileInputData);
    }

    /**
     * Executes the "switch to Edit Profile View" Use Case.
     * @param username the username of the current user
     */
    public void switchToEditProfileView(String username) {
        final ProfileInputData inputData = new ProfileInputData(username, username);
        profileUseCaseInteractor.switchToEditProfileView(inputData);
    }

    /**
     * Executes the "switch to Manage Following View" Use Case.
     * @param username the username of the current user
     */
    public void switchToManageFollowingView(String username) {
        final ProfileInputData inputData = new ProfileInputData(username, username);
        profileUseCaseInteractor.switchToManageFollowingView(inputData);
    }

    /**
     * Executes the "switch to Manage Followers View" Use Case.
     * @param username the username of the current user
     */
    public void switchToManageFollowersView(String username) {
        final ProfileInputData inputData = new ProfileInputData(username, username);
        profileUseCaseInteractor.switchToManageFollowersView(inputData);
    }
}
