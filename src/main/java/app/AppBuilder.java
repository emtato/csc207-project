package app;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.JFrame;

import data_access.*;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_post_view.CreatePostViewModel;
import interface_adapter.delete_account.DeleteAccountController;
import interface_adapter.delete_account.DeleteAccountPresenter;
import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfilePresenter;
import interface_adapter.manage_followers.ManageFollowersController;
import interface_adapter.manage_followers.ManageFollowersPresenter;
import interface_adapter.manage_following.ManageFollowingController;
import interface_adapter.manage_following.ManageFollowingPresenter;
import interface_adapter.map.MapViewModel;
import interface_adapter.post_view.PostViewModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordPresenter;
import interface_adapter.edit_profile.EditProfileViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.manage_followers.ManageFollowersViewModel;
import interface_adapter.manage_following.ManageFollowingViewModel;
import interface_adapter.view_profile.ProfileController;
import interface_adapter.view_profile.ProfilePresenter;
import interface_adapter.view_profile.ProfileViewModel;
import interface_adapter.toggle_settings.SettingsController;
import interface_adapter.toggle_settings.SettingsPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.toggle_settings.SettingsViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.delete_account.DeleteAccountInputBoundary;
import use_case.delete_account.DeleteAccountInteractor;
import use_case.delete_account.DeleteAccountOutputBoundary;
import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInteractor;
import use_case.edit_profile.EditProfileOutputBoundary;
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
import use_case.view_profile.ProfileInputBoundary;
import use_case.view_profile.ProfileInteractor;
import use_case.view_profile.ProfileOutputBoundary;
import use_case.toggle_settings.SettingsInputBoundary;
import use_case.toggle_settings.SettingsInteractor;
import use_case.toggle_settings.SettingsOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;
import view.map.MapView;

/**
 * The AppBuilder class is responsible for putting together the pieces of
 * our CA architecture; piece by piece.
 * <p/>
 * This is done by adding each View and then adding related Use Cases.
 */
// Checkstyle note: you can ignore the "Class Data Abstraction Coupling"
//                  and the "Class Fan-Out Complexity" issues for this lab; we encourage
//                  your team to think about ways to refactor the code to resolve these
//                  if your team decides to work with this as your starter code
//                  for your final project this term.
public class AppBuilder {

    // static instance of an AppBuilder
    private static AppBuilder instance;

    private final DBClubsDataAccessObject clubsDataAccessObject = new DBClubsDataAccessObject();
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final UserFactory userFactory = new CreateAccount();
    private ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private final UserDataAccessObject userDataAccessObject = FileUserDataAccessObject.getInstance();
    private final PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject =
            FilePostCommentLikesDataAccessObject.getInstance();
    private PostViewModel postViewModel;
    private PostView postView;
    private CreatePostViewModel createPostViewModel;
    private CreateNewPostView createNewPostView;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private ProfileViewModel profileViewModel;
    private EditProfileViewModel editProfileViewModel;
    private SettingsViewModel settingsViewModel;
    private ManageFollowersViewModel manageFollowersViewModel;
    private ManageFollowingViewModel manageFollowingViewModel;
    private LoginView loginView;
    private NoteView noteView;
    private ClubHomePageView clubHomePageView;
    private NotificationsView notificationsView;
    private ExploreEventsView exploreEventsView;
    private ExploreView exploreView;
    private HomePageView homePageView;
    private ProfileView profileView;
    private EditProfileView editProfileView;
    private SettingsView settingsView;
    private ManageFollowersView manageFollowersView;
    private ManageFollowingView manageFollowingView;
    private MapViewModel mapViewModel;
    private SpecificClubView specificClubView;
    private CreateClubView createClubView;
    private MapView mapView;

    private AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // The static method that controls access to the singleton AppBuilder instance.
    public static AppBuilder getInstance() {
        if (AppBuilder.instance == null) {
            AppBuilder.instance = new AppBuilder();
        }
        return AppBuilder.instance;
    }

    /**
     * Adds the HomePage View to the application.
     *
     * @return this builder
     */
    public AppBuilder addHomePageView() {
        homePageView = new HomePageView(viewManagerModel, postCommentsLikesDataAccessObject);
        cardPanel.add(homePageView, homePageView.getViewName());
        viewManagerModel.setHomePageView(homePageView);
        return this;
    }

    public AppBuilder addEventsView() {
        exploreEventsView = new ExploreEventsView(viewManagerModel);
        cardPanel.add(exploreEventsView, exploreEventsView.getViewName());
        return this;
    }

