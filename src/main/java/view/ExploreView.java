package view;

import interface_adapter.ViewManagerModel;

import interface_adapter.explore.ExploreViewModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;


import javax.swing.*;

/* Description:
 * explore restaurants, events, and recipes
 */

public class ExploreView extends JPanel {

    private final String viewName = "explore view";
    private final ViewManagerModel viewManagerModel;

    public ExploreView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        JLabel exploreLabel = new JLabel("Explore");
        topPanel.add(exploreLabel);
        this.add(topPanel, BorderLayout.NORTH);



        JLabel exploreRestaurantsLabel = new JLabel("Explore Restaurants");
        JLabel exploreRecipesLabel = new JLabel("Explore Recipes");
        JLabel exploreEventsLabel = new JLabel("Explore Events");

        JPanel exploreRestaurantsPanel = new JPanel();
        JPanel exploreRecipesPanel = new JPanel();
        JPanel exploreEventsPanel = new JPanel();

        exploreRestaurantsPanel.setBackground(Color.PINK);
        exploreRestaurantsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        exploreRestaurantsPanel.setPreferredSize(new Dimension(400, 500));
        exploreRecipesPanel.setBackground(Color.PINK);
        exploreRecipesPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        exploreRecipesPanel.setPreferredSize(new Dimension(400, 500));
        exploreEventsPanel.setBackground(Color.PINK);
        exploreEventsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        exploreEventsPanel.setPreferredSize(new Dimension(400, 500));

        JLabel restaurantsPlaceholder = new JLabel("restaurants go here");
        JLabel recipesPlaceholder = new JLabel("recipes go here");
        JLabel eventsPlaceholder = new JLabel("events go here");

        exploreRestaurantsPanel.add(restaurantsPlaceholder);
        exploreRecipesPanel.add(recipesPlaceholder);
        exploreEventsPanel.add(eventsPlaceholder);

        JButton exploreRestaurantsButton = new JButton("View");

        JButton exploreRecipesButton = new JButton("View");
        exploreRecipesButton.addActionListener(e -> viewManagerModel.setState("recipe view"));

        JButton exploreEventsButton = new JButton("View");
        exploreRecipesButton.addActionListener(e -> viewManagerModel.setState("explore events view"));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(exploreRestaurantsLabel);
        leftPanel.add(exploreRestaurantsPanel);
        leftPanel.add(exploreRestaurantsButton);
        this.add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(exploreRecipesLabel);
        centerPanel.add(exploreRecipesPanel);
        centerPanel.add(exploreRecipesButton);
        this.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(exploreEventsLabel);
        rightPanel.add(exploreEventsPanel);
        rightPanel.add(exploreEventsButton);
        this.add(rightPanel, BorderLayout.EAST);

//        explorePanels.add(exploreRestaurantsLabel);
//        explorePanels.add(exploreRecipesLabel);
//        explorePanels.add(exploreEventsLabel);
//        explorePanels.add(exploreRestaurantsPanel);
//        explorePanels.add(exploreRecipesPanel);
//        explorePanels.add(exploreEventsPanel);
//        explorePanels.add(exploreRestaurantsButton);
//        explorePanels.add(exploreRecipesButton);
//        explorePanels.add(exploreEventsButton);
//        this.add(explorePanels);

//        JPanel bottomPanel = new JPanel();
//        JButton homeButton = new JButton("Home");
//        JButton settingsButton = new JButton("Settings");
//        JButton profileButton = new JButton("Profile");
//        bottomPanel.add(homeButton);
//        bottomPanel.add(settingsButton);
//        bottomPanel.add(profileButton);
//        this.add(bottomPanel);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        this.add(menuBar, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }

    //test
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new ExploreView(new ViewManagerModel()));
//        frame.pack();
//        frame.setVisible(true);
//    }


}
