package use_case.edit_profile;

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
        //presenter.prepareFailView("error message");
        //final EditProfileOutputData editProfileOutputData = new EditProfileOutputData(data);
        //presenter.prepareSuccessView(editProfileOutputData);
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }
}
