package use_case.specific_club;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Club;

public class SpecificClubInteractor implements SpecificClubInputBoundary {
    private final ClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject; // retained for future extension
    private final SpecificClubOutputBoundary specificClubPresenter;

    public SpecificClubInteractor(
            ClubsDataAccessObject clubsDataAccessObject,
            UserDataAccessObject userDataAccessObject,
            SpecificClubOutputBoundary specificClubPresenter) {
        this.clubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.specificClubPresenter = specificClubPresenter;
    }

    @Override
    public void execute(SpecificClubInputData inputData) {
        try {
            Club club = clubsDataAccessObject.getClub(inputData.getClubId());
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
            Club club = clubsDataAccessObject.getClub(clubId);
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
    public void fetchAnnouncements(long clubId) {
        // Currently same as load; could filter announcements into separate output DTO if needed
        loadClub(clubId);
    }

    @Override
    public void fetchPosts(long clubId) {
        // Currently same as load; differentiation can be added later
        loadClub(clubId);
    }

    @Override
    public boolean isMember(String username, long clubId) {
        try {
            Club club = clubsDataAccessObject.getClub(clubId);
            if (club == null) return false;
            return club.getMembers().stream().anyMatch(m -> m.getUsername().equals(username));
        } catch (Exception e) {
            return false;
        }
    }
}
