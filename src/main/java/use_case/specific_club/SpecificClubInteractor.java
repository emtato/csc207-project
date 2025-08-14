package use_case.specific_club;

import data_access.UserDataAccessObject;
import entity.Club;
import use_case.club.ClubReadOperations;

public class SpecificClubInteractor implements SpecificClubInputBoundary {
    private final ClubReadOperations clubRead; // was ClubLookup
    private final UserDataAccessObject userDataAccessObject; // retained for future extension
    private final SpecificClubOutputBoundary specificClubPresenter;

    public SpecificClubInteractor(
            ClubReadOperations clubRead,
            UserDataAccessObject userDataAccessObject,
            SpecificClubOutputBoundary specificClubPresenter) {
        this.clubRead = clubRead;
        this.userDataAccessObject = userDataAccessObject;
        this.specificClubPresenter = specificClubPresenter;
    }

    @Override
    public void execute(SpecificClubInputData inputData) {
        try {
            Club club = clubRead.getClub(inputData.getClubId());
            if (club == null) {
                specificClubPresenter.prepareFailView("Club not found");
                return;
            }
            specificClubPresenter.prepareSuccessView(new SpecificClubOutputData(club, true, null));
        } catch (Exception e) {
            specificClubPresenter.prepareFailView("Error loading club: " + e.getMessage());
        }
    }

    @Override
    public void loadClub(long clubId) {
        try {
            Club club = clubRead.getClub(clubId);
            if (club == null) {
                specificClubPresenter.prepareFailView("Club not found");
                return;
            }
            specificClubPresenter.prepareSuccessView(new SpecificClubOutputData(club, true, null));
        } catch (Exception e) {
            specificClubPresenter.prepareFailView("Error loading club: " + e.getMessage());
        }
    }

    @Override
    public void fetchAnnouncements(long clubId) { loadClub(clubId); }

    @Override
    public void fetchPosts(long clubId) { loadClub(clubId); }

    @Override
    public boolean isMember(String username, long clubId) {
        try {
            Club club = clubRead.getClub(clubId);
            if (club == null) return false;
            return club.getMembers().stream().anyMatch(m -> m.getUsername().equals(username));
        } catch (Exception e) { return false; }
    }
}