    public AppBuilder addSpecificClubView() {
        specificClubView = new SpecificClubView(viewManagerModel, cardPanel);
        cardPanel.add(specificClubView, specificClubView.getViewName());
        return this;
    }

    public AppBuilder addCreateClubView() {
        String currentUsername = userDataAccessObject.getCurrentUsername();
        Account currentUser = (Account) userDataAccessObject.get(currentUsername);
        createClubView = new CreateClubView(viewManagerModel, clubsDataAccessObject, currentUser);
        cardPanel.add(createClubView, createClubView.getViewName());
        return this;
    }

    public AppBuilder addExploreView() {
        exploreView = new ExploreView(viewManagerModel, cardPanel);
        cardPanel.add(exploreView, exploreView.getViewName());
        return this;
    }

    public AppBuilder addNotificationsView() {
        notificationsView = new NotificationsView(viewManagerModel);
        cardPanel.add(notificationsView, notificationsView.getViewName());
        return this;
    }

    /**
     * Adds the Signup View to the application.
     *
     * @return this builder
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel, viewManagerModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    /**
     * Adds the Login View to the application.
     *
     * @return this builder
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel, viewManagerModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Adds the analyze recipe View to the application.
     *
     * @return this builder
     */
    public AppBuilder addPostView() {
        Post trialpost = new Post(new Account("meow", "woof"), 123134344413l, "posttitle", "desc");
        trialpost.setTitle("goon blean");
        trialpost.setDescription("1. smash 4 glorbles of bean paste into a sock, microwave till it sings\n" +
                "2.sprinkle in 2 blinks of mystery flakes, scream gently\n" +
                "3.serve upside-down on a warm tile");
        postViewModel = new PostViewModel();
        //postView = new PostView(postViewModel, viewManagerModel, trialpost);
        postView = new PostView(viewManagerModel, trialpost, postCommentsLikesDataAccessObject);
        cardPanel.add(postView, postView.getViewName());
        viewManagerModel.setPostView(postView);
        return this;
    }

    public AppBuilder addCreatePostView() {
        createPostViewModel = new CreatePostViewModel();
        // TODO: add the use case and move the data access object out of the view and into the interactor
        createNewPostView = new CreateNewPostView(viewManagerModel, postCommentsLikesDataAccessObject, userDataAccessObject);
        cardPanel.add(createNewPostView, createNewPostView.getViewName());
        viewManagerModel.setCreateNewPostView(createNewPostView);
        return this;
    }

    // TODO: implement addMapView()

    /**
     * Adds Map View to the application
     *
     * @return this builder
     */
    public AppBuilder addMapView() {
        Restaurant exampleRestaurant = new Restaurant(new ArrayList<String>(Arrays
                .asList("French", "Italian", "Swiss")), "Toronto");
        mapViewModel = new MapViewModel();
        mapView = new MapView(mapViewModel, exampleRestaurant);
        //cardPanel.add(mapView, mapView.getViewName());
        return this;
    }

    /**
     * Adds the ClubHomePage View to the application.
     *
     * @return this builder
     */
    public AppBuilder addClubHomePageView() {
        clubHomePageView = new ClubHomePageView(viewManagerModel, cardPanel);
        cardPanel.add(clubHomePageView, clubHomePageView.getViewName());
        return this;
    }

    /**
     * Adds the Profile View to the application.
     *
     * @return this builder
     */
    public AppBuilder addProfileView() {
        profileViewModel = new ProfileViewModel();
        profileView = new ProfileView(profileViewModel, viewManagerModel);
        cardPanel.add(profileView, profileView.getViewName());
        return this;
    }

    /**
     * Adds the Edit Profile View to the application.
     *
     * @return this builder
     */
    public AppBuilder addEditProfileView() {
        editProfileViewModel = new EditProfileViewModel();
        editProfileView = new EditProfileView(editProfileViewModel);
        cardPanel.add(editProfileView, editProfileView.getViewName());
        return this;
    }

    /**
     * Adds the Settings View to the application.
     *
     * @return this builder
     */
    public AppBuilder addSettingsView() {
        settingsViewModel = new SettingsViewModel();
        settingsView = new SettingsView(settingsViewModel, viewManagerModel);
        cardPanel.add(settingsView, settingsView.getViewName());
        return this;
    }

    /**
     * Adds the Manage Followers View to the application.
     *
     * @return this builder
     */
    public AppBuilder addManageFollowersView() {
        manageFollowersViewModel = new ManageFollowersViewModel();
        manageFollowersView = new ManageFollowersView(manageFollowersViewModel);
        cardPanel.add(manageFollowersView, manageFollowersView.getViewName());
        return this;
    }

