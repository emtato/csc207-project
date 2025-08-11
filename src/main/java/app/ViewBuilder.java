package app;

import data_access.FileUserDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.analyze_recipe.AnalyzeRecipeViewModel;
import interface_adapter.clubs_home.ClubViewModel;
import interface_adapter.create_club.CreateClubViewModel;
import interface_adapter.create_post_view.CreatePostViewModel;
import interface_adapter.edit_profile.EditProfileViewModel;
import interface_adapter.get_comments.GetCommentsViewModel;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.manage_followers.ManageFollowersViewModel;
import interface_adapter.manage_following.ManageFollowingViewModel;
import interface_adapter.map.MapViewModel;
import interface_adapter.post_view.PostViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.specific_club.SpecificClubViewModel;
import interface_adapter.toggle_settings.SettingsViewModel;
import interface_adapter.view_profile.ProfileViewModel;
import view.*;
import view.map.MapView;
import view.map.RestaurantSearch;

import javax.swing.*;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class ViewBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final UserFactory userFactory = new CreateAccount();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // Views and view models
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private HomePageView homePageView;
    private HomePageViewModel homePageViewModel;
    private ProfileView profileView;
    private ProfileViewModel profileViewModel;
    private EditProfileView editProfileView;
    private EditProfileViewModel editProfileViewModel;
    private SettingsView settingsView;
    private SettingsViewModel settingsViewModel;
    private ClubViewModel clubViewModel;
    private CreateClubViewModel createClubViewModel;
    private PostView postView;
    private PostViewModel postViewModel;
    private ManageFollowersView manageFollowersView;
    private ManageFollowersViewModel manageFollowersViewModel;
    private ManageFollowingView manageFollowingView;
    private ManageFollowingViewModel manageFollowingViewModel;
    private GetCommentsViewModel getCommentsViewModel;
    private AnalyzeRecipeViewModel analyzeRecipeViewModel;
    private ClubHomePageView clubHomePageView;
    private SpecificClubView specificClubView;
    private SpecificClubViewModel specificClubViewModel;
    private CreateClubView createClubView;
    private ExploreEventsView exploreEventsView;
    private MapView mapView;
    private MapViewModel mapViewModel;
    private ExploreView exploreView;
    private CreatePostViewModel createPostViewModel;
    private CreateNewPostView createNewPostView;

    public ViewBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public JFrame build() {
        final JFrame application = new JFrame("Munchables");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // Make sure we start with the login view, just like in Main.java
        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();
        return application;
    }

    /**
     * Adds the Signup View to the application.
     *
     * @return this builder
     */
    public ViewBuilder addSignupView() {
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
    public ViewBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel, viewManagerModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Adds the HomePage View to the application.
     *
     * @return this builder
     */
    public ViewBuilder addHomePageView() {
        homePageViewModel = new HomePageViewModel();
        homePageView = new HomePageView(viewManagerModel, homePageViewModel);
        cardPanel.add(homePageView, homePageView.getViewName());
        viewManagerModel.setHomePageView(homePageView);
        return this;
    }

    /**
     * Adds the Profile View to the application.
     *
     * @return this builder
     */
    public ViewBuilder addProfileView() {
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
    public ViewBuilder addEditProfileView() {
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
    public ViewBuilder addSettingsView() {
        settingsViewModel = new SettingsViewModel();
        settingsView = new SettingsView(settingsViewModel, viewManagerModel);
        cardPanel.add(settingsView, settingsView.getViewName());
        return this;
    }

    /**
     * Adds the Post View to the application.
     *
     * @return this builder
     */
    public ViewBuilder addPostView() {
        Post trialpost = new Post(new Account("meow", "woof"), 123134344413l, "posttitle", "desc");
        trialpost.setTitle("goon blean");
        trialpost.setDescription("1. smash 4 glorbles of bean paste into a sock, microwave till it sings\n" +
                "2.sprinkle in 2 blinks of mystery flakes, scream gently\n" +
                "3.serve upside-down on a warm tile");
        postViewModel = new PostViewModel();
        getCommentsViewModel = new GetCommentsViewModel();
        analyzeRecipeViewModel = new AnalyzeRecipeViewModel();
        postView = new PostView(viewManagerModel, trialpost, getCommentsViewModel, analyzeRecipeViewModel);
        cardPanel.add(postView, postView.getViewName());
        viewManagerModel.setPostView(postView);
        return this;
    }

    /**
     * Adds the Manage Followers View to the application.
     *
     * @return this builder
     */
    public ViewBuilder addManageFollowersView() {
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
    public ViewBuilder addManageFollowingView() {
        manageFollowingViewModel = new ManageFollowingViewModel();
        manageFollowingView = new ManageFollowingView(manageFollowingViewModel);
        cardPanel.add(manageFollowingView, manageFollowingView.getViewName());
        return this;
    }

    /**
     * Adds the ClubHomePage View to the application.
     *
     * @return this builder
     */
    public ViewBuilder addClubHomePageView() {
        clubViewModel = new ClubViewModel();
        specificClubViewModel = new SpecificClubViewModel();
        clubHomePageView = new ClubHomePageView(
                viewManagerModel,
                clubViewModel,
                null,  // ClubController will be set in addClubUseCase
                cardPanel,
                specificClubViewModel,
                null   // SpecificClubController will be set in addSpecificClubUseCase
        );
        cardPanel.add(clubHomePageView, clubHomePageView.getViewName());
        return this;
    }

    public ViewBuilder addEventsView() {
        exploreEventsView = new ExploreEventsView(viewManagerModel);
        cardPanel.add(exploreEventsView, exploreEventsView.getViewName());
        return this;
    }


    /**
     * Adds Map View to the application
     *
     * @return this builder
     */
    public ViewBuilder addMapView() {
        Restaurant exampleRestaurant = new Restaurant(new ArrayList<String>(Arrays
                .asList("French", "Italian", "Swiss")), "Toronto");
        mapViewModel = new MapViewModel();
        mapView = new MapView(mapViewModel, exampleRestaurant);
        //cardPanel.add(mapView, mapView.getViewName());
        return this;
    }

    /**
     * Adds Explore View to the application
     *
     * @return this builder
     */
    public ViewBuilder addExploreView() {
        Session.setUserDataAccessObject(FileUserDataAccessObject.getInstance()); //required here since explore view calls session.getuser before i can add it in usecasebuilder
        exploreView = new ExploreView(viewManagerModel, cardPanel);
        cardPanel.add(exploreView, exploreView.getViewName());
        return this;
    }

    public ViewBuilder addCreatePostView() {
        createPostViewModel = new CreatePostViewModel();
        createNewPostView = new CreateNewPostView(viewManagerModel, createPostViewModel);

        cardPanel.add(createNewPostView, createNewPostView.getViewName());
        viewManagerModel.setCreateNewPostView(createNewPostView);
        return this;
    }

    // TODO: add these views later, cant add them rn because they depend on daos rn
    public ViewBuilder addSpecificClubView() {
        // Create the default club for initial view
        Club defaultClub = new Club("Default Club", "Default Description", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1, new ArrayList<>());

        specificClubViewModel = new SpecificClubViewModel();
        specificClubView = new SpecificClubView(
                viewManagerModel,
                cardPanel,
                defaultClub,
                specificClubViewModel,
                null
        );

        cardPanel.add(specificClubView, specificClubView.getViewName());
        viewManagerModel.setSpecificClubView(specificClubView);  // Store the view in ViewManagerModel
        return this;
    }

    public ViewBuilder addCreateClubView() {
        createClubViewModel = new CreateClubViewModel();
        createClubView = new CreateClubView(
                viewManagerModel,
                null  // Controller will be set in addCreateClubUseCase
                , createClubViewModel,
                Session.getCurrentAccount()
        );

        cardPanel.add(createClubView, createClubView.getViewName());
        return this;
    }


    public ViewManagerModel getViewManagerModel() {
        return viewManagerModel;
    }

    public SignupView getSignupView() {
        return signupView;
    }

    public SignupViewModel getSignupViewModel() {
        return signupViewModel;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public LoginViewModel getLoginViewModel() {
        return loginViewModel;
    }

    public HomePageView getHomePageView() {
        return homePageView;
    }

    public HomePageViewModel getHomePageViewModel() {
        return homePageViewModel;
    }

    public ProfileView getProfileView() {
        return profileView;
    }

    public ProfileViewModel getProfileViewModel() {
        return profileViewModel;
    }

    public EditProfileView getEditProfileView() {
        return editProfileView;
    }

    public EditProfileViewModel getEditProfileViewModel() {
        return editProfileViewModel;
    }

    public SettingsView getSettingsView() {
        return settingsView;
    }

    public SettingsViewModel getSettingsViewModel() {
        return settingsViewModel;
    }

    public PostView getPostView() {
        return postView;
    }

    public PostViewModel getPostViewModel() {
        return postViewModel;
    }

    public GetCommentsViewModel getGetCommentsViewModel() {
        return getCommentsViewModel;
    }

    public AnalyzeRecipeViewModel getAnalyzeRecipeViewModel() {
        return analyzeRecipeViewModel;
    }

    public ManageFollowersView getManageFollowersView() {
        return manageFollowersView;
    }

    public ManageFollowersViewModel getManageFollowersViewModel() {
        return manageFollowersViewModel;
    }

    public ManageFollowingView getManageFollowingView() {
        return manageFollowingView;
    }

    public ManageFollowingViewModel getManageFollowingViewModel() {
        return manageFollowingViewModel;
    }

    public ClubHomePageView getClubHomePageView() {
        return clubHomePageView;
    }

    public SpecificClubView getSpecificClubView() {
        return specificClubView;
    }

    public ExploreEventsView getExploreEventsView() {
        return exploreEventsView;
    }

    public ClubViewModel getClubViewModel() {
        return clubViewModel;
    }

    public CreateClubView getCreateClubView() {
        return createClubView;
    }

    public CreateClubViewModel getCreateClubViewModel() {
        return createClubViewModel;
    }

    public CreatePostViewModel getCreatePostViewModel() {
        return createPostViewModel;
    }


    public MapView getMapView() {
        return mapView;
    }

    public MapViewModel getMapViewModel() {
        return mapViewModel;
    }

    public ExploreView getExploreView() {
        return exploreView;
    }

    public CreateNewPostView getCreateNewPostView() {
        return createNewPostView;
    }

    public SpecificClubViewModel getSpecificClubViewModel() {
        return specificClubViewModel;
    }
}
