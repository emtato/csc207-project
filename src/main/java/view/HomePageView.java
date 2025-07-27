package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JLabel;


import interface_adapter.ViewManagerModel;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;

/**
 * The View for the Home Page.
 */
public class HomePageView extends JPanel {

    private final String viewName = "homepage view";
    private final ViewManagerModel viewManagerModel;


    public HomePageView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;


        JLabel title = new JLabel("Home page"); //get recipe/post title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);



        this.add(title);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        add(menuBar, BorderLayout.NORTH);

    }

    public String getViewName() {
        return viewName;
    }

}
