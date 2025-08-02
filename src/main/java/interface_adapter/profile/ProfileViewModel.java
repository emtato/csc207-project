package interface_adapter.profile;

import interface_adapter.ViewModel;

/**
 * The View Model for the Profile View.
 */
public class ProfileViewModel extends ViewModel<ProfileState> {

    public static final String TITLE_LABEL = "Profile Menu";
    public static final int BIO_ROW_NUM = 3;
    public static final int BIO_COL_NUM = 20;
    public static final String EDIT_PROFILE_BUTTON_LABEL = "Edit Profile";
    public static final String FOLLOWING_BUTTON_LABEL = "Following";
    public static final String FOLLOWERS_BUTTON_LABEL = "Followers";
    public static final int CONTENT_ROW_NUM = 30;
    public static final int CONTENT_COL_NUM = 100;
    public static final int PFP_WIDTH = 200;
    public static final int PFP_HEIGHT = 200;
    public static final int MAIN_PANEL_WIDTH = 1200;
    public static final int MAIN_PANEL_HEIGHT = 300;

    public ProfileViewModel() {
        super("profile");
        setState(new ProfileState());
    }
}
