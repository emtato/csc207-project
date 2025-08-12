package app;

import data_access.*;

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

    /**
     * Builds an instance of the App using File daos.
     */
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
                // .addMapView()
                .addExploreView()
                .addCreatePostView()
                .addCreateClubView();

        // create the use case builder with the File daos
        PostCommentsLikesDataAccessObject postDAO = FilePostCommentLikesDataAccessObject.getInstance();
        useCaseBuilder = new UseCaseBuilder(
                new FileClubsDataAccessObject(postDAO),
                FileUserDataAccessObject.getInstance(),
                postDAO,
                viewBuilder);

        // add the use cases and build the app
        return useCaseBuilder
                //.setSessionUserDataAccessObject()
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
                .addSpecificClubUseCase()
                .addCreateClubUseCase()
                .addClubUseCase()
                .build();
    }

    /**
     * Builds an instance of the App using DB daos.
     */
    public JFrame buildDBApp() {
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
                //.addMapView()
                .addExploreView()
                .addCreatePostView()
                .addCreateClubView();

        // create the use case builder with the DB daos
        PostCommentsLikesDataAccessObject postDAO = DBPostCommentLikesDataAccessObject.getInstance();
        useCaseBuilder = new UseCaseBuilder(
                new FileClubsDataAccessObject(postDAO),
                DBUserDataAccessObject.getInstance(),
                postDAO,
                viewBuilder);

        // add the use cases and build the app
        return useCaseBuilder
                //.setSessionUserDataAccessObject()
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
                .addSpecificClubUseCase()
                .addCreateClubUseCase()
                .addClubUseCase()
                .build();
    }

    /**
     * Builds an instance of the App using In Memory daos.
     */
    public JFrame buildInMemoryApp() {
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
                //.addMapView()
                .addExploreView()
                .addCreatePostView()
                .addCreateClubView();

        // create the use case builder with the In Memory daos
        PostCommentsLikesDataAccessObject postDAO = InMemoryPostCommentLikesDataAccessObject.getInstance();
        useCaseBuilder = new UseCaseBuilder(
                new InMemoryClubsDataAccessObject(postDAO),
                InMemoryUserDataAccessObject.getInstance(),
                postDAO,
                viewBuilder);

        // add the use cases and build the app
        return useCaseBuilder
                //.setSessionUserDataAccessObject()
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
                .addSpecificClubUseCase()
                .addCreateClubUseCase()
                .addClubUseCase()
                .build();
    }
}
