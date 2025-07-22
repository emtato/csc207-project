package interface_adapter.clubs;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class ClubViewModel extends ViewModel<ClubState> {

    public ClubViewModel() {
        super("club view");
        setState(new ClubState());
    }

}
