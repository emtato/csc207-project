// src/main/java/view/MenuBarPanel.java
package view;

import javax.swing.*;
import java.awt.*;
import interface_adapter.ViewManagerModel;

public class MenuBarPanel extends JPanel {
    public MenuBarPanel(ViewManagerModel viewManagerModel) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton homeButton = new JButton("Home");
        JButton clubsButton = new JButton("Clubs");
        JButton settingsButton = new JButton("Settings");
        JButton eventsButton = new JButton("Events");
        JButton recipeButton = new JButton("Recipes");



        homeButton.addActionListener(e -> viewManagerModel.setState("homepage view"));
        clubsButton.addActionListener(e -> viewManagerModel.setState("club view"));
        settingsButton.addActionListener(e -> viewManagerModel.setState("settings"));
        eventsButton.addActionListener(e -> viewManagerModel.setState("explore events view"));
        recipeButton.addActionListener(e -> viewManagerModel.setState("recipe view"));


        add(homeButton);
        add(clubsButton);
        add(settingsButton);
        add(eventsButton);
        add(recipeButton);


        //em test
    }
}
