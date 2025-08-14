package use_case.join_club;

import data_access.*;
import entity.Account;
import entity.Club;
import entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JoinClubInteractorTest {
    private JoinClubInteractor interactor;
    private ClubsDataAccessObject clubsDAO;
    private UserDataAccessObject userDAO;
    private TestPresenter presenter;

    @BeforeEach
    void setup() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsDAO = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userDAO = InMemoryUserDataAccessObject.getInstance();
        // Reset singleton in-memory state
        for (Club c : new ArrayList<>(clubsDAO.getAllClubs())) { clubsDAO.deleteClub(c.getId()); }
        for (Account a : new ArrayList<>(userDAO.getAllUsers())) { userDAO.deleteAccount(a.getUsername()); }
        presenter = new TestPresenter();
        interactor = new JoinClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void joinClubAddsMembership() {
        Account user = new Account("bob", "pw");
        userDAO.save(user);
        clubsDAO.writeClub(300L, new ArrayList<>(), "CoolClub", "desc", new ArrayList<Post>(), new ArrayList<>());

        interactor.execute(new JoinClubInputData("bob", 300L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        assertTrue(presenter.out.getMemberClubs().stream().anyMatch(c -> c.getName().equals("CoolClub")));
        assertTrue(presenter.out.getNonMemberClubs().stream().noneMatch(c -> c.getName().equals("CoolClub")));
    }

    @Test
    void clubNotFoundTriggersFailView() {
        Account user = new Account("alice", "pw");
        userDAO.save(user);
        interactor.execute(new JoinClubInputData("alice", 9999L));
        assertNull(presenter.out);
        assertEquals("Club not found", presenter.error);
    }

    @Test
    void userNotFoundTriggersFailView() {
        clubsDAO.writeClub(42L, new ArrayList<>(), "SomeClub", "desc", new ArrayList<>(), new ArrayList<>());
        interactor.execute(new JoinClubInputData("ghost", 42L));
        assertNull(presenter.out);
        assertEquals("User not found", presenter.error);
    }

    @Test
    void alreadyMemberDoesNotDuplicate() {
        Account user = new Account("chris", "pw");
        ArrayList<String> userClubs = new ArrayList<>(); userClubs.add("55");
        user.setClubs(userClubs);
        userDAO.save(user);
        ArrayList<Account> members = new ArrayList<>(); members.add(user);
        clubsDAO.writeClub(55L, members, "DupClub", "desc", new ArrayList<>(), new ArrayList<>());

        interactor.execute(new JoinClubInputData("chris", 55L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        Club dupClub = presenter.out.getMemberClubs().stream().filter(c -> c.getId()==55L).findFirst().orElse(null);
        assertNotNull(dupClub);
        // membership list should only contain user once
        long count = dupClub.getMembers().stream().filter(a -> a.getUsername().equals("chris")).count();
        assertEquals(1, count);
    }

    @Test
    void nullUserClubListHandled() {
        Account user = new Account("dana", "pw");
        user.setClubs(null); // force null branch
        userDAO.save(user);
        clubsDAO.writeClub(70L, new ArrayList<>(), "NullListClub", "desc", new ArrayList<>(), new ArrayList<>());

        interactor.execute(new JoinClubInputData("dana", 70L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        assertTrue(presenter.out.getMemberClubs().stream().anyMatch(c -> c.getId()==70L));
    }

    @Test
    void announcementsCollectedFromMemberClubs() {
        Account user = new Account("erin", "pw");
        userDAO.save(user);
        // Club A to join
        clubsDAO.writeClub(81L, new ArrayList<>(), "AnnClub", "d", new ArrayList<>(), new ArrayList<>());
        // Club B already joined with announcement posts
        ArrayList<Account> membersB = new ArrayList<>(); membersB.add(user);
        ArrayList<Post> postsB = new ArrayList<>();
        postsB.add(new Post(user, 1L, "Ann1", "d", new ArrayList<>(), new HashMap<>(), " announcement ", "2024-01-01 09:00 AM", new ArrayList<>()));
        postsB.add(new Post(user, 2L, "Other", "d", new ArrayList<>(), new HashMap<>(), "general", "2024-01-01 09:05 AM", new ArrayList<>()));
        postsB.add(null);
        postsB.add(new Post(user, 3L, "NullType", "d", new ArrayList<>(), new HashMap<>(), null, "2024-01-01 09:10 AM", new ArrayList<>()));
        clubsDAO.writeClub(82L, membersB, "Existing", "d", postsB, new ArrayList<>());
        // user is already member of 82
        ArrayList<String> uc = new ArrayList<>(); uc.add("82"); user.setClubs(uc); userDAO.save(user);

        interactor.execute(new JoinClubInputData("erin", 81L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        // Announcements should include only post ID 1 from club 82
        assertEquals(1, presenter.out.getAnnouncements().size());
        assertEquals(1L, presenter.out.getAnnouncements().get(0).getID());
    }

    @Test
    void exceptionDuringProcessingTriggersFailView() {
        class ExplodingClubsDAO implements ClubsDataAccessObject {
            @Override public void writeClub(long clubID, ArrayList<Account> members, String name, String description, ArrayList<Post> posts, ArrayList<String> tags) { }
            @Override public Club getClub(long clubID) { return new Club("X", "d", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), clubID, new ArrayList<>()); }
            @Override public boolean clubExists(String clubName) { return false; }
            @Override public ArrayList<Club> getAllClubs() { throw new RuntimeException("boom"); }
            @Override public void removeMemberFromClub(String username, long clubID) { }
            @Override public void deleteClub(long clubID) { }
        }
        Account user = new Account("fred", "pw"); userDAO.save(user);
        ClubsDataAccessObject exploding = new ExplodingClubsDAO();
        JoinClubInteractor explodingInteractor = new JoinClubInteractor(exploding, userDAO, presenter);
        explodingInteractor.execute(new JoinClubInputData("fred", 9L));
        assertNull(presenter.out);
        assertTrue(presenter.error.startsWith("Error joining club:"));
    }

    private static class TestPresenter implements JoinClubOutputBoundary {
        JoinClubOutputData out; String error;
        public void prepareSuccessView(JoinClubOutputData outputData) { this.out = outputData; }
        public void prepareFailView(String error) { this.error = error; }
    }
}
