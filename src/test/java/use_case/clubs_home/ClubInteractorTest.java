package use_case.clubs_home;

import data_access.*;
import entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.create_club.CreateClubInputData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClubInteractorTest {
    private ClubInteractor clubInteractor;
    private ClubsDataAccessObject clubsDAO;
    private UserDataAccessObject userDAO;

    @BeforeEach
    void setUp() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsDAO = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userDAO = InMemoryUserDataAccessObject.getInstance();
        TestClubPresenter presenter = new TestClubPresenter();
        clubInteractor = new ClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void getClubsDataSuccess() {
        // Setup test data
        Account testUser = new Account("testUser", "password");
        ArrayList<String> userClubs = new ArrayList<>();
        userClubs.add("1");
        testUser.setClubs(userClubs);
        userDAO.save(testUser);

        ArrayList<Account> members = new ArrayList<>();
        members.add(testUser);
        clubsDAO.writeClub(1L, members, "Test Club", "Description", new ArrayList<>(), new ArrayList<>());

        // Execute test
        ClubOutputData result = clubInteractor.getClubsData("testUser");

        // Verify results
        assertTrue(result.isSuccess());
        assertEquals(1, result.getMemberClubs().size());
       // assertEquals(0, result.getNonMemberClubs().size());
        assertEquals("Test Club", result.getMemberClubs().get(0).getName());
    }

    @Test
    void joinClubSuccess() {
        // Setup test data
        Account testUser = new Account("testUser", "password");
        userDAO.save(testUser);

        ArrayList<Account> members = new ArrayList<>();
        clubsDAO.writeClub(1L, members, "Test Club", "Description", new ArrayList<>(), new ArrayList<>());

        // Execute test
        ClubInputData inputData = new ClubInputData("testUser", "1");
        ClubOutputData result = clubInteractor.joinClub(inputData);

        // Verify results
        assertTrue(result.isSuccess());
        assertTrue(result.getMemberClubs().stream()
                .anyMatch(club -> club.getName().equals("Test Club")));
    }

    @Test
    void createClubSuccess() {
        // Setup test data
        Account testUser = new Account("testUser", "password");
        userDAO.save(testUser);

        ArrayList<String> members = new ArrayList<>();
        members.add("testUser");
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        CreateClubInputData inputData = new CreateClubInputData(
                "New Club",
                "New Description",
                "default.jpg",
                tags,
                members
        );

        // Execute test
        ClubOutputData result = clubInteractor.createClub(inputData);

        // Verify results
        assertTrue(result.isSuccess());
        assertTrue(result.getMemberClubs().stream()
                .anyMatch(club -> club.getName().equals("New Club")));
    }

    private static class TestClubPresenter implements ClubOutputBoundary {
        @Override
        public void prepareSuccessView(ClubOutputData clubOutputData) {
        }

        @Override
        public void prepareFailView(String error) {
        }
    }
}
