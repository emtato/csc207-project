package use_case.profile;

public class ProfileInteractor implements ProfileInputBoundary{
    private final ProfileUserDataAccessInterface userDataAccessObject;
    private final ProfileOutputBoundary presenter;

    //TODO: factories
    public ProfileInteractor(ProfileUserDataAccessInterface userDataAccessInterface,
                            ProfileOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(ProfileInputData profileInputData) {
        //presenter.prepareFailView("error message");
        //final ProfileOutputData profileOutputData = new ProfileOutputData(data);
        //presenter.prepareSuccessView(profileOutputData);
    }

    @Override
    public void switchToEditProfileView() {
        presenter.switchToEditProfileView();
    }

    @Override
    public void switchToManageFollowingView() {
        presenter.switchToManageFollowingView();
    }

    @Override
    public void switchToManageFollowersView() {
        presenter.switchToManageFollowersView();
    }

}
