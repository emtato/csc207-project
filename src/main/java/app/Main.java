package app;

import javax.swing.JFrame;
import java.awt.*;


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
                //.addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .addNoteUseCase()
                .addPostView()
                .build();
        application.setPreferredSize(new Dimension(1920, 1080));
        application.pack();
        application.setVisible(true);
    }
}
