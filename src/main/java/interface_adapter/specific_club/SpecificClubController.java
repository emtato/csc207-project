package interface_adapter.specific_club;

import use_case.specific_club.SpecificClubInputBoundary;
import use_case.specific_club.SpecificClubInputData;

public class SpecificClubController {
    private final SpecificClubInputBoundary specificClubInteractor;

    public SpecificClubController(SpecificClubInputBoundary specificClubInteractor) {
        this.specificClubInteractor = specificClubInteractor;
    }

    public void execute(SpecificClubInputData inputData) {
        specificClubInteractor.execute(inputData);
    }

    public void loadClub(long clubId) {
        specificClubInteractor.loadClub(clubId);
    }

    public void fetchAnnouncements(long clubId) {
        specificClubInteractor.fetchAnnouncements(clubId);
    }

    public void fetchPosts(long clubId) {
        specificClubInteractor.fetchPosts(clubId);
    }

    public boolean isMember(String username, long clubId) {
        return specificClubInteractor.isMember(username, clubId);
    }
}
