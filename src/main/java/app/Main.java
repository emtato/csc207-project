package app;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * The Main class of our application.
 */
public class Main {
    /**
     * Builds and runs the CA architecture of the application.
     *
     * @param args unused arguments
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addNoteView()
                .addClubHomePageView()
                .addHomePageView()
                .addExploreView()
                .addEventsView()
                .addNotificationsView()
                .addProfileView()
                .addEditProfileView()
                .addSettingsView()
                .addManageFollowersView()
                .addManageFollowingView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .addNoteUseCase()
                .addEditProfileUseCase()
                .addSettingsUseCase()
                .addManageFollowersUseCase()
                .addManageFollowingUseCase()
                .addProfileUseCase()
                .addPostView()
                .addSpecificClubView()
                .addCreateClubView()
                .addCreatePostView()
                .build();
        application.setPreferredSize(new Dimension(1440, 900));
        application.pack();
        application.setVisible(true);
    }
}
