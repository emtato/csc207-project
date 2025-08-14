package interface_adapter.select_club_members;

import entity.Account;
import interface_adapter.create_club.CreateClubState;
import interface_adapter.create_club.CreateClubViewModel;
import use_case.select_club_members.SelectClubMembersOutputBoundary;

import java.util.ArrayList;
import java.util.List;

public class SelectClubMembersPresenter implements SelectClubMembersOutputBoundary {
    private final CreateClubViewModel createClubViewModel;

    public SelectClubMembersPresenter(CreateClubViewModel createClubViewModel) {
        this.createClubViewModel = createClubViewModel;
    }

    @Override
    public void prepareSelectionView(List<Account> availableUsers) {
        CreateClubState state = createClubViewModel.getState();
        ArrayList<String> usernames = new ArrayList<>();
        for (Account a : availableUsers) {
            usernames.add(a.getUsername());
        }
        state.setMemberUsernames(usernames);
        state.setSelectionMode(true);
        state.setError(null);
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

