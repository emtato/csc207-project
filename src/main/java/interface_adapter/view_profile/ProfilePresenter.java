package interface_adapter.view_profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.edit_profile.EditProfileState;
import interface_adapter.edit_profile.EditProfileViewModel;
import interface_adapter.manage_followers.ManageFollowersState;
import interface_adapter.manage_followers.ManageFollowersViewModel;
import interface_adapter.manage_following.ManageFollowingState;
import interface_adapter.manage_following.ManageFollowingViewModel;
import use_case.view_profile.ProfileOutputBoundary;
import use_case.view_profile.ProfileOutputData;
import use_case.view_profile.SwitchToEditProfileViewOutputData;
import use_case.view_profile.SwitchToFollowersViewOutputData;
import use_case.view_profile.SwitchToFollowingViewOutputData;

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
        final ProfileState profileState = profileViewModel.getState();
        profileState.setUsername(response.getUsername());
        profileState.setDisplayName(response.getDisplayName());
        profileState.setProfilePictureUrl(response.getProfilePictureUrl());
        profileState.setBio(response.getBio());
        profileState.setNumFollowers(response.getNumFollowers());
        profileState.setNumFollowing(response.getNumFollowing());
        profileState.setPosts(response.getPosts());
        profileViewModel.setState(profileState);
        profileViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
    }

    @Override
    public void switchToEditProfileView(SwitchToEditProfileViewOutputData outputData) {
        final EditProfileState editProfileState = editProfileViewModel.getState();
        editProfileState.setUsername(outputData.getUsername());
        editProfileState.setDisplayName(outputData.getDisplayName());
        editProfileState.setProfilePictureUrl(outputData.getProfilePictureUrl());
        editProfileState.setBio(outputData.getBio());
        editProfileState.setPreferences(outputData.getPreferences());
        editProfileState.setNewDisplayName(outputData.getDisplayName());
        editProfileState.setNewProfilePictureUrl(outputData.getProfilePictureUrl());
        editProfileState.setNewBio(outputData.getBio());
        editProfileState.setNewPreferences(outputData.getPreferences());
        editProfileViewModel.setState(editProfileState);
        editProfileViewModel.firePropertyChanged();
        viewManagerModel.setState(editProfileViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToManageFollowingView(SwitchToFollowingViewOutputData outputData) {
        final ManageFollowingState manageFollowingState = manageFollowingViewModel.getState();
        manageFollowingState.setUsername(outputData.getUsername());
        manageFollowingState.setFollowing(outputData.getFollowing());
        manageFollowingState.setRequested(outputData.getRequested());
        manageFollowingState.setOtherUsername("Enter username to follow");
        manageFollowingViewModel.setState(manageFollowingState);
        manageFollowingViewModel.firePropertyChanged();
        viewManagerModel.setState(manageFollowingViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToManageFollowersView(SwitchToFollowersViewOutputData outputData) {
        final ManageFollowersState manageFollowersState = manageFollowersViewModel.getState();
        manageFollowersState.setUsername(outputData.getUsername());
        manageFollowersState.setFollowers(outputData.getFollowers());
        manageFollowersState.setRequesters(outputData.getRequesters());
        manageFollowersViewModel.setState(manageFollowersState);
        manageFollowersViewModel.firePropertyChanged();
        viewManagerModel.setState(manageFollowersViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

}
