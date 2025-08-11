package interface_adapter.clubs_home;

import entity.Account;
import entity.Club;
import entity.Post;
import java.util.ArrayList;

/**
 * The state for the Club View Model.
 */
public class ClubState {
    private ArrayList<Club> memberClubs = new ArrayList<>();
    private ArrayList<Club> nonMemberClubs = new ArrayList<>();
    private ArrayList<Post> announcements = new ArrayList<>();
    private Account currentUser;
    private String error = null;

    public ClubState(ClubState copy) {
        memberClubs = copy.memberClubs;
        nonMemberClubs = copy.nonMemberClubs;
        announcements = copy.announcements;
        currentUser = copy.currentUser;
        error = copy.error;
    }

    // Default constructor
    public ClubState() {}

    public ArrayList<Club> getMemberClubs() {
        return memberClubs;
    }

    public void setMemberClubs(ArrayList<Club> memberClubs) {
        this.memberClubs = memberClubs;
    }

    public ArrayList<Club> getNonMemberClubs() {
        return nonMemberClubs;
    }

    public void setNonMemberClubs(ArrayList<Club> nonMemberClubs) {
        this.nonMemberClubs = nonMemberClubs;
    }

    public ArrayList<Post> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<Post> announcements) {
        this.announcements = announcements;
    }

    public Account getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Account currentUser) {
        this.currentUser = currentUser;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
