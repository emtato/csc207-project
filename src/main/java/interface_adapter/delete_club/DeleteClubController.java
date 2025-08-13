package interface_adapter.delete_club;

import use_case.delete_club.DeleteClubInputBoundary;
import use_case.delete_club.DeleteClubInputData;

public class DeleteClubController {
    private final DeleteClubInputBoundary interactor;

    public DeleteClubController(DeleteClubInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void delete(String requesterUsername, long clubId) {
        interactor.execute(new DeleteClubInputData(requesterUsername, clubId));
    }
}

