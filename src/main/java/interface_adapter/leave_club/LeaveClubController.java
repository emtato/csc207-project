package interface_adapter.leave_club;

import use_case.leave_club.LeaveClubInputBoundary;
import use_case.leave_club.LeaveClubInputData;

public class LeaveClubController {
    private final LeaveClubInputBoundary interactor;

    public LeaveClubController(LeaveClubInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void leave(String username, long clubId) {
        interactor.execute(new LeaveClubInputData(username, clubId));
    }
}

