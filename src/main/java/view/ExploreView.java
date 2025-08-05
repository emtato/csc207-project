package view;

import data_access.PostCommentsLikesDataAccessObject;
import interface_adapter.ViewManagerModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.*;

import entity.Account;
import entity.Event;
import entity.Post;
import entity.Recipe;
import entity.Restaurant;

import interface_adapter.fetch_post.FetchPostController;

import java.time.LocalDate;
import java.util.*;

import java.util.List;
import use_case.fetch_post.FetchPostInteractor;
import view.ui_components.JFrame;
import view.ui_components.MenuBarPanel;

/* Description:
 * explore restaurants, events, and recipes
 */

public class ExploreView extends JPanel {

    private final String viewName = "explore view";
    private final ViewManagerModel viewManagerModel;
    private static JPanel cardPanel;
    private PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject;
    private FetchPostController fetchPostController;

    // example posts
    private Account swissCheez = new Account("swissCheez", "cheese");
    private Event cheeseMeetup = new Event(swissCheez, 1234567890, "Cheese Tasting",
            "Visit to local cheesemongers and farmers markets", "St Lawrence Market",
            LocalDate.of(2025, 8, 12), new ArrayList(), new ArrayList<String>());
//    private Restaurant cheeseVille = new Restaurant("CheeseVille", "14 Cheese Ave", "+1 1234567890",
//            "14 Cheese Ave", new ArrayList<String>(Arrays.asList("French", "Italian", "Swiss")));
    private Restaurant cheeseVille = new Restaurant(new ArrayList<String>(Arrays.asList("French", "Italian", "Swiss")), "Toronto");
//    private String blueCheeseSteps = "1. Get cheese \n" + "2. Cover it in blue food coloring diluted with milk \n" +
//            "3. Make it stinky \n" + "4. Congrats you have blue cheese. Consume it.";
//    private Recipe blueCheeseRecipe = new Recipe(swissCheez, 123454321, "DIY Blue Cheese",
//            "How to make blue cheese at home in 4 easy steps",
//            new ArrayList<String>(Arrays.asList("Cheese", "Blue food coloring", "Milk", "Stink")), blueCheeseSteps,
//            new ArrayList<String>(Arrays.asList("French")));



