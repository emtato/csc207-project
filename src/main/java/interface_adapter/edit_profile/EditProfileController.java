package interface_adapter.edit_profile;

import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInputData;

/**
 * Controller for the Edit Profile Use Case.
 */
public class EditProfileController {
    private final EditProfileInputBoundary editProfileUseCaseInteractor;

    public EditProfileController(EditProfileInputBoundary editProfileUseCaseInteractor) {
        this.editProfileUseCaseInteractor = editProfileUseCaseInteractor;
    }

    /**
     * Executes the Edit Profile Use Case.
     */
    public void execute() {
        final EditProfileInputData editProfileInputData = new EditProfileInputData();
        editProfileUseCaseInteractor.execute(editProfileInputData);
    }

    /**
     * Executes the "switch to Edit Profile" Use Case.
     */
    public void switchToProfileView() {
        editProfileUseCaseInteractor.switchToProfileView();
    }
}
