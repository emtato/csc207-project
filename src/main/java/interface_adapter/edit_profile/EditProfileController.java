package interface_adapter.edit_profile;

import java.util.ArrayList;

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
     * @param username the username of the user whose profile is to be edited
     * @param newDisplayName the new display name
     * @param newBio the new bio
     * @param newProfilePictureUrl the new profile picture url address
     * @param newPreferences a new list of the user's preference tags
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
