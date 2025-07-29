package use_case.manage_followers;

/**
 * Input Boundary for actions which are related to managing followers.
 */
public interface ManageFollowersInputBoundary {
    /**
     * Executes the manage followers use case.
     * @param manageFollowersInputData the input data
     */
    void execute(ManageFollowersInputData manageFollowersInputData);

    /**
     * Executes the switch to profile view use case.
     */
    void switchToProfileView();
}
