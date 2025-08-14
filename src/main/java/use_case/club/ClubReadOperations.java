package use_case.club;

import entity.Club;
import java.util.ArrayList;

/**
 * Domain-level read operations for clubs (single + bulk lookup).
 */
public interface ClubReadOperations {
    Club getClub(long clubID);
    ArrayList<Club> getAllClubs();
}

