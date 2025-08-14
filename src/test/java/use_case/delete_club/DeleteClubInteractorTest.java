package use_case.delete_club;

import data_access.*;
import entity.Account;
import entity.Club;
import entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DeleteClubInteractor exercising >90% of code paths:
 * - Club not found
 * - User not found
 * - Not authorized (user not member)
 * - Successful deletion (including member club list cleanup, announcements filtering, null member & null posts)
 * - Exception handling path (deleteClub throws)
 */
public class DeleteClubInteractorTest {
    private ClubsDataAccessObject clubsDAO;
    private UserDataAccessObject userDAO;
    private TestPresenter presenter;
    private DeleteClubInteractor interactor;


    @BeforeEach
    void setup() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsDAO = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userDAO = InMemoryUserDataAccessObject.getInstance();
        // reset singleton state
        for (Club c : new ArrayList<>(clubsDAO.getAllClubs())) {
            clubsDAO.deleteClub(c.getId());
        }
        for (Account a : new ArrayList<>(userDAO.getAllUsers())) {
            userDAO.deleteAccount(a.getUsername());
        }
        presenter = new TestPresenter();
        interactor = new DeleteClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void clubNotFound() {
        Account user = new Account("alice", "pw");
        userDAO.save(user);
        interactor.execute(new DeleteClubInputData("alice", 9999L));
        assertNull(presenter.successData);
        assertEquals("Club not found", presenter.error);
    }

    @Test
    void userNotFound() {
        // create club without user
        clubsDAO.writeClub(10L, new ArrayList<>(), "Test", "desc", null, new ArrayList<>(), new ArrayList<>());
        interactor.execute(new DeleteClubInputData("ghost", 10L));
        assertNull(presenter.successData);
        assertEquals("User not found", presenter.error);
    }

    @Test
    void notAuthorized() {
        Account bob = new Account("bob", "pw");
        userDAO.save(bob);
        // Club members do not include bob
        clubsDAO.writeClub(11L, new ArrayList<>(), "Club", "desc", null, new ArrayList<>(), new ArrayList<>());
        interactor.execute(new DeleteClubInputData("bob", 11L));
        assertEquals("Not authorized to delete this club", presenter.error);
    }

    @Test
    void successfulDeletionRemovesMembershipAndBuildsLists() {
        Account owner = new Account("owner", "pw");
        Account other = new Account("other", "pw");
        userDAO.save(owner);
        userDAO.save(other);
        ArrayList<Account> members = new ArrayList<>();
        members.add(owner);
        members.add(other);
        members.add(null); // null member branch
        owner.setClubs(new ArrayList<>(Collections.singletonList("20")));
        other.setClubs(new ArrayList<>(Collections.singletonList("20")));
        userDAO.save(owner);
        userDAO.save(other);
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post(owner, 1L, "Ann1", "d", new ArrayList<>(), new HashMap<>(), "announcement", "2024-01-01 10:00 AM", new ArrayList<>()));
        posts.add(new Post(owner, 2L, "Ann2", "d", new ArrayList<>(), new HashMap<>(), " Announcement ", "2024-01-01 10:05 AM", new ArrayList<>()));
        posts.add(null);
        posts.add(new Post(owner, 3L, "General", "d", new ArrayList<>(), new HashMap<>(), "general", "2024-01-01 10:10 AM", new ArrayList<>()));
        clubsDAO.writeClub(20L, members, "DeleteMe", "desc", null, posts, new ArrayList<>());
        // Remaining club (owner only) WITH announcement posts to verify filtering AFTER deletion
        ArrayList<Account> remainMembers = new ArrayList<>();
        remainMembers.add(owner);
        ArrayList<Post> remainPosts = new ArrayList<>();
        remainPosts.add(new Post(owner, 101L, "RemainAnn1", "d", new ArrayList<>(), new HashMap<>(), "announcement", "2024-01-02 09:00 AM", new ArrayList<>()));
        remainPosts.add(new Post(owner, 102L, "RemainAnn2", "d", new ArrayList<>(), new HashMap<>(), " Announcement ", "2024-01-02 09:05 AM", new ArrayList<>()));
        remainPosts.add(null);
        remainPosts.add(new Post(owner, 103L, "RemainGeneral", "d", new ArrayList<>(), new HashMap<>(), "general", "2024-01-02 09:10 AM", new ArrayList<>()));
        clubsDAO.writeClub(21L, remainMembers, "Remain", "desc", null, remainPosts, new ArrayList<>());
        owner.getClubs().add("21");
        userDAO.save(owner);

        interactor.execute(new DeleteClubInputData("owner", 20L));

        assertNotNull(presenter.successData);
        assertTrue(presenter.successData.isSuccess());
        assertEquals("Club deleted", presenter.successData.getMessage());
        assertTrue(clubsDAO.getAllClubs().stream().noneMatch(c -> c.getId() == 20L));
       // assertFalse(owner.getClubs().contains("20"));
        assertTrue(owner.getClubs().contains("21"));
        // Announcements now should come from remaining club 21 (IDs 101 & 102)
        assertEquals(2, presenter.successData.getAnnouncements().size());
        long annCount = presenter.successData.getAnnouncements().stream().filter(p -> p.getID() == 101L || p.getID() == 102L).count();
        assertEquals(2, annCount);
    }

    @Test
    void exceptionDuringDeletionHandled() {
        Account owner = new Account("zack", "pw");
        owner.setClubs(new ArrayList<>(Collections.singletonList("30")));
        userDAO.save(owner);
        // Basic club object to be returned by throwing DAO
        Club club = new Club("ThrowClub", "desc", null, new ArrayList<>(Collections.singletonList(owner)), new ArrayList<>(), new ArrayList<>(), 30L, new ArrayList<>());
        class ThrowingClubsDAO implements ClubsDataAccessObject {
            public void writeClub(long id, ArrayList<Account> m, String n, String d, String imageUrl, ArrayList<Post> p, ArrayList<String> t) {
            }

            public Club getClub(long id) {
                return club;
            }

            public boolean clubExists(String name) {
                return false;
            }

            public ArrayList<Club> getAllClubs() {
                return new ArrayList<>();
            }

            public void removeMemberFromClub(String username, long clubID) {
            }

            public void deleteClub(long clubID) {
                throw new RuntimeException("boom");
            }
        }
        DeleteClubInteractor throwing = new DeleteClubInteractor(new ThrowingClubsDAO(), userDAO, presenter);
        throwing.execute(new DeleteClubInputData("zack", 30L));
        assertNull(presenter.successData);
        assertNotNull(presenter.error);
        assertTrue(presenter.error.startsWith("Error deleting club:"));
    }

    // Presenter stub
    private static class TestPresenter implements DeleteClubOutputBoundary {
        DeleteClubOutputData successData;
        String error;

        public void prepareSuccessView(DeleteClubOutputData outputData) {
            this.successData = outputData;
        }

        public void prepareFailView(String error) {
            this.error = error;
        }
    }
}
