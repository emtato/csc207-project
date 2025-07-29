package use_case.profile;

/**
 * The output boundary for the Profile Use Case.
 */
public interface ProfileOutputBoundary {

    /**
     * Prepares the success view for the Profile Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ProfileOutputData outputData);

    /**
     * Prepares the failure view for the Profile Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Edit Profile View.
     */
    void switchToEditProfileView();

    /**
     * Switches to the Manage Following View.
     */
    void switchToManageFollowingView();

    /**
     * Switches to the ManageFollowers View.
     */
    void switchToManageFollowersView();
}
