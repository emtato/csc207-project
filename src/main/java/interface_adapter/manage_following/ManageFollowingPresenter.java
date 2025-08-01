package interface_adapter.manage_following;

import interface_adapter.ViewManagerModel;
import interface_adapter.manage_followers.ManageFollowersState;
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
        final ManageFollowingState manageFollowingState = manageFollowingViewModel.getState();
        manageFollowingState.setFollowing(response.getFollowing());
        manageFollowingViewModel.setState(manageFollowingState);
        manageFollowingViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
    }

    @Override
    public void switchToProfileView() {
        viewManagerModel.setState(profileViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
