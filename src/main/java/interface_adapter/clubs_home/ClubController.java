package interface_adapter.clubs_home;

import use_case.clubs_home.ClubInputBoundary;
import use_case.clubs_home.ClubInputData;
import use_case.create_club.CreateClubInputData;

import java.util.ArrayList;

public class ClubController {
    final ClubInputBoundary clubUseCaseInteractor;

    public ClubController(ClubInputBoundary clubUseCaseInteractor) {
        this.clubUseCaseInteractor = clubUseCaseInteractor;
    }

    public void fetchClubs() {
        // Get current user from session
        String username = app.Session.getCurrentAccount().getUsername();
        clubUseCaseInteractor.getClubsData(username);
    }

    public void joinClub(String username, long clubId) {
        ClubInputData clubInputData = new ClubInputData(username, String.valueOf(clubId));
        clubUseCaseInteractor.joinClub(clubInputData);
    }

    public void createClub(String title, String description, ArrayList<String> memberUsernames, ArrayList<String> tags) {
        CreateClubInputData createClubInputData = new CreateClubInputData(title, description, null, tags, memberUsernames);
        clubUseCaseInteractor.createClub(createClubInputData);
    }
}
