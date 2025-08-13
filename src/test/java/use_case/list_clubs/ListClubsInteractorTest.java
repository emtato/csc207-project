package use_case.list_clubs;

import data_access.*;
import entity.Account;
import entity.Club;
import entity.Post;
import interface_adapter.clubs_home.ClubViewModel; // only for state shape if needed (not used here)
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        presenter = new TestPresenter();
        interactor = new ListClubsInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void listClubsSeparatesMembership() {
        Account a = new Account("alice", "pw");
        userDAO.save(a);

        // create two clubs, one where alice is member
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

    private static class TestPresenter implements ListClubsOutputBoundary {
        ListClubsOutputData out;
        String error;
        public void prepareSuccessView(ListClubsOutputData outputData) { this.out = outputData; }
        public void prepareFailView(String error) { this.error = error; }
    }
}

