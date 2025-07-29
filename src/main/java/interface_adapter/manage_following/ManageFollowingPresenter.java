package interface_adapter.manage_following;

import interface_adapter.ViewManagerModel;
import interface_adapter.profile.ProfileViewModel;
import use_case.manage_following.ManageFollowingOutputBoundary;
import use_case.manage_following.ManageFollowingOutputData;

/**
 * The Presenter for the Manage Following Use Case.
 */
public class ManageFollowingPresenter implements ManageFollowingOutputBoundary {
    private final ManageFollowingViewModel manageFollowingViewModel;
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    public ManageFollowingPresenter(ViewManagerModel viewManagerModel,
                             ManageFollowingViewModel manageFollowingViewModel,
                             ProfileViewModel profileViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.manageFollowingViewModel = manageFollowingViewModel;
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void prepareSuccessView(ManageFollowingOutputData response) {
        // TODO: On success, do somethings.
    }

    @Override
    public void prepareFailView(String error) {
        final ManageFollowingState manageFollowingState = manageFollowingViewModel.getState();
        // TODO: On failure, do something
        //manageFollowingState.setUsernameError(error);
        //manageFollowingViewModel.firePropertyChanged();
    }

    @Override
    public void switchToProfileView() {
        viewManagerModel.setState(profileViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
