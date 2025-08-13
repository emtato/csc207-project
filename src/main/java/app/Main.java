package app;

import view.GUIConstants;

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
                .addClubUseCase()  // Added this line to initialize ClubController
                .addHomePageView()
                .addExploreView()
                .addEventsView()
                .addProfileView()
                .addEditProfileView()
                .addSettingsView()
                .addManageFollowersView()
                .addManageFollowingView()
                .addPostView()
                .addSpecificClubView()
                .addSpecificClubUseCase()  // Moved right after addSpecificClubView
                .addCreateClubView()
                .addCreatePostView()
                .addCreatePostUseCase()
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
                .addFetchReviewUseCase()
                .addCreateClubUseCase()
                .setSessionUserDataAccessObject()
                .build();
        application.setPreferredSize(new Dimension(GUIConstants.WINDOW_WIDTH, GUIConstants.WINDOW_HEIGHT));
        application.pack();
        application.setVisible(true);
    }
}
