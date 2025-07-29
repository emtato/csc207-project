package interface_adapter.profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.manage_followers.ManageFollowersViewModel;
import interface_adapter.manage_following.ManageFollowingViewModel;
import interface_adapter.edit_profile.EditProfileViewModel;
import use_case.profile.ProfileOutputBoundary;
import use_case.profile.ProfileOutputData;

/**
 * The Presenter for the Profile Use Case.
 */
public class ProfilePresenter implements ProfileOutputBoundary {
    private final ProfileViewModel profileViewModel;
    private final EditProfileViewModel editProfileViewModel;
    private final ManageFollowingViewModel manageFollowingViewModel;
    private final ManageFollowersViewModel manageFollowersViewModel;
    private final ViewManagerModel viewManagerModel;

    public ProfilePresenter(ViewManagerModel viewManagerModel,
                             ProfileViewModel profileViewModel,
                            EditProfileViewModel editProfileViewModel,
                            ManageFollowingViewModel manageFollowingViewModel,
                            ManageFollowersViewModel manageFollowersViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.profileViewModel = profileViewModel;
        this.editProfileViewModel = editProfileViewModel;
        this.manageFollowingViewModel = manageFollowingViewModel;
        this.manageFollowersViewModel = manageFollowersViewModel;
    }

    @Override
    public void prepareSuccessView(ProfileOutputData response) {
        // TODO: On success, do somethings.
    }

    @Override
    public void prepareFailView(String error) {
        final ProfileState profileState = profileViewModel.getState();
        // TODO: On failure, do something
        //profileState.setUsernameError(error);
        //profileViewModel.firePropertyChanged();
    }

    @Override
    public void switchToEditProfileView() {
        viewManagerModel.setState(editProfileViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToManageFollowingView() {
        viewManagerModel.setState(manageFollowingViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToManageFollowersView() {
        viewManagerModel.setState(manageFollowersViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

}
