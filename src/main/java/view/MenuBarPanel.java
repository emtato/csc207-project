// src/main/java/view/MenuBarPanel.java
package view;

import javax.swing.*;
import java.awt.*;

import interface_adapter.ViewManagerModel;

public class MenuBarPanel extends JPanel {
    public MenuBarPanel(ViewManagerModel viewManagerModel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        Dimension buttonSize = new Dimension(GUIConstants.MAIN_BUTTON1_WIDTH, GUIConstants.MAIN_BUTTON1_HEIGHT);

        JButton homeButton = new JButton("Home");
        homeButton.setPreferredSize(buttonSize);
        homeButton.setMaximumSize(buttonSize);

        JButton clubsButton = new JButton("Clubs");
        clubsButton.setPreferredSize(buttonSize);
        clubsButton.setMaximumSize(buttonSize);

        JButton settingsButton = new JButton("Settings");
        settingsButton.setPreferredSize(buttonSize);
        settingsButton.setMaximumSize(buttonSize);

        JButton exploreButton = new JButton("Explore");
        exploreButton.setPreferredSize(buttonSize);
        exploreButton.setMaximumSize(buttonSize);

        JButton notifcationsButton = new JButton("Notifcations");
        notifcationsButton.setPreferredSize(buttonSize);
        notifcationsButton.setMaximumSize(buttonSize);


        homeButton.addActionListener(e -> viewManagerModel.setState("homepage view"));
        clubsButton.addActionListener(e -> viewManagerModel.setState("club view"));
        settingsButton.addActionListener(e -> viewManagerModel.setState("settings"));
        exploreButton.addActionListener(e -> viewManagerModel.setState("explore view"));
        notifcationsButton.addActionListener(e -> viewManagerModel.setState("notifications view"));

//        add(Box.createRigidArea(new Dimension(10, 0)));

        //the bar was overflowing horizontally for many menus and i couldnt find out why, width changed from 100-> 0 and
        // that seems to have worked
        add(homeButton);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(clubsButton);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(exploreButton);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(notifcationsButton);

    }
}
