package use_case.edit_profile;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessObject;
import entity.CreateAccount;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EditProfileInteractorTest {
    @Test
    void saveChangesSuccessTest() {

        // Create user with username Caf Feine
        final String username = "Caf Feine";
        final String password = "password";
        UserFactory factory = new CreateAccount();
        final User user = factory.create(username, password);

        // New profile data
        final String newDisplayName = "Latte";
        final String newBio = "I love you a latte!";
        final String newProfilePictureUrl = "https://i.imgur.com/ZMyaRnT.jpeg";
        final ArrayList<String> newPreferences = new ArrayList<>();
        newPreferences.add("coffee");
        newPreferences.add("tea");
        newPreferences.add("energy drinks");
        EditProfileInputData inputData = new EditProfileInputData(username, newDisplayName, newBio,
                newProfilePictureUrl, newPreferences);

        // Add user to the repository
        UserDataAccessObject userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        EditProfileOutputBoundary successPresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertEquals(newDisplayName, outputData.getNewDisplayName());
                assertEquals(newBio, outputData.getNewBio());
                assertEquals(newProfilePictureUrl, outputData.getNewProfilePictureUrl());
                assertEquals(newPreferences, outputData.getNewPreferences());
                final User newUser = userRepository.get(username);
                assertEquals(username, newUser.getUsername());
                assertEquals(newDisplayName, newUser.getDisplayName());
                assertEquals(newBio, newUser.getBio());
                assertEquals(newProfilePictureUrl, newUser.getProfilePictureUrl());
                assertEquals(newPreferences, newUser.getFoodPreferences());
            }
            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToProfileView() {
                fail("This use case is unexpected.");
            }
        };

        EditProfileInputBoundary interactor = new EditProfileInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }
}
