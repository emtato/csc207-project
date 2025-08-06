package app;

import data_access.DBClubsDataAccessObject;
import data_access.FilePostCommentLikesDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;

import javax.swing.JFrame;

public class AppDirector {
    // static instance of an AppDirector
    private static AppDirector instance;

    private ViewBuilder viewBuilder;
    private UseCaseBuilder useCaseBuilder;

    private AppDirector() {
    }

    public static AppDirector getInstance() {
        if (AppDirector.instance == null) {
            AppDirector.instance = new AppDirector();
        }
        return AppDirector.instance;
    }

    public JFrame buildFileApp() {
        // build the views
        viewBuilder = new ViewBuilder()
                .addSignupView()
                .addLoginView()
                .addHomePageView()
                .addProfileView()
                .addEditProfileView()
                .addSettingsView()
                .addPostView()
                .addManageFollowersView()
                .addManageFollowingView()
                .addClubHomePageView()
                .addSpecificClubView()
                .addEventsView()
                .addNotificationsView()
                .addMapView()
                .addExploreView()
                .addCreatePostView();

        // create the use case builder with the File daos
        PostCommentsLikesDataAccessObject postDAO = FilePostCommentLikesDataAccessObject.getInstance();
        useCaseBuilder = new UseCaseBuilder(
                new DBClubsDataAccessObject(postDAO),
                FileUserDataAccessObject.getInstance(),
                postDAO,
                viewBuilder);

        // add the use cases and build the app
        return useCaseBuilder
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .addSettingsUseCase()
                .addProfileUseCase()
                .addEditProfileUseCase()
                .addManageFollowingUseCase()
                .addManageFollowersUseCase()
                .addDeleteAccountUseCase()
                .addLikePostUseCase()
                .addWriteCommentUseCase()
                .addGetCommentsUseCase()
                .addAnalyzeRecipeUseCase()
                .addFetchPostUseCase()
                .addCreatePostUseCase()
                .build();
    }
}
