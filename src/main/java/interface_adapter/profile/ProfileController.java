package interface_adapter.profile;

import use_case.profile.ProfileInputBoundary;
import use_case.profile.ProfileInputData;

/**
 * Controller for the Profile Use Case.
 */
public class ProfileController {
    private final ProfileInputBoundary profileUseCaseInteractor;

    public ProfileController(ProfileInputBoundary profileUseCaseInteractor) {
        this.profileUseCaseInteractor = profileUseCaseInteractor;
    }

    /**
     * Executes the Profile Use Case.
     */
    public void execute() {
        final ProfileInputData profileInputData = new ProfileInputData();
        profileUseCaseInteractor.execute(profileInputData);
    }

    /**
     * Executes the "switch to Edit Profile View" Use Case.
     */
    public void switchToEditProfileView() {
        profileUseCaseInteractor.switchToEditProfileView();
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
