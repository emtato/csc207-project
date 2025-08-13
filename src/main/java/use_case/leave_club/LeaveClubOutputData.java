package use_case.leave_club;

import entity.Club;
import entity.Post;
import java.util.ArrayList;

public class LeaveClubOutputData {
    private final Club club; // club that was left
    private final ArrayList<Club> memberClubs;
    private final ArrayList<Club> nonMemberClubs;
    private final ArrayList<Post> announcements;
    private final boolean success;
    private final String error;

    public LeaveClubOutputData(Club club,
                               ArrayList<Club> memberClubs,
                               ArrayList<Club> nonMemberClubs,
                               ArrayList<Post> announcements,
                               boolean success,
                               String error) {
        this.club = club;
        this.memberClubs = memberClubs;
        this.nonMemberClubs = nonMemberClubs;
        this.announcements = announcements;
        this.success = success;
        this.error = error;
    }

    public Club getClub() { return club; }
    public ArrayList<Club> getMemberClubs() { return memberClubs; }
    public ArrayList<Club> getNonMemberClubs() { return nonMemberClubs; }
    public ArrayList<Post> getAnnouncements() { return announcements; }
    public boolean isSuccess() { return success; }
    public String getError() { return error; }
}
