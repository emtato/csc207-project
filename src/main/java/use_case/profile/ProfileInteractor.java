package use_case.profile;

import entity.Account;
import entity.User;

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
    public void executeViewProfile(ProfileInputData profileInputData) {
        //presenter.prepareFailView("error message");
        final Account user = (Account) userDataAccessObject.get(profileInputData.getUsername());
        //final ProfileOutputData profileOutputData = new ProfileOutputData(user.getUsername(), user.getBio(),
        //        user.getProfilePicture(), user.getUserPosts());
        //presenter.prepareSuccessView(profileOutputData);
    }

    @Override
    public void switchToEditProfileView(SwitchToEditProfileViewInputData inputData) {
        final String username = inputData.getUsername();
        final Account user = (Account) userDataAccessObject.get(username);
        final SwitchToEditProfileViewOutputData outputData = new SwitchToEditProfileViewOutputData(
                user.getName(), user.getBio(), user.getProfilePicture(), "");
        presenter.switchToEditProfileView(outputData);
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
