package interface_adapter.create_club;

import entity.Account;
import use_case.create_club.CreateClubOutputBoundary;
import use_case.create_club.CreateClubOutputData;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void prepareMemberSelectionView(List<Account> availableUsers) {
        CreateClubState state = createClubViewModel.getState();
        // Convert the Account objects to usernames for the state
        ArrayList<String> availableUsernames = new ArrayList<>();
        for (Account user : availableUsers) {
            availableUsernames.add(user.getUsername());
        }
        state.setMemberUsernames(availableUsernames);
        state.setSelectionMode(true);
        createClubViewModel.setState(state);
        createClubViewModel.firePropertyChanged();
    }
}
