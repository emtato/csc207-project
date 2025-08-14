package use_case.create_club;

import data_access.*;
import entity.Account;
import entity.Club;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CreateClubInteractorTest {
    private CreateClubInteractor interactor;
    private CreateClubClubsDataAccessInterface clubsGateway;
    private CreateClubUserDataAccessInterface userGateway;
    private TestCreateClubPresenter presenter;
    private static int uniqueCounter = 0;

    @BeforeEach
    void setUp() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsGateway = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userGateway = InMemoryUserDataAccessObject.getInstance();
        // Reset users (cannot easily clear clubs, so use unique titles per test)
        for (Account a : new ArrayList<>(((InMemoryUserDataAccessObject) userGateway).getAllUsers())) {
            ((InMemoryUserDataAccessObject) userGateway).deleteAccount(a.getUsername());
        }
        presenter = new TestCreateClubPresenter();
        interactor = new CreateClubInteractor(clubsGateway, userGateway, presenter);
    }

    private String uniqueTitle(String base) { return base + "_" + (uniqueCounter++); }

    @Test
    void createClubSuccessWithDedupAndNulls() {
        // Users
        Account u1 = new Account("alice", "pw");
        Account u2 = new Account("bob", "pw");
        userGateway.save(u1); userGateway.save(u2);
        // member list includes duplicates, whitespace, null, empty
        List<String> members = Arrays.asList("alice", " alice ", "bob", null, "", "bob");
        String title = uniqueTitle("FunClub");
        interactor.execute(new CreateClubInputData(title, "Desc", "img", Arrays.asList("t1","t2"), members));
        assertNotNull(presenter.lastOutput);
        assertTrue(presenter.lastOutput.isSuccess());
        assertEquals(title, presenter.lastOutput.getClub().getName());
        // Ensure each member has club id in their list only once
        String createdId = presenter.lastOutput.getClub().getId()+""; // but Club has getId? (Entity Club has getId())
        for (Account m : Arrays.asList(u1,u2)) {
            long count = m.getClubs().stream().filter(id -> id.equals(createdId)).count();
            assertEquals(1, count);
        }
    }

    @Test
    void emptyTitleFails() {
        interactor.execute(new CreateClubInputData("  ", "Desc", "img", null, Collections.singletonList("user")));
        assertEquals("Title cannot be empty", presenter.lastError);
    }

    @Test
    void emptyDescriptionFails() {
        interactor.execute(new CreateClubInputData(uniqueTitle("Club"), "", "img", null, Collections.singletonList("user")));
        assertEquals("Description cannot be empty", presenter.lastError);
    }

    @Test
    void clubExistsFails() {
        Account u = new Account("maker", "pw"); userGateway.save(u);
        String title = uniqueTitle("ExistingClub");
        // First create
        interactor.execute(new CreateClubInputData(title, "Desc", "img", null, Collections.singletonList("maker")));
        assertTrue(presenter.lastOutput != null && presenter.lastOutput.isSuccess());
        // Second attempt with same title
        presenter.reset();
        interactor.execute(new CreateClubInputData(title, "Desc2", "img", null, Collections.singletonList("maker")));
        assertEquals("A club with this name already exists", presenter.lastError);
    }

    @Test
    void invalidMemberUsernamesFails() {
        Account good = new Account("good", "pw"); userGateway.save(good);
        String title = uniqueTitle("InvalidMembersClub");
        interactor.execute(new CreateClubInputData(title, "Desc", "img", null, Arrays.asList("good", "bad1", "bad1", "bad2")));
        assertTrue(presenter.lastError.startsWith("Invalid member username(s):"));
        assertTrue(presenter.lastError.contains("bad1"));
        assertTrue(presenter.lastError.contains("bad2"));
    }

    @Test
    void noValidMembersFailsWhenAllInvalid() {
        String title = uniqueTitle("NoMembersClub");
        interactor.execute(new CreateClubInputData(title, "Desc", "img", null, Arrays.asList("ghost")));
        assertEquals("Invalid member username(s): ghost", presenter.lastError);
    }

    @Test
    void nullMemberListFailsNoMembers() {
        // Provide null member list -> becomes empty -> fails
        String title = uniqueTitle("NullMembersClub");
        interactor.execute(new CreateClubInputData(title, "Desc", "img", null, null));
        assertEquals("Club must have at least one valid member", presenter.lastError);
    }

    @Test
    void exceptionDuringCreateHandled() {
        // Custom gateway throws after validation passes
        class ThrowingClubsGateway implements CreateClubClubsDataAccessInterface {
            public void writeClub(long clubID, ArrayList<Account> members, String name, String description, String imageUrl, ArrayList<entity.Post> posts, ArrayList<String> tags) { throw new RuntimeException("boom"); }
            public Club getClub(long clubID) { return null; }
            public boolean clubExists(String clubName) { return false; }
        }
        Account m = new Account("member", "pw"); userGateway.save(m);
        CreateClubInteractor throwingInteractor = new CreateClubInteractor(new ThrowingClubsGateway(), userGateway, presenter);
        throwingInteractor.execute(new CreateClubInputData(uniqueTitle("ThrowClub"), "Desc", "img", null, Collections.singletonList("member")));
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.startsWith("Error creating club:"));
    }

    @Test
    void showMemberSelectionSuccess() {
        Account creator = new Account("creator", "pw");
        Account member1 = new Account("m1", "pw");
        Account member2 = new Account("m2", "pw");
        userGateway.save(creator); userGateway.save(member1); userGateway.save(member2);
        List<Account> current = Collections.singletonList(member1);
        interactor.showMemberSelection(current, "creator");
        assertNotNull(presenter.lastAvailableMembers);
        // Should exclude creator and existing member1 leaving only member2
        assertEquals(1, presenter.lastAvailableMembers.size());
        assertEquals("m2", presenter.lastAvailableMembers.get(0).getUsername());
    }

    @Test
    void showMemberSelectionHandlesException() {
        class ThrowingUserGateway implements CreateClubUserDataAccessInterface {
            public User get(String username) { return null; }
            public void save(User user) {}
            public ArrayList<Account> getAllUsers() { throw new RuntimeException("fail"); }
        }
        CreateClubInteractor throwing = new CreateClubInteractor(clubsGateway, new ThrowingUserGateway(), presenter);
        throwing.showMemberSelection(Collections.emptyList(), "creator");
        assertTrue(presenter.lastError != null && presenter.lastError.startsWith("Error loading users:"));
    }

    // Presenter
    private static class TestCreateClubPresenter implements CreateClubOutputBoundary {
        CreateClubOutputData lastOutput; String lastError; List<Account> lastAvailableMembers;
        public void prepareSuccessView(CreateClubOutputData outputData) { this.lastOutput = outputData; }
        public void prepareFailView(String error) { this.lastError = error; }
        public void prepareMemberSelectionView(List<Account> availableUsers) { this.lastAvailableMembers = availableUsers; }
        void reset() { lastOutput = null; lastError = null; lastAvailableMembers = null; }
    }
}
