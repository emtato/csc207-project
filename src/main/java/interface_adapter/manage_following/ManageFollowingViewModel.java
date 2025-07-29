package interface_adapter.manage_following;

import interface_adapter.ViewModel;

/**
 * The View Model for the Manage Following View.
 */
public class ManageFollowingViewModel extends ViewModel<ManageFollowingState> {

    public static final String TITLE_LABEL = "Following";

    public ManageFollowingViewModel() {
        super("manage following");
        setState(new ManageFollowingState());
    }
}