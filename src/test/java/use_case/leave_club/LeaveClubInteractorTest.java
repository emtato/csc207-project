package use_case.leave_club;

import data_access.*;
import entity.Account;
import entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LeaveClubInteractorTest {
    private LeaveClubInteractor interactor;
    private ClubsDataAccessObject clubsDAO;
    private UserDataAccessObject userDAO;
    private TestPresenter presenter;

    @BeforeEach
    void setup() {
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        clubsDAO = InMemoryClubsDataAccessObject.getInstance(postDAO);
        userDAO = InMemoryUserDataAccessObject.getInstance();
        presenter = new TestPresenter();
        interactor = new LeaveClubInteractor(clubsDAO, userDAO, presenter);
    }

    @Test
    void leaveClubRemovesMembership() {
        Account user = new Account("carol", "pw");
        ArrayList<String> clubs = new ArrayList<>();
        clubs.add("500");
        user.setClubs(clubs);
        userDAO.save(user);

        ArrayList<Account> members = new ArrayList<>();
        members.add(user);
        clubsDAO.writeClub(500L, members, "TempClub", "desc", new ArrayList<Post>(), new ArrayList<>());

        interactor.execute(new LeaveClubInputData("carol", 500L));

        assertNotNull(presenter.out);
        assertTrue(presenter.out.isSuccess());
        assertTrue(presenter.out.getNonMemberClubs().stream().anyMatch(c -> c.getName().equals("TempClub")));
        assertFalse(presenter.out.getMemberClubs().stream().anyMatch(c -> c.getName().equals("TempClub")));
    }

    private static class TestPresenter implements LeaveClubOutputBoundary {
        LeaveClubOutputData out; String error;
        public void prepareSuccessView(LeaveClubOutputData outputData) { this.out = outputData; }
        public void prepareFailView(String error) { this.error = error; }
    }
}

