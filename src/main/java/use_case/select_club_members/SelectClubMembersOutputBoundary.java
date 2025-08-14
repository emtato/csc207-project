package use_case.select_club_members;

import entity.Account;
import java.util.List;

public interface SelectClubMembersOutputBoundary {
    void prepareSelectionView(List<Account> availableUsers);
    void prepareFailView(String error);
}

