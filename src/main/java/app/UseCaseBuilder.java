package app;

import data_access.DBClubsDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import data_access.spoonacular.SpoonacularAPI;
import entity.CreateAccount;
import entity.UserFactory;
import interface_adapter.analyze_recipe.AnalyzeRecipeController;
import interface_adapter.analyze_recipe.AnalyzeRecipePresenter;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordPresenter;
import interface_adapter.delete_account.DeleteAccountController;
import interface_adapter.delete_account.DeleteAccountPresenter;
import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfilePresenter;
import interface_adapter.fetch_post.FetchPostController;
import interface_adapter.get_comments.GetCommentsController;
import interface_adapter.get_comments.GetCommentsPresenter;
import interface_adapter.like_post.LikePostController;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.manage_followers.ManageFollowersController;
import interface_adapter.manage_followers.ManageFollowersPresenter;
import interface_adapter.manage_following.ManageFollowingController;
import interface_adapter.manage_following.ManageFollowingPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.toggle_settings.SettingsController;
import interface_adapter.toggle_settings.SettingsPresenter;
import interface_adapter.view_profile.ProfileController;
import interface_adapter.view_profile.ProfilePresenter;
import interface_adapter.write_comment.WriteCommentController;
import use_case.analyze_recipe.AnalyzeRecipeInputBoundary;
import use_case.analyze_recipe.AnalyzeRecipeInteractor;
import use_case.analyze_recipe.AnalyzeRecipeOutputBoundary;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.comment.CommentPostInputBoundary;
import use_case.comment.CommentPostInteractor;
import use_case.delete_account.DeleteAccountInputBoundary;
import use_case.delete_account.DeleteAccountInteractor;
import use_case.delete_account.DeleteAccountOutputBoundary;
import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInteractor;
import use_case.edit_profile.EditProfileOutputBoundary;
import use_case.fetch_post.FetchPostInputBoundary;
import use_case.fetch_post.FetchPostInteractor;
import use_case.get_comments.GetCommentsInputBoundary;
import use_case.get_comments.GetCommentsInteractor;
import use_case.get_comments.GetCommentsOutputBoundary;
import use_case.like_post.LikePostInputBoundary;
import use_case.like_post.LikePostInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.manage_followers.ManageFollowersInputBoundary;
import use_case.manage_followers.ManageFollowersInteractor;
import use_case.manage_followers.ManageFollowersOutputBoundary;
import use_case.manage_following.ManageFollowingInputBoundary;
import use_case.manage_following.ManageFollowingInteractor;
import use_case.manage_following.ManageFollowingOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.toggle_settings.SettingsInputBoundary;
import use_case.toggle_settings.SettingsInteractor;
import use_case.toggle_settings.SettingsOutputBoundary;
import use_case.view_profile.ProfileInputBoundary;
import use_case.view_profile.ProfileInteractor;
import use_case.view_profile.ProfileOutputBoundary;

import javax.swing.*;

public class UseCaseBuilder {
    // DAOS
    private DBClubsDataAccessObject dbClubsDataAccessObject;
    private UserDataAccessObject userDataAccessObject;
    private PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject;

    // View Builder
    private ViewBuilder viewBuilder;
    private UserFactory userFactory = new CreateAccount();

    public UseCaseBuilder(DBClubsDataAccessObject clubsDataAccessObject,
                          UserDataAccessObject userDataAccessObject,
                          PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject,
                          ViewBuilder viewBuilder) {
        this.dbClubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.postCommentsLikesDataAccessObject = postCommentsLikesDataAccessObject;
        this.viewBuilder = viewBuilder;
    }

    public JFrame build(){
        return viewBuilder.build();
    }

