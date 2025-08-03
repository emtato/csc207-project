package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class CreateAccount implements UserFactory {

    @Override
    public User create(String username, String password) {
        return new Account(username, password);
    }
}
