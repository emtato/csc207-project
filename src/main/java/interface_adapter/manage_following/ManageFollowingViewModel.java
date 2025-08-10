package interface_adapter.manage_following;

import interface_adapter.ViewModel;
import view.GUIConstants;

/**
 * The View Model for the Manage Following View.
 */
public class ManageFollowingViewModel extends ViewModel<ManageFollowingState> {

    public static final String TITLE_LABEL = "Following";
    public static final String REMOVE_FOLLOW_LABEL = "Unfollow";
    public static final String UNREQUEST_LABEL = "Unrequest";
    public static final int PANEL_WIDTH = GUIConstants.STANDARD_PANEL_WIDTH;
    public static final int PANEL_HEIGHT = GUIConstants.STANDARD_PANEL_HEIGHT;
    public static final String BACK_BUTTON_LABEL = "Back to Profile";
    public static final String INPUT_PROMPT = "Enter username to follow";
    public static final String FOLLOW_BUTTON_LABEL = "Follow";

    public ManageFollowingViewModel() {
        super("manage following");
        setState(new ManageFollowingState());
    }
}
