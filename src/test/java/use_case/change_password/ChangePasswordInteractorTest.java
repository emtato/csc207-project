package use_case.change_password;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessObject;
import entity.CreateAccount;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ChangePasswordInteractorTest {
    @Test
    void successTest() {
        final ChangePasswordInputData inputData = new ChangePasswordInputData("new password", "Caf");
        final UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();

        // For the success test, we need to add Caf to the data access repository before we log in.
        UserFactory factory = new CreateAccount();
        User user = factory.create("Caf", "password");
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        ChangePasswordOutputBoundary successPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData user) {
                assertEquals("Caf", user.getUsername());
                assertEquals("new password", userRepository.get("Caf").getPassword());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        ChangePasswordInputBoundary interactor = new ChangePasswordInteractor(userRepository, successPresenter,
                factory);
        interactor.execute(inputData);
    }
}
