package use_case.manage_followers;

/**
 * The output boundary for the Manage Followers Use Case.
 */
public interface ManageFollowersOutputBoundary {
    /**
     * Prepares the success view for the Manage Followers Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ManageFollowersOutputData outputData);

    /**
     * Prepares the failure view for the Manage Followers Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Profile View.
     */
    void switchToProfileView();
}
