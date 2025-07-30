package interface_adapter.profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.edit_profile.EditProfileState;
import interface_adapter.manage_followers.ManageFollowersState;
import interface_adapter.manage_followers.ManageFollowersViewModel;
import interface_adapter.manage_following.ManageFollowingState;
import interface_adapter.manage_following.ManageFollowingViewModel;
import interface_adapter.edit_profile.EditProfileViewModel;
import use_case.profile.ProfileOutputBoundary;
import use_case.profile.ProfileOutputData;
import use_case.profile.SwitchToEditProfileViewOutputData;

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
    public void switchToEditProfileView(SwitchToEditProfileViewOutputData outputData) {
        final ProfileState profileState = profileViewModel.getState();
        final EditProfileState editProfileState = editProfileViewModel.getState();

        if(!editProfileState.getUsername().equals(profileState.getUsername())) {
            editProfileState.setUsername(profileState.getUsername());
            editProfileViewModel.firePropertyChanged("username");
        }

        viewManagerModel.setState(editProfileViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToManageFollowingView() {
        final ProfileState profileState = profileViewModel.getState();
        final ManageFollowingState manageFollowingState = manageFollowingViewModel.getState();
        manageFollowingState.setUsername(profileState.getUsername());
        viewManagerModel.setState(manageFollowingViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToManageFollowersView() {
        final ProfileState profileState = profileViewModel.getState();
        final ManageFollowersState manageFollowersState = manageFollowersViewModel.getState();
        manageFollowersState.setUsername(profileState.getUsername());
        viewManagerModel.setState(manageFollowersViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

}
