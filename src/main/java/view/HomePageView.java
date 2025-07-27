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
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        this.viewManagerModel = viewManagerModel;

        JPanel profile = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        profile.setMaximumSize(new Dimension(182, 50));
        profile.setBackground(GUIConstants.WHITE);
        profile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profile.add(new view.JLabel("User ABC", 19, GUIConstants.BLACK, Font.BOLD));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GUIConstants.WHITE);
        Dimension dimension = new Dimension(1000, 500);
        header.setPreferredSize(dimension);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(null);
        north.add(new view.JLabel("Home", 48, GUIConstants.BLACK, Font.BOLD),
                BorderLayout.WEST);
        header.add(north, BorderLayout.NORTH);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
//        c.weightx = 1.0;
//        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(profile, c);

//        c.gridx = 1;
//        c.gridy = 0;
//        c.weighty = 1.0;
//        c.weightx = 1.0;
//        c.fill = GridBagConstraints.BOTH;
//        this.add(north, c);

        c.gridy = 1;
        c.weighty = 1;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        this.add(header, c);


        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        menuBar.setPreferredSize(new Dimension(500, 120));
        c.gridy = 2;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL; // Allow menuBar to use full width
        c.anchor = GridBagConstraints.WEST;
        this.add(menuBar, c);

    }

    public String getViewName() {
        return viewName;
    }

}
