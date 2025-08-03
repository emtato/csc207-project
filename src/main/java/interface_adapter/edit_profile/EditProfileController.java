package interface_adapter.edit_profile;

import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInputData;

import java.util.ArrayList;

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
    public void execute(String username, String newDisplayName, String newBio, String newProfilePictureUrl,
                        ArrayList<String> newPreferences) {
        final EditProfileInputData editProfileInputData = new EditProfileInputData(username, newDisplayName, newBio,
                newProfilePictureUrl, newPreferences);
        editProfileUseCaseInteractor.execute(editProfileInputData);
    }

    /**
     * Executes the "switch to Profile View" Use Case.
     */
    public void switchToProfileView() {
        editProfileUseCaseInteractor.switchToProfileView();
    }
}
