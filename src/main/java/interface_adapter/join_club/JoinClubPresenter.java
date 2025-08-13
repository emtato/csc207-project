package interface_adapter.join_club;

import interface_adapter.clubs_home.ClubState;
import interface_adapter.clubs_home.ClubViewModel;
import use_case.join_club.JoinClubOutputBoundary;
import use_case.join_club.JoinClubOutputData;

public class JoinClubPresenter implements JoinClubOutputBoundary {
    private final ClubViewModel clubViewModel;

    public JoinClubPresenter(ClubViewModel clubViewModel) {
        this.clubViewModel = clubViewModel;
    }

    @Override
    public void prepareSuccessView(JoinClubOutputData outputData) {
        ClubState state = clubViewModel.getState();
        state.setMemberClubs(outputData.getMemberClubs());
        state.setNonMemberClubs(outputData.getNonMemberClubs());
        state.setAnnouncements(outputData.getAnnouncements());
        state.setError(null);
        clubViewModel.setState(state);
        clubViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        ClubState state = clubViewModel.getState();
        state.setError(error);
        clubViewModel.setState(state);
        clubViewModel.firePropertyChanged();
    }
}

