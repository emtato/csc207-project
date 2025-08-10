package use_case.create_club;

import entity.Account;
import java.util.List;

public interface CreateClubOutputBoundary {
    /**
     * Prepares the success view after club creation
     */
    void prepareSuccessView(CreateClubOutputData outputData);

    /**
     * Prepares the fail view with error message
     */
    void prepareFailView(String error);

    /**
     * Prepares the member selection view with available users
     */
    void prepareMemberSelectionView(List<Account> availableUsers);
}
