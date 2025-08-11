package use_case.create_club;

import entity.Account;
import java.util.List;

public interface CreateClubInputBoundary {
    /**
     * Creates a new club with the provided details.
     *
     * @param title The title of the club
     * @param description The description of the club
     * @param imageUrl The URL of the club's profile image
     * @param tags List of tags associated with the club
     * @param memberUsernames List of usernames of initial club members
     */
    void create(String title, String description, String imageUrl, List<String> tags, List<String> memberUsernames);

    /**
     * Shows the member selection interface for adding members to a club.
     *
     * @param currentMembers List of currently selected members
     * @param creatorUsername Username of the club creator
     */
    void showMemberSelection(List<Account> currentMembers, String creatorUsername);
}
