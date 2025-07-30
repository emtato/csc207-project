package use_case.edit_profile;

import entity.User;

public class EditProfileInteractor implements EditProfileInputBoundary{
    private final EditProfileUserDataAccessInterface userDataAccessObject;
    private final EditProfileOutputBoundary presenter;

    //TODO: factories
    public EditProfileInteractor(EditProfileUserDataAccessInterface userDataAccessInterface,
                                 EditProfileOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditProfileInputData editProfileInputData) {
        if (true) {
            final User user = userDataAccessObject.get(editProfileInputData.getUsername());
            userDataAccessObject.updateDisplayName(user, editProfileInputData.getNewDisplayName());
            userDataAccessObject.updateBio(user, editProfileInputData.getNewBio());
            userDataAccessObject.updateProfilePicture(user, editProfileInputData.getNewProfilePicture());
            userDataAccessObject.updatePreferences(user, editProfileInputData.getNewPreferences());

            final EditProfileOutputData editProfileOutputData =
                    new EditProfileOutputData(
                            editProfileInputData.getNewDisplayName(),
                            editProfileInputData.getNewBio(),
                            editProfileInputData.getNewProfilePicture(),
                            editProfileInputData.getNewPreferences());
            presenter.prepareSuccessView(editProfileOutputData);
        } else {
            presenter.prepareFailView("error message");
        }
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }
}
