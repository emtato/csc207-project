package data_access;

import entity.Club;
import use_case.create_club.CreateClubClubsDataAccessInterface;

import java.util.ArrayList;

public interface ClubsDataAccessObject extends CreateClubClubsDataAccessInterface {

    // Methods specific to broader club management beyond Create Club use case
    @Override
    void writeClub(long clubID, java.util.ArrayList<entity.Account> members, String name, String description, String imageUrl, java.util.ArrayList<entity.Post> posts, java.util.ArrayList<String> tags);

    ArrayList<Club> getAllClubs();
    void removeMemberFromClub(String username, long clubID);
    void deleteClub(long clubID);
}
