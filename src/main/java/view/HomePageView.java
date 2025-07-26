package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JLabel;


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
    private HomePageController homePageController;

    private final JButton signUp;
    private final JButton toLogin;
    private final JButton toClubs;
    private final JButton toSettings;


    public HomePageView(HomePageViewModel homePageViewModel) {
//        System.out.println("ClubHomePageView constructor called");
        this.homePageViewModel = homePageViewModel;
//        homePageViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel("WAAAAAAAAAAAAAAAAAAAAA"); //get recipe/post title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttons = new JPanel();
        toLogin = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        buttons.add(toLogin);
        toClubs = new JButton("To Clubs");
        buttons.add(toClubs);
        signUp = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        buttons.add(signUp);
        toSettings = new JButton("Settings");
        buttons.add(toSettings);

        signUp.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        homePageController.switchToSignUpView();
                    }
                }
        );


        toLogin.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        homePageController.switchToLoginView();
                    }
                }
        );

        toClubs.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        homePageController.switchToClubView();
                    }
                }
        );
        toSettings.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { homePageController.switchToSettingsView(); }
                }
        );


        this.add(title);
        this.add(buttons);

    }

    public String getViewName() {
        return viewName;
    }

    public void setHomePageController(HomePageController controller) {
        this.homePageController = controller;
    }
}
