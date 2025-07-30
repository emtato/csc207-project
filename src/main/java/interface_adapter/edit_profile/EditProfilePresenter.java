package interface_adapter.edit_profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import use_case.edit_profile.EditProfileOutputBoundary;
import use_case.edit_profile.EditProfileOutputData;

/**
 * The Presenter for the Edit Profile Use Case.
 */
public class EditProfilePresenter implements EditProfileOutputBoundary {
    private final EditProfileViewModel editProfileViewModel;
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    public EditProfilePresenter(ViewManagerModel viewManagerModel,
                                EditProfileViewModel editProfileViewModel,
                                    ProfileViewModel profileViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.editProfileViewModel = editProfileViewModel;
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void prepareSuccessView(EditProfileOutputData response) {
        final EditProfileState editProfileState = editProfileViewModel.getState();
        editProfileState.setDisplayName(response.getNewDisplayName());
        editProfileState.setBio(response.getNewBio());
        editProfileState.setProfilePicture(response.getNewProfilePicture());
        editProfileState.setPreferences(response.getNewPreferences());
        editProfileViewModel.firePropertyChanged("displayName");
        editProfileViewModel.firePropertyChanged("bio");
        editProfileViewModel.firePropertyChanged("profilePicture");
        editProfileViewModel.firePropertyChanged("preferences");
    }

    @Override
    public void prepareFailView(String error) {
        final EditProfileState editProfileState = editProfileViewModel.getState();
        // TODO: On failure, do something
        //editProfileState.setUsernameError(error);
        //editProfileViewModel.firePropertyChanged();
    }

    @Override
    public void switchToProfileView() {
        viewManagerModel.setState(profileViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
