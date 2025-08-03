package use_case.settings;

import data_access.InMemoryUserDataAccessObject;
import entity.CreateAccount;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
class SettingsInteractorTest {

    @Test
    void privacyToggleOffSuccessTest() {
        final boolean isPublic = false;
        SettingsInputData inputData = new SettingsInputData("Caf Feine", isPublic);
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        // Add Caf Feine to the user repository.
        UserFactory factory = new CreateAccount();
        User user = factory.create("Caf Feine", "password");
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        SettingsOutputBoundary successPresenter = new SettingsOutputBoundary() {
            @Override
            public void preparePrivacySuccessView(SettingsOutputData outputData) {
                assertEquals(isPublic, outputData.isOn());
                assertEquals(isPublic, userRepository.get("Caf Feine").isPublic());
            }

            @Override
            public void prepareNotificationsSuccessView(SettingsOutputData outputData) {
                fail("Notifications use case execution is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        SettingsInputBoundary interactor = new SettingsInteractor(userRepository, successPresenter);
        interactor.executePrivacyToggle(inputData);
    }

    @Test
    void notificationsToggleOffSuccessTest() {
        final boolean notificationsEnabled = false;
        SettingsInputData inputData = new SettingsInputData("Caf Feine", notificationsEnabled);
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        // Add Caf Feine to the user repository.
        UserFactory factory = new CreateAccount();
        User user = factory.create("Caf Feine", "password");
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        SettingsOutputBoundary successPresenter = new SettingsOutputBoundary() {
            @Override
            public void prepareNotificationsSuccessView(SettingsOutputData outputData) {
                assertEquals(notificationsEnabled, outputData.isOn());
                assertEquals(notificationsEnabled, userRepository.get("Caf Feine").isNotificationsEnabled());
            }

            @Override
            public void preparePrivacySuccessView(SettingsOutputData outputData) {
                fail("Privacy use case execution is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        SettingsInputBoundary interactor = new SettingsInteractor(userRepository, successPresenter);
        interactor.executeNotificationsToggle(inputData);
    }
}