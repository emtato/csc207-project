package use_case.manage_following;

/**
 * Input Boundary for actions which are related to managing followed accounts.
 */
public interface ManageFollowingInputBoundary {
    /**
     * Executes the manage following use case.
     * @param manageFollowingInputData the input data
     */
    void execute(ManageFollowingInputData manageFollowingInputData);

    /**
     * Executes the switch to profile view use case.
     */
    void switchToProfileView();
}
