package use_case.create_club;

import data_access.*;
import entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateClubInteractorTest {
    private CreateClubInteractor createClubInteractor;
    private ClubsDataAccessObject clubsDAO;
    private UserDataAccessObject userDAO;
    private TestCreateClubPresenter presenter;

    @BeforeEach
    void setUp() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsDAO = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userDAO = InMemoryUserDataAccessObject.getInstance();
        presenter = new TestCreateClubPresenter();
        createClubInteractor = new CreateClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void createClubSuccess() {
        // Setup test data
        Account testUser = new Account("testUser", "password");
        userDAO.save(testUser);

        List<String> memberUsernames = new ArrayList<>();
        memberUsernames.add("testUser");
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");

        // Execute test
        createClubInteractor.create(
            "Test Club",
            "Test Description",
            "image.jpg",
            tags,
            memberUsernames
        );

        // Verify results
        assertTrue(presenter.getLastOutput().isSuccess());
        assertEquals("Test Club", presenter.getLastOutput().getClub().getName());
        assertEquals("Test Description", presenter.getLastOutput().getClub().getDescription());
    }

    @Test
    void createClubEmptyTitleFail() {
        // Setup test data
        List<String> memberUsernames = new ArrayList<>();
        memberUsernames.add("testUser");
        List<String> tags = new ArrayList<>();
        tags.add("tag1");

        // Execute test
        createClubInteractor.create(
            "",
            "Test Description",
            "image.jpg",
            tags,
            memberUsernames
        );

        // Verify results
        assertEquals("Title cannot be empty", presenter.getLastError());
    }

    @Test
    void createClubEmptyDescriptionFail() {
        // Setup test data
        List<String> memberUsernames = new ArrayList<>();
        memberUsernames.add("testUser");
        List<String> tags = new ArrayList<>();
        tags.add("tag1");

        // Execute test
        createClubInteractor.create(
            "Test Club",
            "",
            "image.jpg",
            tags,
            memberUsernames
        );

        // Verify results
        assertEquals("Description cannot be empty", presenter.getLastError());
    }

    @Test
    void createClubNoMembersFail() {
        // Setup test data
        List<String> memberUsernames = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        tags.add("tag1");

        // Execute test
        createClubInteractor.create(
            "Test Club",
            "Test Description",
            "image.jpg",
            tags,
            memberUsernames
        );

        // Verify results
        assertEquals("Club must have at least one member", presenter.getLastError());
    }

    @Test
    void showMemberSelectionSuccess() {
        // Setup test data
        Account creator = new Account("creator", "password");
        Account member1 = new Account("member1", "password");
        Account member2 = new Account("member2", "password");

        userDAO.save(creator);
        userDAO.save(member1);
        userDAO.save(member2);

        List<Account> currentMembers = new ArrayList<>();
        currentMembers.add(member1);

        // Execute test
        createClubInteractor.showMemberSelection(currentMembers, "creator");

        // Verify results
        List<Account> availableMembers = presenter.getLastAvailableMembers();
        assertNotNull(availableMembers);
        assertEquals(1, availableMembers.size());
        assertEquals("member2", availableMembers.get(0).getUsername());
    }

    private static class TestCreateClubPresenter implements CreateClubOutputBoundary {
        private CreateClubOutputData lastOutput;
        private String lastError;
        private List<Account> lastAvailableMembers;

        @Override
        public void prepareSuccessView(CreateClubOutputData outputData) {
            this.lastOutput = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastError = error;
        }

        @Override
        public void prepareMemberSelectionView(List<Account> availableMembers) {
            this.lastAvailableMembers = availableMembers;
        }

        public CreateClubOutputData getLastOutput() {
            return lastOutput;
        }

        public String getLastError() {
            return lastError;
        }

        public List<Account> getLastAvailableMembers() {
            return lastAvailableMembers;
        }
    }
}