    /**
     * Adds the Manage Following View to the application.
     *
     * @return this builder
     */
    public AppBuilder addManageFollowingView() {
        manageFollowingViewModel = new ManageFollowingViewModel();
        manageFollowingView = new ManageFollowingView(manageFollowingViewModel);
        cardPanel.add(manageFollowingView, manageFollowingView.getViewName());
        return this;
    }

    /**
     * Adds the Signup Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loginViewModel, profileViewModel, settingsViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, postCommentsLikesDataAccessObject, loginOutputBoundary);

        final LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Adds the Change Password Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary =
                new ChangePasswordPresenter(settingsViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        final ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);
        settingsView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        settingsView.setLogoutController(logoutController);
        return this;
    }

    /**
     * Adds the Change Settings Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addSettingsUseCase() {
        final SettingsOutputBoundary settingsOutputBoundary = new SettingsPresenter(viewManagerModel,
                settingsViewModel);

        final SettingsInputBoundary settingsInteractor =
                new SettingsInteractor(userDataAccessObject, settingsOutputBoundary);
        final SettingsController settingsController = new SettingsController(settingsInteractor);
        settingsView.setSettingsController(settingsController);
        return this;
    }

    /**
     * Adds the Profile Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addProfileUseCase() {
        final ProfileOutputBoundary profileOutputBoundary = new ProfilePresenter(viewManagerModel,
                profileViewModel, editProfileViewModel, manageFollowingViewModel, manageFollowersViewModel);
        final ProfileInputBoundary profileInteractor =
                new ProfileInteractor(userDataAccessObject, postCommentsLikesDataAccessObject, profileOutputBoundary);
        final ProfileController profileController = new ProfileController(profileInteractor);
        profileView.setProfileController(profileController);
        editProfileView.setProfileController(profileController);
        manageFollowingView.setProfileController(profileController);
        manageFollowersView.setProfileController(profileController);
        return this;
    }

    /**
     * Adds the Edit Profile Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addEditProfileUseCase() {
        final EditProfileOutputBoundary editProfileOutputBoundary = new EditProfilePresenter(viewManagerModel,
                editProfileViewModel, profileViewModel);
        final EditProfileInputBoundary editProfileInteractor =
                new EditProfileInteractor(userDataAccessObject, editProfileOutputBoundary);
        final EditProfileController editProfileController = new EditProfileController(editProfileInteractor);
        editProfileView.setEditProfileController(editProfileController);
        return this;
    }

    /**
     * Adds the Manage Following Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addManageFollowingUseCase() {
        final ManageFollowingOutputBoundary manageFollowingOutputBoundary = new ManageFollowingPresenter(
                viewManagerModel,
                manageFollowingViewModel,
                profileViewModel);
        final ManageFollowingInputBoundary manageFollowingInteractor =
                new ManageFollowingInteractor(userDataAccessObject, manageFollowingOutputBoundary);
        final ManageFollowingController manageFollowingController =
                new ManageFollowingController(manageFollowingInteractor);
        manageFollowingView.setManageFollowingController(manageFollowingController);
        return this;
    }

    /**
     * Adds the Manage Followers Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addManageFollowersUseCase() {
        final ManageFollowersOutputBoundary manageFollowersOutputBoundary = new ManageFollowersPresenter(
                viewManagerModel,
                manageFollowersViewModel,
                profileViewModel);
        final ManageFollowersInputBoundary manageFollowersInteractor =
                new ManageFollowersInteractor(userDataAccessObject, manageFollowersOutputBoundary);
        final ManageFollowersController manageFollowersController =
                new ManageFollowersController(manageFollowersInteractor);
        manageFollowersView.setManageFollowersController(manageFollowersController);
        return this;
    }

    /**
     * Adds the Delete Account Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addDeleteAccountUseCase() {
        final DeleteAccountOutputBoundary deleteAccountOutputBoundary = new DeleteAccountPresenter(
                viewManagerModel, loginViewModel);
        final DeleteAccountInputBoundary deleteAccountInteractor =
                new DeleteAccountInteractor(userDataAccessObject, postCommentsLikesDataAccessObject,
                        deleteAccountOutputBoundary);
        final DeleteAccountController deleteAccountController =
                new DeleteAccountController(deleteAccountInteractor);
        settingsView.setDeleteAccountController(deleteAccountController);
        return this;
    }

    /**
     * Creates the JFrame for the application and initially sets the SignupView to be displayed.
     *
     * @return the application
     */
    public JFrame build() {
        final JFrame application = new JFrame("HELLO :)");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
