package interface_adapter.clubs_home;

import interface_adapter.ViewManagerModel;
import use_case.clubs_home.ClubOutputBoundary;
import use_case.clubs_home.ClubOutputData;

public class ClubPresenter implements ClubOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final ClubViewModel clubViewModel;

    public ClubPresenter(ViewManagerModel viewManagerModel, ClubViewModel clubViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.clubViewModel = clubViewModel;
    }

    @Override
    public void prepareSuccessView(ClubOutputData clubOutputData) {
        // On success, update the view model with the new data
        ClubState clubState = clubViewModel.getState();
        clubState.setMemberClubs(clubOutputData.getMemberClubs());
        clubState.setNonMemberClubs(clubOutputData.getNonMemberClubs());
        clubState.setAnnouncements(clubOutputData.getAnnouncements());
        clubViewModel.setState(clubState);
        clubViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        // On failure, update the view model with the error message
        ClubState clubState = clubViewModel.getState();
        clubState.setError(error);
        clubViewModel.setState(clubState);
        clubViewModel.firePropertyChanged();
    }
}
