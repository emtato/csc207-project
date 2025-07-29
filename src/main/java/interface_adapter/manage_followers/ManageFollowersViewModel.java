package interface_adapter.manage_followers;

import interface_adapter.ViewModel;

/**
 * The View Model for the Manage Followers View.
 */
public class ManageFollowersViewModel extends ViewModel<ManageFollowersState> {

    public static final String TITLE_LABEL = "Followers";

    public ManageFollowersViewModel() {
        super("manage followers");
        setState(new ManageFollowersState());
    }
}
