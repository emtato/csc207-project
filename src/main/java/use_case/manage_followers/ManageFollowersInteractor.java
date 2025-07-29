package use_case.manage_followers;

public class ManageFollowersInteractor implements ManageFollowersInputBoundary{
    private final ManageFollowersUserDataAccessInterface userDataAccessObject;
    private final ManageFollowersOutputBoundary presenter;

    //TODO: factories
    public ManageFollowersInteractor(ManageFollowersUserDataAccessInterface userDataAccessInterface,
                                     ManageFollowersOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(ManageFollowersInputData manageFollowersInputData) {
        //presenter.prepareFailView("error message");
        //final ManageFollowersOutputData manageFollowersOutputData = new ManageFollowersOutputData(data);
        //presenter.prepareSuccessView(manageFollowersOutputData);
    }

    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }
}
