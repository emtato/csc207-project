package use_case.signup;

import entity.Account;
import entity.User;
import entity.UserFactory;

import java.util.HashMap;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        System.out.println("Debug: Received signup input username: " + signupInputData.getUsername());
        if (userDataAccessObject.existsByName(signupInputData.getUsername())) {
            userPresenter.prepareFailView("User already exists.");
        }
        else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("Passwords don't match.");
        }
        else {
            final User user = userFactory.create(signupInputData.getUsername(), signupInputData.getPassword());
            System.out.println("Debug: Created user with name: " + user.getUsername());

            // TODO: delete this later, using it for testing for now
            HashMap<String, User> test = new HashMap<>();
            User userTest = new Account("username", "password");
            userTest.setProfilePictureUrl("https://i.imgur.com/eA9NeJ1.jpeg");
            userTest.setDisplayName("display name");
            test.put("username", userTest);
            user.setFollowingAccounts(test);

            HashMap<String, User> test2 = new HashMap<>();
            User userTest2 = new Account("username!", "password");
            userTest2.setProfilePictureUrl("https://i.imgur.com/NiZh2s6.jpeg");
            userTest2.setDisplayName("display name!");
            User userTest3 = new Account("username!!", "password");
            userTest3.setProfilePictureUrl("https://i.imgur.com/NiZh2s6.jpeg");
            userTest3.setDisplayName("display name!!");
            User userTest4 = new Account("username!!!", "password");
            userTest4.setProfilePictureUrl("https://i.imgur.com/NiZh2s6.jpeg");
            userTest4.setDisplayName("display name!!!");
            User userTest5 = new Account("username!!!!", "password");
            userTest5.setProfilePictureUrl("https://i.imgur.com/NiZh2s6.jpeg");
            userTest5.setDisplayName("display name!!!!");
            test2.put("username", userTest);
            test2.put("username!", userTest2);
            test2.put("username!!", userTest3);
            test2.put("username!!!", userTest4);
            test2.put("username!!!!", userTest5);
            user.setFollowerAccounts(test2);

            userDataAccessObject.save(user);
            System.out.println("Debug: User exists after save: " + userDataAccessObject.existsByName(user.getUsername()));

            final SignupOutputData signupOutputData = new SignupOutputData(user.getUsername(), false);
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }

}
