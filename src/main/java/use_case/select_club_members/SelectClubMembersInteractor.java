package use_case.select_club_members;

import entity.Account;
import use_case.create_club.CreateClubUserDataAccessInterface; // reuse existing gateway contract

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectClubMembersInteractor implements SelectClubMembersInputBoundary {
    private final CreateClubUserDataAccessInterface userGateway;
    private final SelectClubMembersOutputBoundary presenter;

    public SelectClubMembersInteractor(CreateClubUserDataAccessInterface userGateway,
                                       SelectClubMembersOutputBoundary presenter) {
        this.userGateway = userGateway;
        this.presenter = presenter;
    }

    @Override
    public void showMemberSelection(List<Account> currentMembers, String creatorUsername) {
        try {
            ArrayList<Account> allUsers = userGateway.getAllUsers();
            if (allUsers == null) {
                presenter.prepareSelectionView(new ArrayList<>());
                return;
            }
            Set<String> existing = new HashSet<>();
            for (Account a : currentMembers) {
                existing.add(a.getUsername());
            }
            ArrayList<Account> available = new ArrayList<>();
            for (Account u : allUsers) {
                String uname = u.getUsername();
                if (!uname.equals(creatorUsername) && !existing.contains(uname)) {
                    available.add(u);
                }
            }
            presenter.prepareSelectionView(available);
        } catch (Exception e) {
            presenter.prepareFailView("Error loading users: " + e.getMessage());
        }
    }
}

