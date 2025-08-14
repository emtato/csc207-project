package use_case.create_club;

import entity.Account;
import java.util.List;

public interface CreateClubInputBoundary {
    /**
     * Executes the create club use case with the provided input data.
     */
    void execute(CreateClubInputData inputData);

    /**
     * Shows the member selection interface for adding members to a club.
     *
     * @param currentMembers List of currently selected members
     * @param creatorUsername Username of the club creator
     */
    void showMemberSelection(List<Account> currentMembers, String creatorUsername);
}
