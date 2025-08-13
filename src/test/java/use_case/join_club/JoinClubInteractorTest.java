package use_case.join_club;

import data_access.*;
import entity.Account;
import entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        presenter = new TestPresenter();
        interactor = new JoinClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void joinClubAddsMembership() {
        // user
        Account user = new Account("bob", "pw");
        userDAO.save(user);
        // club (no members)
        clubsDAO.writeClub(300L, new ArrayList<>(), "CoolClub", "desc", new ArrayList<Post>(), new ArrayList<>());

        interactor.execute(new JoinClubInputData("bob", 300L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        assertTrue(presenter.out.getMemberClubs().stream().anyMatch(c -> c.getName().equals("CoolClub")));
        // ensure removed from non-member list
        assertTrue(presenter.out.getNonMemberClubs().stream().noneMatch(c -> c.getName().equals("CoolClub")));
    }

    private static class TestPresenter implements JoinClubOutputBoundary {
        JoinClubOutputData out; String error;
        public void prepareSuccessView(JoinClubOutputData outputData) { this.out = outputData; }
        public void prepareFailView(String error) { this.error = error; }
    }
}