    public ExploreView(ViewManagerModel viewManagerModel, PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject) {
        this.viewManagerModel = viewManagerModel;
        this.setLayout(new BorderLayout(10, 10));
        this.postCommentsLikesDataAccessObject = postCommentsLikesDataAccessObject;
        this.fetchPostController = new FetchPostController(new FetchPostInteractor(postCommentsLikesDataAccessObject));

        JPanel topPanel = new JPanel();
        JLabel exploreLabel = new JLabel("Explore");
        exploreLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        topPanel.add(exploreLabel);
        this.add(topPanel, BorderLayout.NORTH);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 3));

        // Write cheeseMeetup
        HashMap<String, ArrayList<String>> cheeseMeetupContents = new HashMap<>();
        cheeseMeetupContents.put("location", new ArrayList<>(Arrays.asList(cheeseMeetup.getLocation())));
        cheeseMeetupContents.put("date", new ArrayList<>(Arrays.asList(cheeseMeetup.getDate().toString())));
        cheeseMeetupContents.put("participants", cheeseMeetup.getParticipants());
        cheeseMeetupContents.put("foodPreferences", cheeseMeetup.getFoodPreferences());
        postCommentsLikesDataAccessObject.writePost(cheeseMeetup.getID(), cheeseMeetup.getUser(), cheeseMeetup.getTitle(),
            "event", cheeseMeetup.getDescription(), cheeseMeetupContents, new ArrayList<>(), new ArrayList<>(), "12:00 AM");



        // Get data
        int numPosts = fetchPostController.getAvailablePostIDs().size();
        List<Post> allPosts = fetchPostController.getRandomFeedPosts(numPosts);
        ArrayList<Restaurant> allRestaurants = new ArrayList<>();
        ArrayList<Event> allEvents = new ArrayList<>();
        ArrayList<Recipe> allRecipes = new ArrayList<>();

        for (Post post : allPosts) {
            if (post instanceof Recipe){allRecipes.add((Recipe) post);}
            else if (post instanceof Event){allEvents.add((Event) post);}
            // else if (post instanceof Restaurant){allRestaurants.add((Restaurant) post);}
            // TODO: Ask Spence about making Restaurant instanceof Post
        }

        // Restaurants
        JLabel exploreRestaurantsLabel = new JLabel("Explore Restaurants");
        exploreRestaurantsLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel exploreRestaurantsPanel = new JPanel();
        exploreRestaurantsPanel.setBackground(Color.PINK);
        exploreRestaurantsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        exploreRestaurantsPanel.setLayout(new BoxLayout(exploreRestaurantsPanel, BoxLayout.Y_AXIS));


        for (int i = 0; i < 10; i++) {
            JPanel RestaurantBox = new JPanel();
            RestaurantBox.setLayout(new BoxLayout(RestaurantBox, BoxLayout.Y_AXIS));
            RestaurantBox.setBackground(Color.WHITE);
            RestaurantBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            JLabel name = new JLabel("CheeseVille");
            name.setFont(new Font("Roboto", Font.BOLD, 15));
            JLabel address = new JLabel("14 Fromage Ave");
            JLabel priceRange = new JLabel("Price range: $$");
            //JButton viewRestaurantButton = new JButton("View Restaurant");
            RestaurantBox.add(name);
            RestaurantBox.add(address);
            RestaurantBox.add(priceRange);
            //RestaurantBox.add(viewRestaurantButton);
            exploreRestaurantsPanel.add(RestaurantBox);
            exploreRestaurantsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing
        }

        JScrollPane exploreRestaurantsScrollPanel = new JScrollPane(exploreRestaurantsPanel);
        exploreRestaurantsScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        // Recipes
        JLabel exploreRecipesLabel = new JLabel("Explore Recipes");
        exploreRecipesLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel exploreRecipesPanel = new JPanel();
        exploreRecipesPanel.setBackground(Color.PINK);
        exploreRecipesPanel.setLayout(new BoxLayout(exploreRecipesPanel, BoxLayout.Y_AXIS));
        exploreRecipesPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));


        for (Recipe recipe : allRecipes) {
            JPanel recipeBox = new JPanel();
            recipeBox.setLayout(new BoxLayout(recipeBox, BoxLayout.Y_AXIS));
            recipeBox.setBackground(Color.WHITE);
            recipeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            JLabel recipeTitle = new JLabel(recipe.getTitle());
            recipeTitle.setFont(new Font("Roboto", Font.BOLD, 15));
            JLabel recipeDescription = new JLabel(recipe.getDescription());
            JLabel recipePoster = new JLabel("Posted by: " + recipe.getUser().getUsername());
            JButton viewRecipeButton = new JButton("View Recipe");
            viewRecipeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewManagerModel.setState("post view");
                    for (Component c : cardPanel.getComponents()) {
                        if (c instanceof PostView) {
                            ((PostView) c).displayPost(recipe);
                            break;
                        }
                    }
                }
            });


            recipeBox.add(recipeTitle);
            recipeBox.add(recipeDescription);
            recipeBox.add(recipePoster);
            recipeBox.add(viewRecipeButton);
            exploreRecipesPanel.add(recipeBox);
            exploreRecipesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing
        }

        JScrollPane exploreRecipeScrollPanel = new JScrollPane(exploreRecipesPanel);
        exploreRecipeScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Events
        JLabel exploreEventsLabel = new JLabel("Explore Events");
        exploreEventsLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel exploreEventsPanel = new JPanel();
        exploreEventsPanel.setBackground(Color.PINK);
        exploreEventsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        exploreEventsPanel.setLayout(new BoxLayout(exploreEventsPanel, BoxLayout.Y_AXIS));

        for (Event event : allEvents) {
            JPanel EventBox = new JPanel();
            EventBox.setLayout(new BoxLayout(EventBox, BoxLayout.Y_AXIS));
            EventBox.setBackground(Color.WHITE);
            EventBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            JLabel eventTitle = new JLabel(event.getTitle());
            eventTitle.setFont(new Font("Roboto", Font.BOLD, 15));
            JLabel eventDescription = new JLabel(event.getDescription());
            JLabel eventPoster = new JLabel("Posted by: " + event.getUser().getUsername());
            JButton viewEventButton = new JButton("View Event");
            viewEventButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewManagerModel.setState("post view");
                    for (Component c : cardPanel.getComponents()) {
                        if (c instanceof PostView) {
                            ((PostView) c).displayPost(event);
                            break;
                        }
                    }
                }
            });

            EventBox.add(eventTitle);
            EventBox.add(eventDescription);
            EventBox.add(eventPoster);
            EventBox.add(viewEventButton);
            exploreEventsPanel.add(EventBox);
            exploreEventsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing
        }

        JScrollPane exploreEventScrollPanel = new JScrollPane(exploreEventsPanel);
        exploreEventScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(exploreRestaurantsLabel);
        leftPanel.add(exploreRestaurantsScrollPanel);
        mainPanel.add(leftPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(exploreRecipesLabel);
        centerPanel.add(exploreRecipeScrollPanel);
        mainPanel.add(centerPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(exploreEventsLabel);
        rightPanel.add(exploreEventScrollPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        this.add(menuBar, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }

    //test/////////////////////////////////////////////
    public static void main(String[] args) {
        view.ui_components.JFrame frame = new view.ui_components.JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ExploreView(new ViewManagerModel(), cardPanel));
        frame.pack();
        frame.setVisible(true);
    }


}
