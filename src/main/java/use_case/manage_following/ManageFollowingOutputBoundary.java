package use_case.manage_following;

/**
 * The output boundary for the Manage Following Use Case.
 */
public interface ManageFollowingOutputBoundary {
    /**
     * Prepares the success view for the Manage Following Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ManageFollowingOutputData outputData);

    /**
     * Prepares the failure view for the Manage Following Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Profile View.
     */
    void switchToProfileView();
}
