package use_case.list_clubs;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListClubsInteractor implements ListClubsInputBoundary {
    private final ClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private final ListClubsOutputBoundary presenter;

    private static final Map<String, ListClubsOutputData> CACHE = new HashMap<>();
    private static final Map<String, Long> LAST_FETCH = new HashMap<>();
    private static final long MIN_INTERVAL_MS = 1500; // throttle repeated fetches

    public ListClubsInteractor(ClubsDataAccessObject clubsDataAccessObject,
                               UserDataAccessObject userDataAccessObject,
                               ListClubsOutputBoundary presenter) {
        this.clubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(ListClubsInputData inputData) {
        final String username = inputData.getUsername();
        long now = System.currentTimeMillis();
        Long last = LAST_FETCH.get(username);
        if (last != null && now - last < MIN_INTERVAL_MS) {
            // Throttled: serve cached data if available
            ListClubsOutputData cached = CACHE.get(username);
            if (cached != null) {
                presenter.prepareSuccessView(new ListClubsOutputData(
                        cached.getMemberClubs(),
                        cached.getNonMemberClubs(),
                        cached.getAnnouncements(),
                        true,
                        "(throttled)"
                ));
                return;
            }
            // else continue to attempt fetch
        }
        try {
            Account currentUser = (Account) userDataAccessObject.get(username);
            if (currentUser == null) {
                presenter.prepareFailView("User not found");
                return;
            }
            ArrayList<String> userClubIds = currentUser.getClubs();
            if (userClubIds == null) {
                userClubIds = new ArrayList<>();
                // Avoid immediate save (network) just for empty list; defer until we actually reconstruct
                currentUser.setClubs(userClubIds);
            }

            boolean reconstructed = false;
            if (userClubIds.isEmpty()) {
                ArrayList<Club> scan = clubsDataAccessObject.getAllClubs();
                for (Club c : scan) {
                    if (c.getMembers().stream().anyMatch(m -> m.getUsername().equals(username))) {
                        String cid = String.valueOf(c.getId());
                        if (!userClubIds.contains(cid)) {
                            userClubIds.add(cid);
                            reconstructed = true;
                        }
                    }
                }
                if (reconstructed) {
                    // Persist only if changed to reduce writes
                    userDataAccessObject.save(currentUser);
                }
            }

            ArrayList<Club> allClubs = clubsDataAccessObject.getAllClubs();
            ArrayList<Club> memberClubs = new ArrayList<>();
            ArrayList<Club> nonMemberClubs = new ArrayList<>();
            ArrayList<Post> announcements = new ArrayList<>();

            for (Club club : allClubs) {
                String cid = String.valueOf(club.getId());
                if (userClubIds.contains(cid)) {
                    memberClubs.add(club);
                    ArrayList<Post> clubPosts = club.getPosts();
                    if (clubPosts != null) {
                        for (Post p : clubPosts) {
                            if (p != null && p.getType() != null && p.getType().trim().equalsIgnoreCase("announcement")) {
                                announcements.add(p);
                            }
                        }
                    }
                } else {
                    nonMemberClubs.add(club);
                }
            }
            ListClubsOutputData out = new ListClubsOutputData(memberClubs, nonMemberClubs, announcements, true, null);
            CACHE.put(username, out);
            LAST_FETCH.put(username, now);
            presenter.prepareSuccessView(out);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.toLowerCase().contains("too many requests")) {
                ListClubsOutputData cached = CACHE.get(username);
                if (cached != null) {
                    presenter.prepareSuccessView(new ListClubsOutputData(
                            cached.getMemberClubs(),
                            cached.getNonMemberClubs(),
                            cached.getAnnouncements(),
                            true,
                            "Using cached data (rate limited)"
                    ));
                    return;
                }
            }
            presenter.prepareFailView("Error fetching clubs data: " + e.getMessage());
        }
    }
}
