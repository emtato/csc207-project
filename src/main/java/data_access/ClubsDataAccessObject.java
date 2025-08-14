package data_access;

import use_case.club.ClubReadOperations;
import use_case.create_club.ClubWriteOperations;
import use_case.club.ClubMembershipMutation;

public interface ClubsDataAccessObject extends ClubReadOperations, ClubWriteOperations, ClubMembershipMutation {
    @Override
    void writeClub(long clubID, java.util.ArrayList<entity.Account> members, String name, String description, String imageUrl, java.util.ArrayList<entity.Post> posts, java.util.ArrayList<String> tags);
    // Consolidated interfaces: read (getClub, getAllClubs), write (writeClub, clubExists, deleteClub), membership mutation (removeMemberFromClub).
}
