package interface_adapter.manage_following;

import interface_adapter.ViewModel;

/**
 * The View Model for the Manage Following View.
 */
public class ManageFollowingViewModel extends ViewModel<ManageFollowingState> {

    public static final String TITLE_LABEL = "Following";
    public static final String REMOVE_LABEL = "Unfollow";
    public static final int PANEL_WIDTH = 500;
    public static final int PANEL_HEIGHT = 700;

    public ManageFollowingViewModel() {
        super("manage following");
        setState(new ManageFollowingState());
    }
}