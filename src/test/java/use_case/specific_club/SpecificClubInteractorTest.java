package use_case.specific_club;

import data_access.*;
import entity.Account;
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
        presenter = new TestSpecificClubPresenter();
        specificClubInteractor = new SpecificClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void executeSuccess() {
        // Setup test data
        long clubId = 1L;
        ArrayList<Account> members = new ArrayList<>();
        ArrayList<Post> posts = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();
        clubsDAO.writeClub(clubId, members, "Test Club", "Description", posts, tags);

        // Execute test
        specificClubInteractor.execute(new SpecificClubInputData(clubId, "Test Club", "Description"));

        // Verify results
        assertTrue(presenter.getLastOutput().isSuccess());
        assertEquals("Test Club", presenter.getLastOutput().getClub().getName());
    }

    @Test
    void loadClubSuccess() {
        // Setup test data
        long clubId = 1L;
        ArrayList<Account> members = new ArrayList<>();
        clubsDAO.writeClub(clubId, members, "Test Club", "Description", new ArrayList<>(), new ArrayList<>());

        // Execute test
        specificClubInteractor.loadClub(clubId);

        // Verify results
        assertTrue(presenter.getLastOutput().isSuccess());
        assertEquals("Test Club", presenter.getLastOutput().getClub().getName());
    }

    @Test
    void isMemberTrue() {
        // Setup test data
        long clubId = 1L;
        Account testUser = new Account("testUser", "password");
        ArrayList<Account> members = new ArrayList<>();
        members.add(testUser);
        clubsDAO.writeClub(clubId, members, "Test Club", "Description", new ArrayList<>(), new ArrayList<>());

        // Execute test and verify
        assertTrue(specificClubInteractor.isMember("testUser", clubId));
    }

    @Test
    void isMemberFalse() {
        // Setup test data
        long clubId = 1L;
        ArrayList<Account> members = new ArrayList<>();
        clubsDAO.writeClub(clubId, members, "Test Club", "Description", new ArrayList<>(), new ArrayList<>());

        // Execute test and verify
        assertFalse(specificClubInteractor.isMember("nonexistentUser", clubId));
    }

    private static class TestSpecificClubPresenter implements SpecificClubOutputBoundary {
        private SpecificClubOutputData lastOutput;

        @Override
        public void prepareSuccessView(SpecificClubOutputData outputData) {
            this.lastOutput = outputData;
        }

        @Override
        public void prepareFailView(String error) {
        }

        public SpecificClubOutputData getLastOutput() {
            return lastOutput;
        }
    }
}
