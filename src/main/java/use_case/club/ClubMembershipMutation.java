package use_case.club;

/**
 * Membership mutation operations (currently only removal) for club domain.
 */
public interface ClubMembershipMutation {
    void removeMemberFromClub(String username, long clubID);
}

