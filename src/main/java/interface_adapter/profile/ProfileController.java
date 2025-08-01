package interface_adapter.profile;

import use_case.profile.ProfileInputBoundary;
import use_case.profile.ProfileInputData;

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
     */
    public void executeViewProfile(String username) {
        final ProfileInputData profileInputData = new ProfileInputData(username);
        profileUseCaseInteractor.executeViewProfile(profileInputData);
    }

    /**
     * Executes the "switch to Edit Profile View" Use Case.
     */
    public void switchToEditProfileView(String username) {
        final ProfileInputData inputData = new ProfileInputData(username);
        profileUseCaseInteractor.switchToEditProfileView(inputData);
    }

    /**
     * Executes the "switch to Manage Following View" Use Case.
     */
    public void switchToManageFollowingView() {
        profileUseCaseInteractor.switchToManageFollowingView();
    }

    /**
     * Executes the "switch to Manage Followers View" Use Case.
     */
    public void switchToManageFollowersView() {
        profileUseCaseInteractor.switchToManageFollowersView();
    }
}
