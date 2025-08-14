package use_case.select_club_members;

import entity.Account;
import java.util.List;

public interface SelectClubMembersInputBoundary {
    void showMemberSelection(List<Account> currentMembers, String creatorUsername);
}

