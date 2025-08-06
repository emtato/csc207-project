package use_case.manage_followers;

/**
 * Input Boundary for actions which are related to managing followers.
 */
public interface ManageFollowersInputBoundary {
    /**
     * Executes the remove follower use case.
     * @param manageFollowersInputData the input data
     */
    void executeRemoveFollower(ManageFollowersInputData manageFollowersInputData);

    /**
     * Executes the decline follow request use case.
     * @param manageFollowersInputData the input data
     */
    void executeRemoveRequester(ManageFollowersInputData manageFollowersInputData);

    /**
     * Executes the accept follow request use case.
     * @param manageFollowersInputData the input data
     */
    void executeAcceptRequester(ManageFollowersInputData manageFollowersInputData);

    /**
     * Executes the switch to profile view use case.
     */
    void switchToProfileView();
}
