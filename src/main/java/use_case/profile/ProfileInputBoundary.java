package use_case.profile;

/**
 * Input Boundary for actions done on the profile menu.
 */
public interface ProfileInputBoundary {
    /**
     * Executes the view profile use case.
     * @param profileInputData the input data
     */
    void executeViewProfile(ProfileInputData profileInputData);

    /**
     * Switches to the Edit Profile View.
     */
    void switchToEditProfileView(ProfileInputData profileInputData);

    /**
     * Switches to the Manage Following View.
     */
    void switchToManageFollowingView();

    /**
     * Switches to the ManageFollowers View.
     */
    void switchToManageFollowersView();

    // TODO: switching to the views for the buttons on the menu bar
}
