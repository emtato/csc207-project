package use_case.specific_club;

import data_access.*;
import entity.Account;
import entity.Club;
import entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpecificClubInteractorTest {
    private SpecificClubInteractor specificClubInteractor;
    private ClubsDataAccessObject clubsDAO;
    private UserDataAccessObject userDAO;
    private TestSpecificClubPresenter presenter;

    @BeforeEach
    void setUp() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsDAO = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userDAO = InMemoryUserDataAccessObject.getInstance();
        // reset in-memory singleton state
        for (Club c : new ArrayList<>(clubsDAO.getAllClubs())) { clubsDAO.deleteClub(c.getId()); }
        for (Account a : new ArrayList<>(userDAO.getAllUsers())) { userDAO.deleteAccount(a.getUsername()); }
        presenter = new TestSpecificClubPresenter();
        specificClubInteractor = new SpecificClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void executeSuccess() {
        // Setup test data
        long clubId = 1L;
        clubsDAO.writeClub(clubId, new ArrayList<>(), "Test Club", "Description", "img", new ArrayList<>(), new ArrayList<>());

        // Execute test
        specificClubInteractor.execute(new SpecificClubInputData(clubId, "Test Club", "Description"));

        // Verify results
        assertTrue(presenter.getLastOutput().isSuccess());
        assertEquals("Test Club", presenter.getLastOutput().getClub().getName());
    }

    @Test
    void executeClubNotFound() {
        specificClubInteractor.execute(new SpecificClubInputData(999L, "Missing", ""));
        assertNull(presenter.getLastOutput());
        assertEquals("Club not found", presenter.getLastError());
    }

    @Test
    void executeExceptionCaught() {
        // DAO that throws in getClub
        class ThrowingClubsDAO implements ClubsDataAccessObject {
            public void writeClub(long c, ArrayList<Account> m, String n, String d, String imageUrl, ArrayList<Post> p, ArrayList<String> t) {}
            public Club getClub(long id) { throw new RuntimeException("boom"); }
            public boolean clubExists(String name) { return false; }
            public ArrayList<entity.Club> getAllClubs() { return new ArrayList<>(); }
            public void removeMemberFromClub(String u, long c) {}
            public void deleteClub(long c) {}
        }
        SpecificClubInteractor throwing = new SpecificClubInteractor(new ThrowingClubsDAO(), userDAO, presenter);
        throwing.execute(new SpecificClubInputData(5L, "X", "Y"));
        assertNull(presenter.getLastOutput());
        assertTrue(presenter.getLastError().startsWith("Error loading club:"));
    }

    @Test
    void loadClubSuccess() {
        // Setup test data
        long clubId = 2L;
        clubsDAO.writeClub(clubId, new ArrayList<>(), "Load Club", "Desc", "img", new ArrayList<>(), new ArrayList<>());

        // Execute test
        specificClubInteractor.loadClub(clubId);

        // Verify results
        assertTrue(presenter.getLastOutput().isSuccess());
        assertEquals("Load Club", presenter.getLastOutput().getClub().getName());
    }

    @Test
    void loadClubNotFound() {
        specificClubInteractor.loadClub(12345L);
        assertNull(presenter.getLastOutput());
        assertEquals("Club not found", presenter.getLastError());
    }

    @Test
    void fetchAnnouncementsDelegatesSuccess() {
        // Setup test data
        long clubId = 3L;
        clubsDAO.writeClub(clubId, new ArrayList<>(), "Ann Club", "Desc", "img", new ArrayList<>(), new ArrayList<>());

        // Execute test
        specificClubInteractor.fetchAnnouncements(clubId);

        // Verify results
        assertNotNull(presenter.getLastOutput());
        assertEquals("Ann Club", presenter.getLastOutput().getClub().getName());
    }

    @Test
    void fetchAnnouncementsClubNotFound() {
        specificClubInteractor.fetchAnnouncements(5678L);
        assertNull(presenter.getLastOutput());
        assertEquals("Club not found", presenter.getLastError());
    }

    @Test
    void fetchPostsDelegatesSuccess() {
        // Setup test data
        long clubId = 4L;
        clubsDAO.writeClub(clubId, new ArrayList<>(), "Posts Club", "Desc", "img", new ArrayList<>(), new ArrayList<>());

        // Execute test
        specificClubInteractor.fetchPosts(clubId);

        // Verify results
        assertNotNull(presenter.getLastOutput());
        assertEquals("Posts Club", presenter.getLastOutput().getClub().getName());
    }

    @Test
    void fetchPostsClubNotFound() {
        specificClubInteractor.fetchPosts(99999L);
        assertNull(presenter.getLastOutput());
        assertEquals("Club not found", presenter.getLastError());
    }

    @Test
    void isMemberTrue() {
        // Setup test data
        long clubId = 10L;
        Account testUser = new Account("testUser", "password");
        ArrayList<Account> members = new ArrayList<>(); members.add(testUser);
        clubsDAO.writeClub(clubId, members, "Member Club", "Desc", "img", new ArrayList<>(), new ArrayList<>());

        // Execute test and verify
        assertTrue(specificClubInteractor.isMember("testUser", clubId));
    }

    @Test
    void isMemberFalseUserNotInClub() {
        // Setup test data
        long clubId = 11L;
        clubsDAO.writeClub(clubId, new ArrayList<>(), "Empty Club", "Desc", "img", new ArrayList<>(), new ArrayList<>());

        // Execute test and verify
        assertFalse(specificClubInteractor.isMember("other", clubId));
    }

    @Test
    void isMemberFalseClubNotFound() {
        // Execute test and verify
        assertFalse(specificClubInteractor.isMember("any", 123456L));
    }

    @Test
    void isMemberExceptionReturnsFalse() {
        class ThrowingClubsDAO implements ClubsDataAccessObject {
            public void writeClub(long c, ArrayList<Account> m, String n, String d, String imageUrl, ArrayList<Post> p, ArrayList<String> t) {}
            public Club getClub(long id) { throw new RuntimeException("boom"); }
            public boolean clubExists(String name) { return false; }
            public ArrayList<entity.Club> getAllClubs() { return new ArrayList<>(); }
            public void removeMemberFromClub(String u, long c) {}
            public void deleteClub(long c) {}
        }
        SpecificClubInteractor throwing = new SpecificClubInteractor(new ThrowingClubsDAO(), userDAO, presenter);
        assertFalse(throwing.isMember("user", 77L));
    }

    private static class TestSpecificClubPresenter implements SpecificClubOutputBoundary {
        private SpecificClubOutputData lastOutput; private String lastError;

        public void prepareSuccessView(SpecificClubOutputData outputData) {
            this.lastOutput = outputData;
            this.lastError = null;
        }

        public void prepareFailView(String error) {
            this.lastOutput = null;
            this.lastError = error;
        }

        public SpecificClubOutputData getLastOutput() {
            return lastOutput;
        }

        public String getLastError() {
            return lastError;
        }
    }
}
