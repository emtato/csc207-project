package use_case.club;

import entity.Club;
import entity.Post;
import java.util.List;

/**
 * Result DTO for ClubAnnouncementCollector (separated because Java 15 lacks record support).
 * Accessor method names mimic those that a record would generate for compatibility.
 */
public final class ClubAnnouncementResult {
    private final List<Club> memberClubs;
    private final List<Club> nonMemberClubs;
    private final List<Post> announcements;

    public ClubAnnouncementResult(List<Club> memberClubs, List<Club> nonMemberClubs, List<Post> announcements) {
        this.memberClubs = memberClubs;
        this.nonMemberClubs = nonMemberClubs;
        this.announcements = announcements;
    }

    public List<Club> memberClubs() { return memberClubs; }
    public List<Club> nonMemberClubs() { return nonMemberClubs; }
    public List<Post> announcements() { return announcements; }
}

