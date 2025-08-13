package use_case.delete_club;

import entity.Club;
import entity.Post;
import java.util.ArrayList;

public class DeleteClubOutputData {
    private final ArrayList<Club> memberClubs;
    private final ArrayList<Club> nonMemberClubs;
    private final ArrayList<Post> announcements;
    private final boolean success;
    private final String message;

    public DeleteClubOutputData(ArrayList<Club> memberClubs,
                                ArrayList<Club> nonMemberClubs,
                                ArrayList<Post> announcements,
                                boolean success,
                                String message) {
        this.memberClubs = memberClubs;
        this.nonMemberClubs = nonMemberClubs;
        this.announcements = announcements;
        this.success = success;
        this.message = message;
    }

    public ArrayList<Club> getMemberClubs() { return memberClubs; }
    public ArrayList<Club> getNonMemberClubs() { return nonMemberClubs; }
    public ArrayList<Post> getAnnouncements() { return announcements; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}

