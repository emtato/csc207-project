package interface_adapter.create_club;

import use_case.create_club.CreateClubOutputBoundary;
import use_case.create_club.CreateClubOutputData;

public class CreateClubPresenter implements CreateClubOutputBoundary {
    private final CreateClubViewModel createClubViewModel;

    public CreateClubPresenter(CreateClubViewModel createClubViewModel) {
        this.createClubViewModel = createClubViewModel;
    }

    @Override
    public void prepareSuccessView(CreateClubOutputData outputData) {
        CreateClubState state = createClubViewModel.getState();
        state.setError(null);
        state.setSelectionMode(false);
        createClubViewModel.setState(state);
        createClubViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        CreateClubState state = createClubViewModel.getState();
        state.setError(error);
        state.setSelectionMode(false);
        createClubViewModel.setState(state);
        createClubViewModel.firePropertyChanged();
    }
}
