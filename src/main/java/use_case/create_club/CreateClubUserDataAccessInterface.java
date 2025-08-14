package use_case.create_club;

import entity.Account;
import entity.User;
import java.util.ArrayList;

/**
 * Boundary interface (gateway) for user persistence operations needed by the Create Club use case.
 * Uses generic User for compatibility; implementors may return concrete Account (covariant).
 */
public interface CreateClubUserDataAccessInterface {
    User get(String username);
    void save(User user);
    ArrayList<Account> getAllUsers();
}
