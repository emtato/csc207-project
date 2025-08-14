package use_case.select_club_members;

import entity.Account;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.create_club.CreateClubUserDataAccessInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SelectClubMembersInteractorTest {

    // Test presenter capturing outputs
    private static class TestPresenter implements SelectClubMembersOutputBoundary {
        List<Account> lastAvailable; String lastError;
        @Override public void prepareSelectionView(List<Account> availableUsers) { this.lastAvailable = availableUsers; }
        @Override public void prepareFailView(String error) { this.lastError = error; }
    }

    private static class BaseGateway implements CreateClubUserDataAccessInterface {
        ArrayList<Account> users;
        BaseGateway(Account... users) { this.users = new ArrayList<>(Arrays.asList(users)); }
        @Override public User get(String username) { return null; }
        @Override public void save(User user) {}
        @Override public ArrayList<Account> getAllUsers() { return users; }
    }

    @Test
    void filtersOutCreatorAndExistingMembers() {
        Account creator = new Account("creator", "pw");
        Account m1 = new Account("m1", "pw");
        Account m2 = new Account("m2", "pw");
        Account m3 = new Account("m3", "pw");
        TestPresenter presenter = new TestPresenter();
        SelectClubMembersInteractor interactor = new SelectClubMembersInteractor(new BaseGateway(creator, m1, m2, m3), presenter);
        interactor.showMemberSelection(Collections.singletonList(m1), "creator");
        assertNotNull(presenter.lastAvailable);
        // Expect m2 & m3 only
        assertEquals(2, presenter.lastAvailable.size());
        List<String> names = presenter.lastAvailable.stream().map(Account::getUsername).collect(Collectors.toList());
        assertTrue(names.contains("m2"));
        assertTrue(names.contains("m3"));
        assertFalse(names.contains("creator"));
        assertFalse(names.contains("m1"));
    }

    @Test
    void returnsAllWhenNoCurrentMembersExceptCreatorExcluded() {
        Account creator = new Account("creator", "pw");
        Account a = new Account("a", "pw");
        Account b = new Account("b", "pw");
        TestPresenter presenter = new TestPresenter();
        SelectClubMembersInteractor interactor = new SelectClubMembersInteractor(new BaseGateway(creator, a, b), presenter);
        interactor.showMemberSelection(Collections.emptyList(), "creator");
        assertEquals(2, presenter.lastAvailable.size());
        List<String> names = presenter.lastAvailable.stream().map(Account::getUsername).collect(Collectors.toList());
        assertEquals(List.of("a","b"), names.stream().sorted().collect(Collectors.toList()));
    }

    @Test
    void preservesDuplicatesInAvailableList() {
        // Interactor does not de-duplicate; ensure behavior documented by test
        Account creator = new Account("creator", "pw");
        Account dup = new Account("x", "pw");
        // two references representing duplicated entries
        BaseGateway gw = new BaseGateway(creator, dup, dup);
        TestPresenter presenter = new TestPresenter();
        SelectClubMembersInteractor interactor = new SelectClubMembersInteractor(gw, presenter);
        interactor.showMemberSelection(Collections.emptyList(), "creator");
        assertEquals(2, presenter.lastAvailable.size());
    }

    @Test
    void nullAllUsersReturnsEmptyList() {
        CreateClubUserDataAccessInterface gw = new CreateClubUserDataAccessInterface() {
            @Override public User get(String username) { return null; }
            @Override public void save(User user) {}
            @Override public ArrayList<Account> getAllUsers() { return null; }
        };
        TestPresenter presenter = new TestPresenter();
        SelectClubMembersInteractor interactor = new SelectClubMembersInteractor(gw, presenter);
        interactor.showMemberSelection(Collections.emptyList(), "creator");
        assertNotNull(presenter.lastAvailable);
        assertTrue(presenter.lastAvailable.isEmpty());
    }

    @Test
    void exceptionPathTriggersFailView() {
        CreateClubUserDataAccessInterface gw = new CreateClubUserDataAccessInterface() {
            @Override public User get(String username) { return null; }
            @Override public void save(User user) {}
            @Override public ArrayList<Account> getAllUsers() { throw new RuntimeException("boom"); }
        };
        TestPresenter presenter = new TestPresenter();
        SelectClubMembersInteractor interactor = new SelectClubMembersInteractor(gw, presenter);
        interactor.showMemberSelection(Collections.emptyList(), "creator");
        assertNull(presenter.lastAvailable);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.startsWith("Error loading users:"));
    }
}
