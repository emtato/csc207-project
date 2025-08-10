package view;

import app.AppProperties;
import app.Session;
import data_access.places.GooglePlacesAPI;
import entity.Account;
import entity.Event;
import entity.Recipe;
import entity.Restaurant;
import interface_adapter.ViewManagerModel;
import view.map.MapView;
import view.map.RestaurantSearch;
import view.ui_components.MenuBarPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* Description:
 * Explore restaurants, events, and recipes tailored for the current logged-in user.
 */
public class ExploreView extends JPanel {

    private final String viewName = "explore view";
    private final ViewManagerModel viewManagerModel;
    private static JPanel cardPanel;
    private Account currentUser;

    public ExploreView(ViewManagerModel viewManagerModel, JPanel cardPanel) {
        this.viewManagerModel = viewManagerModel;
        this.cardPanel = cardPanel;
        this.setLayout(new BorderLayout(10, 10));

        Session.setCurrentUsername("example_user"); // or real logged-in username
        Session.setCurrentAccount();
        this.currentUser = Session.getCurrentAccount();

        if (currentUser == null) {
            System.err.println("Warning: current user not set in Session");
        }

        // Example Event & Recipe using currentUser
        Event exampleEvent = new Event(currentUser, 1234567890, "Local Food Festival",
                "A gathering to celebrate local cuisines.", "Downtown Park",
                LocalDate.of(2025, 8, 20), new ArrayList<>(), new ArrayList<>());

        String recipeSteps = "1. Prepare ingredients\n2. Cook with love\n3. Serve and enjoy.";
        Recipe exampleRecipe = new Recipe(currentUser, 987654321, "Grandma's Secret Pie",
                "A delicious homemade pie recipe.",
                new ArrayList<>(Arrays.asList("Flour", "Sugar", "Apples")), recipeSteps,
                new ArrayList<>(Arrays.asList("Dessert")));

        // Top label
        JPanel topPanel = new JPanel();
        JLabel exploreLabel = new JLabel("Explore");
        exploreLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        topPanel.add(exploreLabel);
        this.add(topPanel, BorderLayout.NORTH);

        // Main panel with 3 columns: restaurants, recipes, events
        JPanel explorePanel = new JPanel(new GridLayout(1, 3));

        // --- Restaurants ---
        JLabel restaurantsLabel = new JLabel("Explore Restaurants");
        restaurantsLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel restaurantsPanel = new JPanel();
        restaurantsPanel.setBackground(Color.PINK);
        restaurantsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        restaurantsPanel.setLayout(new BoxLayout(restaurantsPanel, BoxLayout.Y_AXIS));

        String location = currentUser.getLocation();
        if (location == null || location.isBlank()) {
            location = "";
        }
        var cuisines = currentUser.getFoodPreferences();
        if (cuisines == null || cuisines.isEmpty()) {
            cuisines = new ArrayList<>(Arrays.asList("International"));
        }

        try {
            RestaurantSearch searcher = new RestaurantSearch();
            final Restaurant seed = new Restaurant(new ArrayList<>(cuisines), location);

            new SwingWorker<List<Restaurant>, Void>() {
                @Override
                protected List<Restaurant> doInBackground() throws Exception {
                    return searcher.search(seed);
                }

                @Override
                protected void done() {
                    try {
                        List<Restaurant> liveRestaurants = get();
                        if (liveRestaurants == null || liveRestaurants.isEmpty()) {
                            restaurantsPanel.add(new JLabel("No restaurants found for your preferences."));
                        } else {
                            for (Restaurant rest : liveRestaurants) {
                                JPanel restBox = new JPanel();
                                restBox.setLayout(new BoxLayout(restBox, BoxLayout.Y_AXIS));
                                restBox.setBackground(Color.WHITE);
                                restBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

                                JLabel name = new JLabel(rest.getName());
                                name.setFont(new Font("Roboto", Font.BOLD, 15));
                                JLabel address = new JLabel(rest.getAddress() == null ? "Address unknown" : rest.getAddress());
                                JLabel priceRange = new JLabel("Price range: " + (rest.getPriceRange() == null ? "N/A" : rest.getPriceRange()));

                                JButton viewButton = new JButton("View Restaurant");
                                // TODO: add listener to open restaurant details

                                restBox.add(name);
                                restBox.add(address);
                                restBox.add(priceRange);
                                restBox.add(viewButton);

                                restaurantsPanel.add(restBox);
                                restaurantsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            }
                        }
                        restaurantsPanel.revalidate();
                        restaurantsPanel.repaint();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();

        } catch (IOException e) {
            e.printStackTrace();
            restaurantsPanel.add(new JLabel("Restaurant search not available."));
        }

        JScrollPane restaurantsScroll = new JScrollPane(restaurantsPanel);
        restaurantsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JButton restaurantsViewBtn = new JButton("View");
        // Add listeners if needed

        // --- Recipes ---
        JLabel recipesLabel = new JLabel("Explore Recipes");
        recipesLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel recipesPanel = new JPanel();
        recipesPanel.setBackground(Color.PINK);
        recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));
        recipesPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));

