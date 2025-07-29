package use_case.edit_profile;

/**
 * Input Boundary for actions which are related to editing a user's profile.
 */
public interface EditProfileInputBoundary {
    /**
     * Executes the edit profile use case.
     * @param editProfileInputData the input data
     */
    void execute(EditProfileInputData editProfileInputData);

    /**
     * Executes the switch to profile view use case.
     */
    void switchToProfileView();
}
