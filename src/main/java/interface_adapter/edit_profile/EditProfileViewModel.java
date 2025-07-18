package interface_adapter.edit_profile;

import interface_adapter.ViewModel;

/**
 * The View Model for the Edit Profile View.
 */
public class EditProfileViewModel extends ViewModel<EditProfileState> {

    public static final String TITLE_LABEL = "Edit Profile";
    public static final int BIO_ROW_NUM = 2;
    public static final int BIO_COL_NUM = 2;
    public static final String BACK_BUTTON_LABEL = "Back";
    public static final String EDIT_NAME_LABEL = "Name";
    public static final String EDIT_BIO_LABEL = "Bio";
    public static final String UPLOAD_PFP_LABEL = "Upload New Profile Picture";
    public static final String PREFERENCE_TAGS_LABEL = "Preference Tags";
    public static final String SAVE_CHANGES_BUTTON_LABEL = "Save Changes";

    public EditProfileViewModel() {
        super("edit profile");
        setState(new EditProfileState());
    }
}