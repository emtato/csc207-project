package interface_adapter.leave_club;

import interface_adapter.clubs_home.ClubState;
import interface_adapter.clubs_home.ClubViewModel;
import interface_adapter.specific_club.SpecificClubState;
import interface_adapter.specific_club.SpecificClubViewModel;
import use_case.leave_club.LeaveClubOutputBoundary;
import use_case.leave_club.LeaveClubOutputData;

public class LeaveClubPresenter implements LeaveClubOutputBoundary {
    private final ClubViewModel clubViewModel;
    private final SpecificClubViewModel specificClubViewModel;

    public LeaveClubPresenter(ClubViewModel clubViewModel,
                              SpecificClubViewModel specificClubViewModel) {
        this.clubViewModel = clubViewModel;
        this.specificClubViewModel = specificClubViewModel;
    }

    @Override
    public void prepareSuccessView(LeaveClubOutputData outputData) {
        // Update club homepage lists
        ClubState clubState = clubViewModel.getState();
        clubState.setMemberClubs(outputData.getMemberClubs());
        clubState.setNonMemberClubs(outputData.getNonMemberClubs());
        clubState.setAnnouncements(outputData.getAnnouncements());
        clubState.setError(null);
        clubViewModel.setState(clubState);
        clubViewModel.firePropertyChanged();

        // Update specific club view (clear or show remaining data)
        SpecificClubState scState = specificClubViewModel.getState();
        scState.setClub(outputData.getClub());
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
        specificClubViewModel.setState(scState);
        specificClubViewModel.firePropertyChanged();
    }
}

