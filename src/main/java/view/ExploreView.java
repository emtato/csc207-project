package view;

import interface_adapter.ViewManagerModel;

import interface_adapter.explore.ExploreViewModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import entity.Account;
import entity.Event;
import entity.Recipe;
import entity.Restaurant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


import javax.swing.*;

/* Description:
 * explore restaurants, events, and recipes
 */

public class ExploreView extends JPanel {

    private final String viewName = "explore view";
    private final ViewManagerModel viewManagerModel;

    // example posts
    private Account swissCheez = new Account("swissCheez", "cheese");
    private Event cheeseMeetup = new Event(swissCheez, 1234567890, "Cheese Tasting",
        "Visit to local cheesemongers and farmers markets", "St Lawrence Market",
        LocalDate.of(2025, 8, 12), new ArrayList(), new ArrayList<String>());
    private Restaurant cheeseVille = new Restaurant("CheeseVille","14 Cheese Ave", "+1 1234567890",
        "14 Cheese Ave", new ArrayList<String>(Arrays.asList("French", "Italian", "Swiss")));
    private String blueCheeseSteps = "1. Get cheese \n" + "2. Cover it in blue food coloring diluted with milk \n" +
        "3. Make it stinky \n" + "4. Congrats you have blue cheese. Consume it.";
    private Recipe blueCheeseRecipe = new Recipe(swissCheez, 123454321, "DIY Blue Cheese",
        "How to make blue cheese at home in 4 easy steps",
        new ArrayList<String>(Arrays.asList("Cheese", "Blue food coloring", "Milk", "Stink")), blueCheeseSteps,
        new ArrayList<String>(Arrays.asList("French")));

    public ExploreView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        JLabel exploreLabel = new JLabel("Explore");
        topPanel.add(exploreLabel);
        this.add(topPanel, BorderLayout.NORTH);


        JPanel explorePanel = new JPanel();
        explorePanel.setLayout(new GridLayout(1,3));


        // Restaurants
        JLabel exploreRestaurantsLabel = new JLabel("Explore Restaurants");
        JPanel exploreRestaurantsPanel = new JPanel();
        exploreRestaurantsPanel.setBackground(Color.PINK);
        exploreRestaurantsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        JButton exploreRestaurantsButton = new JButton("View");


        // Recipes
        JLabel exploreRecipesLabel = new JLabel("Explore Recipes");
        JPanel exploreRecipesPanel = new JPanel();
        exploreRecipesPanel.setBackground(Color.PINK);
        exploreRecipesPanel.setLayout(new BoxLayout(exploreRecipesPanel, BoxLayout.Y_AXIS));
        exploreRecipesPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));


        for (int i = 0; i < 10; i++) {
            JPanel recipeBox = new JPanel();
            recipeBox.setLayout(new BoxLayout(recipeBox, BoxLayout.Y_AXIS));
            recipeBox.setBackground(Color.WHITE);
            recipeBox.setPreferredSize(new Dimension(400, 150));
            JLabel recipeTitle = new JLabel(blueCheeseRecipe.getTitle());
            recipeTitle.setFont(new Font("Roboto", Font.BOLD, 20));
            JLabel recipeDescription = new JLabel(blueCheeseRecipe.getDescription());
            JLabel recipePoster = new JLabel("Posted by: " + blueCheeseRecipe.getUser().getUsername());
            JButton viewRecipeButton = new JButton("View Recipe");
            recipeBox.add(recipeTitle);
            recipeBox.add(recipeDescription);
            recipeBox.add(recipePoster);
            recipeBox.add(viewRecipeButton);
            exploreRecipesPanel.add(recipeBox);
            exploreRecipesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing
        }

        JScrollPane exploreRecipeScrollPane = new JScrollPane(exploreRecipesPanel);
        exploreRecipeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);




        JButton exploreRecipesButton = new JButton("View");
        exploreRecipesButton.addActionListener(e -> viewManagerModel.setState("recipe view"));


        // Events
        JLabel exploreEventsLabel = new JLabel("Explore Events");
        JPanel exploreEventsPanel = new JPanel();
        exploreEventsPanel.setBackground(Color.PINK);
        exploreEventsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        exploreEventsPanel.setPreferredSize(new Dimension(360, 500));
        JButton exploreEventsButton = new JButton("View");
        exploreEventsButton.addActionListener(e -> viewManagerModel.setState("explore events view"));


//        JLabel restaurantsPlaceholder = new JLabel("restaurants go here");
//        JLabel recipesPlaceholder = new JLabel("recipes go here");
//        JLabel eventsPlaceholder = new JLabel("events go here");

//        exploreRestaurantsPanel.add(restaurantsPlaceholder);
//        exploreRecipesPanel.add(recipesPlaceholder);
//        exploreEventsPanel.add(eventsPlaceholder);



        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(exploreRestaurantsLabel);
        leftPanel.add(exploreRestaurantsPanel);
        leftPanel.add(exploreRestaurantsButton);
        explorePanel.add(leftPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(exploreRecipesLabel);
        centerPanel.add(exploreRecipeScrollPane);
        centerPanel.add(exploreRecipesButton);
        explorePanel.add(centerPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(exploreEventsLabel);
        rightPanel.add(exploreEventsPanel);
        rightPanel.add(exploreEventsButton);
        explorePanel.add(rightPanel);

        add(explorePanel, BorderLayout.CENTER);

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

    //test/////////////////////////////////////////////
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ExploreView(new ViewManagerModel()));
        frame.pack();
        frame.setVisible(true);
    }


}
