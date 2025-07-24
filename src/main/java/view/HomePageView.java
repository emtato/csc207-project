package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JLabel;


import interface_adapter.ViewManagerModel;
import interface_adapter.clubs.ClubViewModel;
import interface_adapter.clubs.ClubState;
import interface_adapter.homepage.HomePageController;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;

/**
 * The View for the Home Page.
 */
public class HomePageView extends JPanel {

    private final String viewName = "homepage view";
    private final HomePageViewModel homePageViewModel;
    private final ViewManagerModel viewManagerModel;
    private HomePageController homePageController;

//    private final JButton signUp;
//    private final JButton toLogin;
//    private final JButton toClubs;
//    private final JButton toSettings;


    public HomePageView(HomePageViewModel homePageViewModel, ViewManagerModel viewManagerModel) {
//        System.out.println("ClubHomePageView constructor called");
        this.homePageViewModel = homePageViewModel;
        this.viewManagerModel = viewManagerModel;
//        homePageViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel("Home page"); //get recipe/post title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);



        this.add(title);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        add(menuBar, BorderLayout.NORTH);

    }

    public String getViewName() {
        return viewName;
    }

    public void setHomePageController(HomePageController controller) {
        this.homePageController = controller;
    }
}
