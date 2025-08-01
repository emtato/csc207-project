package interface_adapter.edit_profile;

import interface_adapter.ViewModel;

/**
 * The View Model for the Edit Profile View.
 */
public class EditProfileViewModel extends ViewModel<EditProfileState> {

    public static final String TITLE_LABEL = "Edit Profile";
    public static final int INPUT_FIELD_COL_NUM = 20;
    public static final int PFP_WIDTH = 200;
    public static final int PFP_HEIGHT = 200;
    public static final int BIO_ROW_NUM = 3;
    public static final int PREF_ROW_NUM = 3;
    public static final String BACK_BUTTON_LABEL = "Back to Profile";
    public static final String EDIT_NAME_LABEL = "Name";
    public static final String EDIT_BIO_LABEL = "Bio";
    public static final String UPLOAD_PFP_LABEL = "Profile Picture URL";
    public static final String PREFERENCE_TAGS_LABEL = "Preference Tags";
    public static final String SAVE_CHANGES_BUTTON_LABEL = "Save Changes";
    public static final int EDIT_PANEL_WIDTH = 1200;
    public static final int EDIT_PANEL_HEIGHT = 800;

    public EditProfileViewModel() {
        super("edit profile");
        setState(new EditProfileState());
    }
}