        for (int i = 0; i < 10; i++) {
            JPanel recipeBox = new JPanel();
            recipeBox.setLayout(new BoxLayout(recipeBox, BoxLayout.Y_AXIS));
            recipeBox.setBackground(Color.WHITE);
            recipeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            JLabel recipeTitle = new JLabel(exampleRecipe.getTitle());
            recipeTitle.setFont(new Font("Roboto", Font.BOLD, 15));
            JLabel recipeDesc = new JLabel(exampleRecipe.getDescription());
            JLabel recipeUser = new JLabel("Posted by: " + exampleRecipe.getUser().getUsername());
            JButton viewRecipeBtn = new JButton("View Recipe");

            viewRecipeBtn.addActionListener(e -> {
                viewManagerModel.setState("post view");
                for (Component c : cardPanel.getComponents()) {
                    if (c instanceof PostView) {
                        ((PostView) c).displayPost(exampleRecipe);
                        break;
                    }
                }
            });

            recipeBox.add(recipeTitle);
            recipeBox.add(recipeDesc);
            recipeBox.add(recipeUser);
            recipeBox.add(viewRecipeBtn);

            recipesPanel.add(recipeBox);
            recipesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane recipesScroll = new JScrollPane(recipesPanel);
        recipesScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JButton recipesViewBtn = new JButton("View");
        // Add listeners if needed

        // --- Events ---
        JLabel eventsLabel = new JLabel("Explore Events");
        eventsLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel eventsPanel = new JPanel();
        eventsPanel.setBackground(Color.PINK);
        eventsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < 10; i++) {
            JPanel eventBox = new JPanel();
            eventBox.setLayout(new BoxLayout(eventBox, BoxLayout.Y_AXIS));
            eventBox.setBackground(Color.WHITE);
            eventBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            JLabel eventTitle = new JLabel(exampleEvent.getTitle());
            eventTitle.setFont(new Font("Roboto", Font.BOLD, 15));
            JLabel eventDesc = new JLabel(exampleEvent.getDescription());
            JLabel eventUser = new JLabel("Posted by: " + exampleEvent.getUser().getUsername());
            JButton viewEventBtn = new JButton("View Event");

            eventBox.add(eventTitle);
            eventBox.add(eventDesc);
            eventBox.add(eventUser);
            eventBox.add(viewEventBtn);

            eventsPanel.add(eventBox);
            eventsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane eventsScroll = new JScrollPane(eventsPanel);
        eventsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JButton eventsViewBtn = new JButton("View");
        eventsViewBtn.addActionListener(e -> viewManagerModel.setState("explore events view"));

        // Add to main panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(restaurantsLabel);
        leftPanel.add(restaurantsScroll);
        leftPanel.add(restaurantsViewBtn);
        explorePanel.add(leftPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(recipesLabel);
        centerPanel.add(recipesScroll);
        centerPanel.add(recipesViewBtn);
        explorePanel.add(centerPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(eventsLabel);
        rightPanel.add(eventsScroll);
        rightPanel.add(eventsViewBtn);
        explorePanel.add(rightPanel);

        add(explorePanel, BorderLayout.CENTER);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        this.add(menuBar, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }
}