    /**
     * Adds the Signup Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(
                viewBuilder.getViewManagerModel(),
                viewBuilder.getSignupViewModel(),
                viewBuilder.getLoginViewModel());
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        viewBuilder.getSignupView().setSignupController(controller);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(
                viewBuilder.getViewManagerModel(),
                viewBuilder.getLoginViewModel(),
                viewBuilder.getProfileViewModel(),
                viewBuilder.getSettingsViewModel());
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, postCommentsLikesDataAccessObject, loginOutputBoundary);

        final LoginController loginController = new LoginController(loginInteractor);
        viewBuilder.getLoginView().setLoginController(loginController);
        return this;
    }

    /**
     * Adds the Change Password Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary =
                new ChangePasswordPresenter(viewBuilder.getSettingsViewModel());

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        final ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);
        viewBuilder.getSettingsView().setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(
                viewBuilder.getViewManagerModel(),
                viewBuilder.getLoginViewModel());

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        viewBuilder.getSettingsView().setLogoutController(logoutController);
        return this;
    }

    /**
     * Adds the Change Settings Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addSettingsUseCase() {
        final SettingsOutputBoundary settingsOutputBoundary = new SettingsPresenter(
                viewBuilder.getViewManagerModel(),
                viewBuilder.getSettingsViewModel());

        final SettingsInputBoundary settingsInteractor =
                new SettingsInteractor(userDataAccessObject, settingsOutputBoundary);
        final SettingsController settingsController = new SettingsController(settingsInteractor);
        viewBuilder.getSettingsView().setSettingsController(settingsController);
        return this;
    }

    /**
     * Adds the Profile Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addProfileUseCase() {
        final ProfileOutputBoundary profileOutputBoundary = new ProfilePresenter(
                viewBuilder.getViewManagerModel(),
                viewBuilder.getProfileViewModel(),
                viewBuilder.getEditProfileViewModel(),
                viewBuilder.getManageFollowingViewModel(),
                viewBuilder.getManageFollowersViewModel());
        final ProfileInputBoundary profileInteractor =
                new ProfileInteractor(userDataAccessObject, postCommentsLikesDataAccessObject, profileOutputBoundary);
        final ProfileController profileController = new ProfileController(profileInteractor);
        viewBuilder.getProfileView().setProfileController(profileController);
        viewBuilder.getEditProfileView().setProfileController(profileController);
        viewBuilder.getManageFollowingView().setProfileController(profileController);
        viewBuilder.getManageFollowersView().setProfileController(profileController);
        return this;
    }

    /**
     * Adds the Edit Profile Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addEditProfileUseCase() {
        final EditProfileOutputBoundary editProfileOutputBoundary = new EditProfilePresenter(
                viewBuilder.getViewManagerModel(),
                viewBuilder.getEditProfileViewModel(),
                viewBuilder.getProfileViewModel());
        final EditProfileInputBoundary editProfileInteractor =
                new EditProfileInteractor(userDataAccessObject, editProfileOutputBoundary);
        final EditProfileController editProfileController = new EditProfileController(editProfileInteractor);
        viewBuilder.getEditProfileView().setEditProfileController(editProfileController);
        return this;
    }

    /**
     * Adds the Manage Following Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addManageFollowingUseCase() {
        final ManageFollowingOutputBoundary manageFollowingOutputBoundary = new ManageFollowingPresenter(
                viewBuilder.getViewManagerModel(),
                viewBuilder.getManageFollowingViewModel(),
                viewBuilder.getProfileViewModel());
        final ManageFollowingInputBoundary manageFollowingInteractor =
                new ManageFollowingInteractor(userDataAccessObject, manageFollowingOutputBoundary);
        final ManageFollowingController manageFollowingController =
                new ManageFollowingController(manageFollowingInteractor);
        viewBuilder.getManageFollowingView().setManageFollowingController(manageFollowingController);
        return this;
    }

    /**
     * Adds the Manage Followers Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addManageFollowersUseCase() {
        final ManageFollowersOutputBoundary manageFollowersOutputBoundary = new ManageFollowersPresenter(
                viewBuilder.getViewManagerModel(),
                viewBuilder.getManageFollowersViewModel(),
                viewBuilder.getProfileViewModel());
        final ManageFollowersInputBoundary manageFollowersInteractor =
                new ManageFollowersInteractor(userDataAccessObject, manageFollowersOutputBoundary);
        final ManageFollowersController manageFollowersController =
                new ManageFollowersController(manageFollowersInteractor);
        viewBuilder.getManageFollowersView().setManageFollowersController(manageFollowersController);
        return this;
    }

    /**
     * Adds the Delete Account Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addDeleteAccountUseCase() {
        final DeleteAccountOutputBoundary deleteAccountOutputBoundary = new DeleteAccountPresenter(
                viewBuilder.getViewManagerModel(), viewBuilder.getLoginViewModel());
        final DeleteAccountInputBoundary deleteAccountInteractor =
                new DeleteAccountInteractor(userDataAccessObject, postCommentsLikesDataAccessObject,
                        deleteAccountOutputBoundary);
        final DeleteAccountController deleteAccountController =
                new DeleteAccountController(deleteAccountInteractor);
        viewBuilder.getSettingsView().setDeleteAccountController(deleteAccountController);
        return this;
    }

    /**
     * Adds the Like Post Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addLikePostUseCase() {
        //final LikePostOutputBoundary likePostOutputBoundary = new LikePostPresenter(viewManagerModel);
        final LikePostInputBoundary likePostInteractor = new LikePostInteractor(postCommentsLikesDataAccessObject);
        final LikePostController likePostController =
                new LikePostController(likePostInteractor);
        viewBuilder.getPostView().setLikePostController(likePostController);
        viewBuilder.getHomePageView().setLikePostController(likePostController);
        viewBuilder.getProfileView().setLikePostController(likePostController);
        viewBuilder.getClubHomePageView().setLikePostController(likePostController);
        viewBuilder.getSpecificClubView().setLikePostController(likePostController);
        return this;
    }

    /**
     * Adds the Write Comment Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addWriteCommentUseCase() {
        //final WriteCommentOutputBoundary writeCommentOutputBoundary = new WriteCommentPresenter(viewManagerModel);
        final CommentPostInputBoundary writeCommentInteractor =
                new CommentPostInteractor(postCommentsLikesDataAccessObject);
        final WriteCommentController writeCommentController =
                new WriteCommentController(writeCommentInteractor);
        viewBuilder.getPostView().setWriteCommentController(writeCommentController);
        return this;
    }

    /**
     * Adds the Get Comments Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addGetCommentsUseCase() {
        final GetCommentsOutputBoundary getCommentsOutputBoundary = new GetCommentsPresenter(
                viewBuilder.getGetCommentsViewModel());
        final GetCommentsInputBoundary getCommentsInteractor =
                new GetCommentsInteractor(postCommentsLikesDataAccessObject, getCommentsOutputBoundary);
        final GetCommentsController getCommentsController =
                new GetCommentsController(getCommentsInteractor);
        viewBuilder.getPostView().setGetCommentsController(getCommentsController);
        return this;
    }

    /**
     * Adds the Analyze Recipe Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addAnalyzeRecipeUseCase() {
        final AnalyzeRecipeOutputBoundary analyzeRecipeOutputBoundary =
                new AnalyzeRecipePresenter(viewBuilder.getAnalyzeRecipeViewModel());
        final AnalyzeRecipeInputBoundary analyzeRecipeInteractor =
                new AnalyzeRecipeInteractor(new SpoonacularAPI(), analyzeRecipeOutputBoundary);
        final AnalyzeRecipeController analyzeRecipeController =
                new AnalyzeRecipeController(analyzeRecipeInteractor);
        viewBuilder.getPostView().setAnalyzeRecipeController(analyzeRecipeController);
        return this;
    }

    /**
     * Adds the Fetch Post Use Case to the application.
     *
     * @return this builder
     */
    public UseCaseBuilder addFetchPostUseCase() {
        //final FetchPostOutputBoundary fetchPostOutputBoundary = new FetchPostPresenter();
        final FetchPostInputBoundary fetchPostInteractor = new FetchPostInteractor(postCommentsLikesDataAccessObject);
        final FetchPostController fetchPostController = new FetchPostController(fetchPostInteractor);
        viewBuilder.getHomePageView().setFetchPostController(fetchPostController);
        return this;
    }
}
