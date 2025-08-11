package data_access;

import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;

public interface ClubsDataAccessObject{

    public void writeClub(long clubID, ArrayList<Account> members, String name, String description, ArrayList<Post> posts, ArrayList<String> tags);
    public Club getClub(long clubID);
    public ArrayList<Club> getAllClubs();
    public boolean clubExists(String clubName);
    public void removeMemberFromClub(String username, long clubID);
}
