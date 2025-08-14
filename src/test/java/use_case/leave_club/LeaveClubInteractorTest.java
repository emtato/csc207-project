package use_case.leave_club;

import data_access.*;
import entity.Account;
import entity.Club;
import entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LeaveClubInteractorTest {
    private LeaveClubInteractor interactor;
    private ClubsDataAccessObject clubsDAO;
    private UserDataAccessObject userDAO;
    private TestPresenter presenter;

    @BeforeEach
    void setup() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsDAO = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userDAO = InMemoryUserDataAccessObject.getInstance();
        // Reset singleton state (best-effort) to avoid test interference
        for (Club c : new ArrayList<>(clubsDAO.getAllClubs())) {
            clubsDAO.deleteClub(c.getId());
        }
        for (Account a : new ArrayList<>(userDAO.getAllUsers())) {
            userDAO.deleteAccount(a.getUsername());
        }
        presenter = new TestPresenter();
        interactor = new LeaveClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void leaveClubRemovesMembership() {
        Account user = new Account("carol", "pw");
        ArrayList<String> clubs = new ArrayList<>();
        clubs.add("500");
        user.setClubs(clubs);
        userDAO.save(user);

        ArrayList<Account> members = new ArrayList<>();
        members.add(user);
        clubsDAO.writeClub(500L, members, "TempClub", "desc", null, new ArrayList<Post>(), new ArrayList<>());

        interactor.execute(new LeaveClubInputData("carol", 500L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        assertTrue(presenter.out.getNonMemberClubs().stream().anyMatch(c -> c.getName().equals("TempClub")));
        assertFalse(presenter.out.getMemberClubs().stream().anyMatch(c -> c.getName().equals("TempClub")));
    }

    @Test
    void clubNotFoundTriggersFailView() {
        Account user = new Account("alice", "pw");
        userDAO.save(user);
        interactor.execute(new LeaveClubInputData("alice", 9999L));
        assertNull(presenter.out);
        assertEquals("Club not found", presenter.error);
    }

    @Test
    void userNotFoundTriggersFailView() {
        // Create club but do not create user
        clubsDAO.writeClub(77L, new ArrayList<>(), "LonelyClub", "desc", null, new ArrayList<>(), new ArrayList<>());
        interactor.execute(new LeaveClubInputData("ghost", 77L));
        assertNull(presenter.out);
        assertEquals("User not found", presenter.error);
    }

    @Test
    void nullUserClubListAndAnnouncementsExtraction() {
        // user is member of two clubs (leave one, stay in one). Remaining club has announcement posts.
        Account user = new Account("dave", "pw");
        ArrayList<String> clubs = new ArrayList<>();
        clubs.add("10"); // leaving this club
        clubs.add("11"); // staying member of this club
        user.setClubs(clubs);
        userDAO.save(user);

        ArrayList<Account> membersClub10 = new ArrayList<>();
        membersClub10.add(user);
        clubsDAO.writeClub(10L, membersClub10, "ClubLeave", "desc", null, new ArrayList<>(), new ArrayList<>());

        ArrayList<Account> membersClub11 = new ArrayList<>();
        membersClub11.add(user);
        // Posts: one announcement with irregular casing & spaces, one null post, one non-announcement
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post(user, 1L, "Ann1", "d", new ArrayList<>(), new HashMap<>(), " Announcement ", "2024-01-01 10:00 AM", new ArrayList<>()));
        posts.add(null);
        posts.add(new Post(user, 2L, "Other", "d", new ArrayList<>(), new HashMap<>(), "general", "2024-01-01 11:00 AM", new ArrayList<>()));
        clubsDAO.writeClub(11L, membersClub11, "ClubStay", "desc", null, posts, new ArrayList<>());

        // Force user clubs list to null to cover null branch
        user.setClubs(null);
        userDAO.save(user);

        interactor.execute(new LeaveClubInputData("dave", 10L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        // Since we nulled user clubs before, after leaving it should treat membership list as empty -> no member clubs
        assertTrue(presenter.out.getMemberClubs().isEmpty());
        // Non-member clubs should include both clubs now
        assertEquals(2, presenter.out.getNonMemberClubs().size());
        // Announcements list should be empty because user no longer member of ClubStay when rebuilding lists
        assertTrue(presenter.out.getAnnouncements().isEmpty());
    }

    @Test
    void exceptionDuringProcessingTriggersFailView() {
        // Custom DAO that throws when getAllClubs called
        class ExplodingClubsDAO implements ClubsDataAccessObject {
            @Override
            public void writeClub(long clubID, ArrayList<Account> members, String name, String description, String imageUrl, ArrayList<Post> posts, ArrayList<String> tags) { }
            @Override
            public Club getClub(long clubID) { return new Club("Name", "desc", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), clubID, new ArrayList<>()); }
            @Override
            public boolean clubExists(String clubName) { return false; }
            @Override
            public ArrayList<Club> getAllClubs() { throw new RuntimeException("boom"); }
            @Override
            public void removeMemberFromClub(String username, long clubID) { }
            @Override
            public void deleteClub(long clubID) { }
        }
        ClubsDataAccessObject explodingDAO = new ExplodingClubsDAO();
        LeaveClubInteractor explodingInteractor = new LeaveClubInteractor(explodingDAO, userDAO, presenter);
        Account user = new Account("erin", "pw");
        user.setClubs(new ArrayList<>(Collections.singletonList("1")));
        userDAO.save(user);
        explodingInteractor.execute(new LeaveClubInputData("erin", 1L));
        assertNull(presenter.out);
        assertNotNull(presenter.error);
        assertTrue(presenter.error.startsWith("Error leaving club:"));
    }

    @Test
    void leavingClubUserNotMemberDoesNothing() {
        // User belongs to club 600, attempts to leave club 601 where they are not a member.
        Account user = new Account("frank", "pw");
        user.setClubs(new ArrayList<>(Arrays.asList("600")));
        userDAO.save(user);
        // Club 600 (member) and Club 601 (not member of 601's member list)
        ArrayList<Account> members600 = new ArrayList<>();
        members600.add(user);
        clubsDAO.writeClub(600L, members600, "MemberClub", "desc", null, new ArrayList<>(), new ArrayList<>());
        clubsDAO.writeClub(601L, new ArrayList<>(), "OtherClub", "desc", null, new ArrayList<>(), new ArrayList<>());

        interactor.execute(new LeaveClubInputData("frank", 601L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        // Still member of 600, not member of 601 (never was). Member list of 600 should remain size 1.
        Club memberClub = presenter.out.getMemberClubs().stream().filter(c -> c.getId() == 600L).findFirst().orElse(null);
        assertNotNull(memberClub);
        assertEquals(1, memberClub.getMembers().size());
        // Non-member list should include 601.
        assertTrue(presenter.out.getNonMemberClubs().stream().anyMatch(c -> c.getId() == 601L));
    }

    @Test
    void leavingOneClubStillCollectsAnnouncementsFromRemainingClub() {
        Account user = new Account("gina", "pw");
        user.setClubs(new ArrayList<>(Arrays.asList("21", "22")));
        userDAO.save(user);

        // Club 21 (to leave) with membership including user
        ArrayList<Account> members21 = new ArrayList<>(); members21.add(user);
        clubsDAO.writeClub(21L, members21, "LeaveThis", "desc", null, new ArrayList<>(), new ArrayList<>());

        // Club 22 (stay in) with posts
        ArrayList<Account> members22 = new ArrayList<>(); members22.add(user);
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post(user, 100L, "StayAnn", "d", new ArrayList<>(), new HashMap<>(), "announcement", "2024-01-01 10:00 AM", new ArrayList<>()));
        posts.add(new Post(user, 101L, "NullType", "d", new ArrayList<>(), new HashMap<>(), null, "2024-01-01 10:05 AM", new ArrayList<>()));
        posts.add(null); // null post
        posts.add(new Post(user, 102L, "General", "d", new ArrayList<>(), new HashMap<>(), "general", "2024-01-01 10:10 AM", new ArrayList<>()));
        clubsDAO.writeClub(22L, members22, "StayClub", "desc", null, posts, new ArrayList<>());

        interactor.execute(new LeaveClubInputData("gina", 21L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        // Should remain member of club 22 only
        assertEquals(1, presenter.out.getMemberClubs().size());
        assertEquals(22L, presenter.out.getMemberClubs().get(0).getId());
        // Announcements list should contain exactly the announcement post (ID 100)
        assertEquals(1, presenter.out.getAnnouncements().size());
        assertEquals(100L, presenter.out.getAnnouncements().get(0).getID());
    }

    @Test
    void userClubListUpdatedAfterLeaving() {
        // Arrange
        Account user = new Account("henry", "pw");
        ArrayList<String> initialClubs = new ArrayList<>();
        initialClubs.add("900");
        initialClubs.add("901");
        user.setClubs(initialClubs);
        userDAO.save(user);

        ArrayList<Account> members900 = new ArrayList<>(); members900.add(user);
        ArrayList<Account> members901 = new ArrayList<>(); members901.add(user);
        clubsDAO.writeClub(900L, members900, "LeaveTarget", "d", null, new ArrayList<Post>(), new ArrayList<>());
        clubsDAO.writeClub(901L, members901, "StayClub", "d", null, new ArrayList<Post>(), new ArrayList<>());

        // Act
        interactor.execute(new LeaveClubInputData("henry", 900L));

        // Assert
        Account refreshed = (Account) userDAO.get("henry");
        assertNotNull(refreshed);
        ArrayList<String> updated = refreshed.getClubs();
        assertNotNull(updated, "User clubs list should not be null");
        assertFalse(updated.contains("900"), "Club 900 should be removed from user's club list");
        assertTrue(updated.contains("901"), "Other club membership should be preserved");
        Club leftClub = clubsDAO.getClub(900L);
        assertNotNull(leftClub);
        assertTrue(leftClub.getMembers().stream().noneMatch(a -> a.getUsername().equals("henry")), "User should no longer appear in club member list");
    }

    private static class TestPresenter implements LeaveClubOutputBoundary {
        LeaveClubOutputData out; String error;
        public void prepareSuccessView(LeaveClubOutputData outputData) { this.out = outputData; }
        public void prepareFailView(String error) { this.error = error; }
    }
}
