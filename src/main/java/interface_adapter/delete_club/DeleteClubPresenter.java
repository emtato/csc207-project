package interface_adapter.delete_club;

import interface_adapter.clubs_home.ClubState;
import interface_adapter.clubs_home.ClubViewModel;
import interface_adapter.specific_club.SpecificClubState;
import interface_adapter.specific_club.SpecificClubViewModel;
import use_case.delete_club.DeleteClubOutputBoundary;
import use_case.delete_club.DeleteClubOutputData;

public class DeleteClubPresenter implements DeleteClubOutputBoundary {
    private final ClubViewModel clubViewModel;
    private final SpecificClubViewModel specificClubViewModel;

    public DeleteClubPresenter(ClubViewModel clubViewModel, SpecificClubViewModel specificClubViewModel) {
        this.clubViewModel = clubViewModel;
        this.specificClubViewModel = specificClubViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteClubOutputData data) {
        ClubState clubState = clubViewModel.getState();
        clubState.setMemberClubs(data.getMemberClubs());
        clubState.setNonMemberClubs(data.getNonMemberClubs());
        clubState.setAnnouncements(data.getAnnouncements());
        clubState.setError(null);
        clubViewModel.setState(clubState);
        clubViewModel.firePropertyChanged();

        SpecificClubState scState = specificClubViewModel.getState();
        scState.setDeleted(true);
        scState.setClub(null);
        scState.setError(null);
        specificClubViewModel.setState(scState);
        specificClubViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        ClubState clubState = clubViewModel.getState();
        clubState.setError(error);
        clubViewModel.setState(clubState);
        clubViewModel.firePropertyChanged();

        SpecificClubState scState = specificClubViewModel.getState();
        scState.setError(error);
        scState.setDeleted(false);
        specificClubViewModel.setState(scState);
        specificClubViewModel.firePropertyChanged();
    }
}

