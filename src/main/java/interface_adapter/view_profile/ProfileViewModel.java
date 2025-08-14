package interface_adapter.view_profile;

import interface_adapter.ViewModel;
import view.GUIConstants;

/**
 * The View Model for the Profile View.
 */
public class ProfileViewModel extends ViewModel<ProfileState> {

    public static final String TITLE_LABEL = "Profile Menu";
    public static final int BIO_ROW_NUM = 3;
    public static final int BIO_COL_NUM = 10;
    public static final String EDIT_PROFILE_BUTTON_LABEL = "Edit Profile";
    public static final String FOLLOWING_BUTTON_LABEL = "Following";
    public static final String FOLLOWERS_BUTTON_LABEL = "Followers";
    public static final int CONTENT_PANEL_WIDTH = 1200;
    public static final int CONTENT_PANEL_HEIGHT = 250;
    public static final int INFO_PANEL_WIDTH = 1000;
    public static final int INFO_PANEL_HEIGHT = 300;
    public static final int PFP_WIDTH = 200;
    public static final int PFP_HEIGHT = 200;
    public static final int MAIN_PANEL_WIDTH = (int) (GUIConstants.WINDOW_WIDTH * 0.8);
    public static final int MAIN_PANEL_HEIGHT = (int) (GUIConstants.WINDOW_HEIGHT * 0.3);
    public static final int POST_WIDTH = 600;
    public static final int POST_HEIGHT = 400;
    public static final String SEE_MY_PROFILE_BUTTON_LABEL = "View My Profile";
    public static final String SEE_OTHER_PROFILE_BUTTON_LABEL = "View Different Profile";

    public ProfileViewModel() {
        super("profile");
        setState(new ProfileState());
    }
}
