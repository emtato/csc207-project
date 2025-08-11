package use_case.clubs_home;

import entity.Club;
import entity.Post;
import java.util.ArrayList;

public class ClubOutputData {
    private final ArrayList<Club> memberClubs;
    private final ArrayList<Club> nonMemberClubs;
    private final ArrayList<Post> announcements;
    private final boolean success;
    private final String error;

    public ClubOutputData(ArrayList<Club> memberClubs, ArrayList<Club> nonMemberClubs,
                         ArrayList<Post> announcements, boolean success, String error) {
        this.memberClubs = memberClubs;
        this.nonMemberClubs = nonMemberClubs;
        this.announcements = announcements;
        this.success = success;
        this.error = error;
    }

    public ArrayList<Club> getMemberClubs() {
        return memberClubs;
    }

    public ArrayList<Club> getNonMemberClubs() {
        return nonMemberClubs;
    }

    public ArrayList<Post> getAnnouncements() {
        return announcements;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
