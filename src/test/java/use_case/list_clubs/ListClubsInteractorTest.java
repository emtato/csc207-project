package use_case.list_clubs;

import data_access.*;
import entity.Account;
import entity.Club;
import entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ListClubsInteractorTest {
    private ListClubsInteractor interactor;
    private ClubsDataAccessObject clubsDAO;
    private UserDataAccessObject userDAO;
    private TestPresenter presenter;

    @BeforeEach
    void setup() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsDAO = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userDAO = InMemoryUserDataAccessObject.getInstance();
        // reset in-memory singleton state
        for (Club c : new ArrayList<>(clubsDAO.getAllClubs())) { clubsDAO.deleteClub(c.getId()); }
        for (Account a : new ArrayList<>(userDAO.getAllUsers())) { userDAO.deleteAccount(a.getUsername()); }
        // Clear static caches in ListClubsInteractor via reflection for test isolation
        try {
            Field cacheField = ListClubsInteractor.class.getDeclaredField("CACHE");
            cacheField.setAccessible(true);
            ((Map<?,?>) cacheField.get(null)).clear();
            Field lastFetchField = ListClubsInteractor.class.getDeclaredField("LAST_FETCH");
            lastFetchField.setAccessible(true);
            ((Map<?,?>) lastFetchField.get(null)).clear();
        } catch (Exception ignored) { }
        presenter = new TestPresenter();
        interactor = new ListClubsInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void listClubsSeparatesMembership() {
        Account a = new Account("alice", "pw");
        userDAO.save(a);
        ArrayList<Account> members1 = new ArrayList<>(); members1.add(a);
        clubsDAO.writeClub(100L, members1, "MemberClub", "desc", null, new ArrayList<Post>(), new ArrayList<>());
        clubsDAO.writeClub(200L, new ArrayList<>(), "OtherClub", "desc", null, new ArrayList<Post>(), new ArrayList<>());
        interactor.execute(new ListClubsInputData("alice"));
        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        assertNull(presenter.out.getError());
        assertEquals(1, presenter.out.getMemberClubs().size());
        assertEquals("MemberClub", presenter.out.getMemberClubs().get(0).getName());
        assertEquals(1, presenter.out.getNonMemberClubs().size());
    }

    @Test
    void userNotFoundTriggersFailView() {
        interactor.execute(new ListClubsInputData("ghost"));
        assertNull(presenter.out);
        assertEquals("User not found", presenter.error);
    }

    @Test
    void nullUserClubListInitialization() {
        Account user = new Account("bob", "pw");
        user.setClubs(null); // force null path
        userDAO.save(user);
        clubsDAO.writeClub(300L, new ArrayList<>(), "SomeClub", "d", null, new ArrayList<>(), new ArrayList<>());
        interactor.execute(new ListClubsInputData("bob"));
        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        assertTrue(userDAO.get("bob") instanceof Account);
        assertNotNull(((Account) userDAO.get("bob")).getClubs());
    }

    @Test
    void announcementsCollectedAndFiltered() {
        Account user = new Account("carol", "pw"); userDAO.save(user);
        ArrayList<Account> memberList = new ArrayList<>(); memberList.add(user);
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post(user, 1L, "Ann1", "d", new ArrayList<>(), new HashMap<>(), "announcement", "2024-01-01 09:00 AM", new ArrayList<>()));
        posts.add(null);
        posts.add(new Post(user, 2L, "Other", "d", new ArrayList<>(), new HashMap<>(), "general", "2024-01-01 09:05 AM", new ArrayList<>()));
        posts.add(new Post(user, 3L, "Ann2", "d", new ArrayList<>(), new HashMap<>(), " Announcement ", "2024-01-01 09:10 AM", new ArrayList<>()));
        clubsDAO.writeClub(400L, memberList, "PostClub", "d", null, posts, new ArrayList<>());
        ArrayList<Post> otherPosts = new ArrayList<>();
        otherPosts.add(new Post(user, 4L, "AnnOther", "d", new ArrayList<>(), new HashMap<>(), "announcement", "2024-01-01 09:15 AM", new ArrayList<>()));
        clubsDAO.writeClub(401L, new ArrayList<>(), "Other", "d", null, otherPosts, new ArrayList<>());
        interactor.execute(new ListClubsInputData("carol"));
        assertNotNull(presenter.out);
        assertEquals(2, presenter.out.getAnnouncements().size());
        long countIds = presenter.out.getAnnouncements().stream().filter(p -> p.getID()==1L || p.getID()==3L).count();
        assertEquals(2, countIds);
    }

    @Test
    void reconstructionAddsMissingClubIdsAndPersists() {
        Account user = new Account("eve", "pw");
        user.setClubs(new ArrayList<>()); // empty but user is member of a club => reconstruction
        userDAO.save(user);
        ArrayList<Account> members = new ArrayList<>(); members.add(user);
        clubsDAO.writeClub(501L, members, "ReconClub", "desc", null, new ArrayList<>(), new ArrayList<>());
        interactor.execute(new ListClubsInputData("eve"));
        assertNotNull(presenter.out);
        assertTrue(presenter.out.getMemberClubs().stream().anyMatch(c -> c.getId()==501L));
        Account refreshed = (Account) userDAO.get("eve");
        assertTrue(refreshed.getClubs().contains("501"));
    }

    @Test
    void throttleReturnsCachedDataWithMessage() throws InterruptedException {
        Account user = new Account("fred", "pw"); userDAO.save(user);
        clubsDAO.writeClub(601L, new ArrayList<>(), "C1", "d", null, new ArrayList<>(), new ArrayList<>());
        interactor.execute(new ListClubsInputData("fred"));
        assertNotNull(presenter.out);
        presenter.reset();
        interactor.execute(new ListClubsInputData("fred")); // within throttle interval
        assertNotNull(presenter.out);
        assertEquals("(throttled)", presenter.out.getError());
    }

    @Test
    void throttledWithoutCacheFallsThroughToFreshFetch() {
        Account user = new Account("gina", "pw"); userDAO.save(user);
        clubsDAO.writeClub(700L, new ArrayList<>(), "TClub", "d", null, new ArrayList<>(), new ArrayList<>());
        interactor.execute(new ListClubsInputData("gina")); // populate cache and last fetch
        // Remove cache entry but keep last fetch timestamp via reflection
        try {
            Field cacheField = ListClubsInteractor.class.getDeclaredField("CACHE");
            cacheField.setAccessible(true);
            ((Map<?,?>) cacheField.get(null)).clear();
        } catch (Exception ignored) { }
        presenter.reset();
        interactor.execute(new ListClubsInputData("gina"));
        assertNotNull(presenter.out);
        assertNull(presenter.out.getError()); // fresh fetch, not throttled msg
    }

    @Test
    void rateLimitedUsesCachedData() {
        Account user = new Account("henry", "pw"); userDAO.save(user);
        clubsDAO.writeClub(800L, new ArrayList<>(), "BaseClub", "d", null, new ArrayList<>(), new ArrayList<>());
        interactor.execute(new ListClubsInputData("henry")); // populate cache
        assertNotNull(presenter.out);
        // Interactor with DAO that throws rate limit error
        class RateLimitDAO implements ClubsDataAccessObject {
            public void writeClub(long id, ArrayList<Account> m, String n, String d, String imageUrl, ArrayList<Post> p, ArrayList<String> t) {}
            public Club getClub(long id) { throw new RuntimeException("Too Many Requests from server"); }
            public boolean clubExists(String name) { return false; }
            public ArrayList<Club> getAllClubs() { throw new RuntimeException("Too Many Requests"); }
            public void removeMemberFromClub(String username, long clubID) {}
            public void deleteClub(long clubID) {}
        }
        presenter.reset();
        // Force bypass of throttle by backdating LAST_FETCH entry
        try {
            Field lastFetchField = ListClubsInteractor.class.getDeclaredField("LAST_FETCH");
            lastFetchField.setAccessible(true);
            @SuppressWarnings("unchecked") Map<String, Long> lastFetch = (Map<String, Long>) lastFetchField.get(null);
            lastFetch.put("henry", System.currentTimeMillis() - 10_000L); // older than MIN_INTERVAL_MS
        } catch (Exception ignored) { }
        ListClubsInteractor rateLimited = new ListClubsInteractor(new RateLimitDAO(), userDAO, presenter);
        rateLimited.execute(new ListClubsInputData("henry"));
        assertNotNull(presenter.out);
        assertEquals("Using cached data (rate limited)", presenter.out.getError());
    }

    @Test
    void exceptionHandlingShowsFailView() {
        Account user = new Account("dave", "pw"); userDAO.save(user);
        class ExplodingClubsDAO implements ClubsDataAccessObject {
            public void writeClub(long id, ArrayList<Account> m, String n, String d, String imageUrl, ArrayList<Post> p, ArrayList<String> t) {}
            public Club getClub(long id) { return null; }
            public boolean clubExists(String name) { return false; }
            public ArrayList<Club> getAllClubs() { throw new RuntimeException("boom"); }
            public void removeMemberFromClub(String username, long clubID) {}
            public void deleteClub(long clubID) {}
        }
        ListClubsInteractor exploding = new ListClubsInteractor(new ExplodingClubsDAO(), userDAO, presenter);
        exploding.execute(new ListClubsInputData("dave"));
        assertNull(presenter.out);
        assertTrue(presenter.error.startsWith("Error fetching clubs data:"));
    }

    private static class TestPresenter implements ListClubsOutputBoundary {
        ListClubsOutputData out; String error;
        public void prepareSuccessView(ListClubsOutputData outputData) { this.out = outputData; }
        public void prepareFailView(String error) { this.error = error; }
        void reset() { out = null; error = null; }
    }
}
