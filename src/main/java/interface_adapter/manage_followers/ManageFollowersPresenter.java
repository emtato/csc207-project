package interface_adapter.manage_followers;

import interface_adapter.ViewManagerModel;
import interface_adapter.view_profile.ProfileViewModel;
import use_case.manage_followers.ManageFollowersOutputBoundary;
import use_case.manage_followers.ManageFollowersOutputData;

/**
 * The Presenter for the Manage Followers Use Case.
 */
public class ManageFollowersPresenter implements ManageFollowersOutputBoundary {
    private final ManageFollowersViewModel manageFollowersViewModel;
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    public ManageFollowersPresenter(ViewManagerModel viewManagerModel,
                                    ManageFollowersViewModel manageFollowersViewModel,
                                    ProfileViewModel profileViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.manageFollowersViewModel = manageFollowersViewModel;
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void prepareSuccessView(ManageFollowersOutputData response) {
        final ManageFollowersState manageFollowersState = manageFollowersViewModel.getState();
        manageFollowersState.setFollowers(response.getFollowers());
        manageFollowersState.setRequesters(response.getRequesters());
        manageFollowersViewModel.setState(manageFollowersState);
        manageFollowersViewModel.firePropertyChanged();
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
