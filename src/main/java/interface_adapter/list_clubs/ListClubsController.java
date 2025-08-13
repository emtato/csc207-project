package interface_adapter.list_clubs;

import use_case.list_clubs.ListClubsInputBoundary;
import use_case.list_clubs.ListClubsInputData;

public class ListClubsController {
    private final ListClubsInputBoundary interactor;

    public ListClubsController(ListClubsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void fetch(String username) {
        interactor.execute(new ListClubsInputData(username));
    }
}

