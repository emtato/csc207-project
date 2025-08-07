package use_case.edit_profile;

import entity.User;

public class EditProfileInteractor implements EditProfileInputBoundary {
    private final EditProfileUserDataAccessInterface userDataAccessObject;
    private final EditProfileOutputBoundary presenter;

    public EditProfileInteractor(EditProfileUserDataAccessInterface userDataAccessInterface,
                                 EditProfileOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditProfileInputData editProfileInputData) {
        final User user = userDataAccessObject.get(editProfileInputData.getUsername());
        userDataAccessObject.updateDisplayName(user, editProfileInputData.getNewDisplayName());
        userDataAccessObject.updateBio(user, editProfileInputData.getNewBio());
        userDataAccessObject.updateProfilePictureUrl(user, editProfileInputData.getNewProfilePictureUrl());
        userDataAccessObject.updatePreferences(user, editProfileInputData.getNewPreferences());

        final EditProfileOutputData editProfileOutputData =
                new EditProfileOutputData(
                        editProfileInputData.getNewDisplayName(),
                        editProfileInputData.getNewBio(),
                        editProfileInputData.getNewProfilePictureUrl(),
                        editProfileInputData.getNewPreferences());
        presenter.prepareSuccessView(editProfileOutputData);
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }
}
