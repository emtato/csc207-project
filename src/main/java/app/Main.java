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
        final AppBuilder appBuilder = AppBuilder.getInstance();
        final JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
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
                .addPostView()
                .addSpecificClubView()
                .addCreateClubView()
                .addCreatePostView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addDeleteAccountUseCase()
                .addLogoutUseCase()
                .addEditProfileUseCase()
                .addSettingsUseCase()
                .addManageFollowersUseCase()
                .addManageFollowingUseCase()
                .addProfileUseCase()
                .addLikePostUseCase()
                .addWriteCommentUseCase()
                .addGetCommentsUseCase()
                .addAnalyzeRecipeUseCase()
                .addFetchPostUseCase()
                .build();
        application.setPreferredSize(new Dimension(1440, 900));
        application.pack();
        application.setVisible(true);
    }
}
