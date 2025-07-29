package interface_adapter.edit_profile;

import interface_adapter.ViewManagerModel;
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
        // TODO: On success, do somethings.
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
