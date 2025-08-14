package use_case.list_clubs;

import data_access.*;
import entity.Account;
import entity.Club;
import entity.Post;
import interface_adapter.clubs_home.ClubViewModel; // only for state shape if needed (not used here)
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

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
        // reset singleton state
        for (Club c : new ArrayList<>(clubsDAO.getAllClubs())) { clubsDAO.deleteClub(c.getId()); }
        for (Account a : new ArrayList<>(userDAO.getAllUsers())) { userDAO.deleteAccount(a.getUsername()); }
        presenter = new TestPresenter();
        interactor = new ListClubsInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void listClubsSeparatesMembership() {
        Account a = new Account("alice", "pw");
        userDAO.save(a);

        ArrayList<Account> members1 = new ArrayList<>();
        members1.add(a);
        clubsDAO.writeClub(100L, members1, "MemberClub", "desc", new ArrayList<Post>(), new ArrayList<>());
        clubsDAO.writeClub(200L, new ArrayList<>(), "OtherClub", "desc", new ArrayList<Post>(), new ArrayList<>());

        interactor.execute(new ListClubsInputData("alice"));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
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
        clubsDAO.writeClub(300L, new ArrayList<>(), "SomeClub", "d", new ArrayList<>(), new ArrayList<>());

        interactor.execute(new ListClubsInputData("bob"));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        // User club list should now be initialized (even if empty since not member yet)
        assertTrue(userDAO.get("bob") instanceof Account);
        assertNotNull(((Account) userDAO.get("bob")).getClubs());
    }

    @Test
    void announcementsCollectedAndFiltered() {
        Account user = new Account("carol", "pw");
        userDAO.save(user);
        // Member club with posts (announcement, null, non-announcement, trimmed spaced announcement)
        ArrayList<Account> memberList = new ArrayList<>(); memberList.add(user);
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post(user, 1L, "Ann1", "d", new ArrayList<>(), new HashMap<>(), "announcement", "2024-01-01 09:00 AM", new ArrayList<>()));
        posts.add(null);
        posts.add(new Post(user, 2L, "Other", "d", new ArrayList<>(), new HashMap<>(), "general", "2024-01-01 09:05 AM", new ArrayList<>()));
        posts.add(new Post(user, 3L, "Ann2", "d", new ArrayList<>(), new HashMap<>(), " Announcement ", "2024-01-01 09:10 AM", new ArrayList<>()));
        clubsDAO.writeClub(400L, memberList, "PostClub", "d", posts, new ArrayList<>());
        // Non-member club with posts shouldn't appear in announcements
        ArrayList<Post> otherPosts = new ArrayList<>();
        otherPosts.add(new Post(user, 4L, "AnnOther", "d", new ArrayList<>(), new HashMap<>(), "announcement", "2024-01-01 09:15 AM", new ArrayList<>()));
        clubsDAO.writeClub(401L, new ArrayList<>(), "Other", "d", otherPosts, new ArrayList<>());

        interactor.execute(new ListClubsInputData("carol"));

        assertNotNull(presenter.out);
        assertEquals(2, presenter.out.getAnnouncements().size());
        long countIds = presenter.out.getAnnouncements().stream().filter(p -> p.getID()==1L || p.getID()==3L).count();
        assertEquals(2, countIds);
    }

    @Test
    void exceptionHandlingShowsFailView() {
        Account user = new Account("dave", "pw"); userDAO.save(user);
        class ExplodingClubsDAO implements ClubsDataAccessObject {
            public void writeClub(long id, ArrayList<Account> m, String n, String d, ArrayList<Post> p, ArrayList<String> t) {}
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
        ListClubsOutputData out;
        String error;
        public void prepareSuccessView(ListClubsOutputData outputData) { this.out = outputData; }
        public void prepareFailView(String error) { this.error = error; }
    }
}
