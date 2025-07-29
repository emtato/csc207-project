package use_case.manage_following;

public class ManageFollowingInteractor implements ManageFollowingInputBoundary{
    private final ManageFollowingUserDataAccessInterface userDataAccessObject;
    private final ManageFollowingOutputBoundary presenter;

    //TODO: factories
    public ManageFollowingInteractor(ManageFollowingUserDataAccessInterface userDataAccessInterface,
                             ManageFollowingOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(ManageFollowingInputData manageFollowingInputData) {
        //presenter.prepareFailView("error message");
        //final ManageFollowingOutputData manageFollowingOutputData = new ManageFollowingOutputData(data);
        //presenter.prepareSuccessView(manageFollowingOutputData);
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }

}
