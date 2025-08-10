package interface_adapter.create_club;

import interface_adapter.ViewModel;

public class CreateClubViewModel extends ViewModel<CreateClubState> {
    public CreateClubViewModel() {
        super("create club view");
        setState(new CreateClubState());
    }
}
