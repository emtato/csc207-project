package use_case.club;

import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility to derive membership partition and announcement posts for a user from a list of clubs.
 * Pure (no side effects) to enable reuse across join / leave / list / delete operations.
 */
public final class ClubAnnouncementCollector {
    private ClubAnnouncementCollector() {}

    public static ClubAnnouncementResult collect(Account user, List<Club> allClubs) {
        ArrayList<Club> member = new ArrayList<>();
        ArrayList<Club> nonMember = new ArrayList<>();
        ArrayList<Post> announcements = new ArrayList<>();
        if (user == null || allClubs == null) {
            return new ClubAnnouncementResult(member, nonMember, announcements);
        }
        List<String> userClubIds = user.getClubs();
        for (Club c : allClubs) {
            if (c == null) continue;
            String cid = String.valueOf(c.getId());
            boolean isMember = userClubIds != null && userClubIds.contains(cid);
            if (isMember) {
                member.add(c);
                if (c.getPosts() != null) {
                    for (Post p : c.getPosts()) {
                        if (p != null && p.getType() != null && p.getType().trim().equalsIgnoreCase("announcement")) {
                            announcements.add(p);
                        }
                    }
                }
            } else {
                nonMember.add(c);
            }
        }
        return new ClubAnnouncementResult(member, nonMember, announcements);
    }
}
