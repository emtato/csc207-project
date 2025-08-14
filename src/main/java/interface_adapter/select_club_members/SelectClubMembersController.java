package interface_adapter.select_club_members;

import entity.Account;
import use_case.select_club_members.SelectClubMembersInputBoundary;
import java.util.List;

public class SelectClubMembersController {
    private final SelectClubMembersInputBoundary interactor;

    public SelectClubMembersController(SelectClubMembersInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void showMemberSelection(List<Account> currentMembers, String creatorUsername) {
        interactor.showMemberSelection(currentMembers, creatorUsername);
    }
}

