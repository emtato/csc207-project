package interface_adapter.specific_club;

import use_case.specific_club.SpecificClubOutputBoundary;
import use_case.specific_club.SpecificClubOutputData;

public class SpecificClubPresenter implements SpecificClubOutputBoundary {
    private final SpecificClubViewModel viewModel;

    public SpecificClubPresenter(SpecificClubViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SpecificClubOutputData outputData) {
        SpecificClubState state = viewModel.getState();
        state.setClub(outputData.getClub());
        state.setError(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        SpecificClubState state = viewModel.getState();
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
