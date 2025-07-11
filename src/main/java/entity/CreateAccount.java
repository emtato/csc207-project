package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class CreateAccount implements UserFactory {

    @Override
    public User create(String name, String password) {
        return new Account(name, password);
    }
}
