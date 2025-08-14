package use_case.homepage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomePageInteractorTest {

    @Test
    void switchToSignUp_callsPresenter() {
        final boolean[] called = new boolean[1];

        HomePageOutputBoundary presenter = new HomePageOutputBoundary() {
            @Override
            public void prepareHomePageView() {
                fail("should not be called");
            }

            @Override
            public void switchToSignUpView() {
                called[0] = true;
            }

            @Override
            public void switchToLogInView() {
                fail("should not be called");
            }

            @Override
            public void switchToClubView() {
                fail("should not be called");
            }

            @Override
            public void switchToSettingsView() {
                fail("should not be called");
            }
        };

        HomePageInputBoundary interactor = new HomePageInteractor(presenter);
        interactor.switchToSignUpView();

        assertEquals(true, called[0]);
    }

    @Test
    void switchToLogIn_callsPresenter() {
        final boolean[] called = {false};

        HomePageOutputBoundary presenter = new HomePageOutputBoundary() {
            @Override
            public void prepareHomePageView() {
                fail("should not be called");
            }

            @Override
            public void switchToSignUpView() {
                fail("should not be called");
            }

            @Override
            public void switchToLogInView() {
                called[0] = true;
            }

            @Override
            public void switchToClubView() {
                fail("should not be called");
            }

            @Override
            public void switchToSettingsView() {
                fail("should not be called");
            }
        };

        HomePageInputBoundary interactor = new HomePageInteractor(presenter);
        interactor.switchToLogInView();

        assertEquals(true, called[0]);
    }

    @Test
    void switchToClub_callsPresenter() {
        final boolean[] called = {false};

        HomePageOutputBoundary presenter = new HomePageOutputBoundary() {
            @Override
            public void prepareHomePageView() {
                fail("should not be called");
            }

            @Override
            public void switchToSignUpView() {
                fail("should not be called");
            }

            @Override
            public void switchToLogInView() {
                fail("should not be called");
            }

            @Override
            public void switchToClubView() {
                called[0] = true;
            }

            @Override
            public void switchToSettingsView() {
                fail("should not be called");
            }
        };

        HomePageInputBoundary interactor = new HomePageInteractor(presenter);
        interactor.switchToClubView();

        assertEquals(true, called[0]);
    }

    @Test
    void switchToSettings_callsPresenter() {
        final boolean[] called = {false};

        HomePageOutputBoundary presenter = new HomePageOutputBoundary() {
            @Override
            public void prepareHomePageView() {
                fail("should not be called");
            }

            @Override
            public void switchToSignUpView() {
                fail("should not be called");
            }

            @Override
            public void switchToLogInView() {
                fail("should not be called");
            }

            @Override
            public void switchToClubView() {
                fail("should not be called");
            }

            @Override
            public void switchToSettingsView() {
                called[0] = true;
            }
        };

        HomePageInputBoundary interactor = new HomePageInteractor(presenter);
        interactor.switchToSettingsView();

        assertEquals(true, called[0]);

        interactor.executeViewProfile();

    }
}
