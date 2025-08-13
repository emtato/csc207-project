package interface_adapter.join_club;

import use_case.join_club.JoinClubInputBoundary;
import use_case.join_club.JoinClubInputData;

public class JoinClubController {
    private final JoinClubInputBoundary interactor;

    public JoinClubController(JoinClubInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void join(String username, long clubId) {
        interactor.execute(new JoinClubInputData(username, clubId));
    }
}

