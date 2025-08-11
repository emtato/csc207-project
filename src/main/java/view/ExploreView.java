package view;

import app.Session;
import entity.Account;
import entity.Event;
import entity.Recipe;
import entity.Restaurant;
import interface_adapter.ViewManagerModel;
import interface_adapter.map.MapState;
import interface_adapter.map.MapViewModel;
import view.map.MapView;
import view.map.RestaurantSearch;
import view.ui_components.MapPanel;
import view.ui_components.MenuBarPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
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

        // Session init (example). In production you would not set a username here.
        Session.setCurrentUsername("example_user");
        Session.setCurrentAccount();
        this.currentUser = Session.getCurrentAccount();

        if (currentUser == null) {
            System.err.println("Warning: current user not set in Session");
            this.add(new JLabel("No logged-in user."), BorderLayout.CENTER);
            return;
        }

        // Example Event & Recipe (social posts)
        Event exampleEvent = new Event(currentUser, 1234567890, "Local Food Festival",
                "A gathering to celebrate local cuisines.", "Downtown Park",
                LocalDate.of(2025, 8, 20), new ArrayList<>(), new ArrayList<>());

        String recipeSteps = "1. Prepare ingredients\n2. Cook with love\n3. Serve and enjoy.";
        Recipe exampleRecipe = new Recipe(currentUser, 987654321, "Grandma's Secret Pie",
                "A delicious homemade pie recipe.",
                new ArrayList<>(Arrays.asList("Flour", "Sugar", "Apples")), recipeSteps,
                new ArrayList<>(Arrays.asList("Dessert")));

        // Top title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel exploreLabel = new JLabel("Explore");
        exploreLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        topPanel.add(exploreLabel);
        this.add(topPanel, BorderLayout.NORTH);

        // Main panel with 3 columns
        JPanel explorePanel = new JPanel(new GridLayout(1, 3));

        // === Restaurants column ===
        JLabel restaurantsLabel = new JLabel("Explore Restaurants");
        restaurantsLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel restaurantsPanel = new JPanel();
        restaurantsPanel.setBackground(Color.PINK);
        restaurantsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        restaurantsPanel.setLayout(new BoxLayout(restaurantsPanel, BoxLayout.Y_AXIS));
        restaurantsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane restaurantsScroll = new JScrollPane(restaurantsPanel);
        restaurantsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // user prefs
        String location = currentUser.getLocation();
        if (location == null || location.isBlank()) location = "";
        var cuisines = currentUser.getFoodPreferences();
        if (cuisines == null || cuisines.isEmpty()) cuisines = new ArrayList<>(Arrays.asList(""));

        // loading indicator
        JLabel loadingLabel = new JLabel("Searching for restaurants...");
        loadingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        restaurantsPanel.add(loadingLabel);
        restaurantsPanel.revalidate();

        // Run RestaurantSearch off the EDT
        try {
            RestaurantSearch searcher = new RestaurantSearch();
            final Restaurant seed = new Restaurant(new ArrayList<>(cuisines), location);

            new SwingWorker<List<Restaurant>, Void>() {
                @Override
                protected List<Restaurant> doInBackground() {
                    return searcher.search(seed);
                }

                @Override
                protected void done() {
                    restaurantsPanel.removeAll();
                    try {
                        List<Restaurant> liveRestaurants = get(); // runs on EDT inside done()
                        if (liveRestaurants == null || liveRestaurants.isEmpty()) {
                            JLabel none = new JLabel("No restaurants found for your preferences.");
                            none.setAlignmentX(Component.LEFT_ALIGNMENT);
                            restaurantsPanel.add(none);
                        } else {
                            for (Restaurant restaurant : liveRestaurants) {
                                final Restaurant rFinal = restaurant;

                                // rest box
                                JPanel restBox = new JPanel();
                                restBox.setLayout(new BoxLayout(restBox, BoxLayout.Y_AXIS));
                                restBox.setBackground(Color.WHITE);
                                restBox.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                                restBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
                                restBox.setAlignmentX(Component.LEFT_ALIGNMENT);

                                JLabel name = new JLabel(rFinal.getName() == null ? "Unknown" : rFinal.getName());
                                name.setFont(new Font("Roboto", Font.BOLD, 15));
                                name.setAlignmentX(Component.LEFT_ALIGNMENT);

                                JLabel addressLabel = new JLabel(rFinal.getAddress() == null ? "Address unknown" : rFinal.getAddress());
                                addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                                // show "N/A" for missing/unknown price levels (more user-friendly than "?")
                                String rawPrice = rFinal.getPriceRange();
                                String priceText = (rawPrice == null || rawPrice.isBlank() || rawPrice.equals("?")) ? "N/A" : rawPrice;
                                JLabel priceRange = new JLabel("Price range: " + priceText);
                                priceRange.setAlignmentX(Component.LEFT_ALIGNMENT);

                                // Buttons row (left-aligned)
                                JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
                                buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
                                JButton viewMapBtn = new JButton("View Map");
                                final JButton websiteBtn = new JButton("Website");

                                // Website enabled only if restaurant has URI
                                websiteBtn.setEnabled(rFinal.getURI() != null);

                                // View Map — open dialog and populate MapPanel
                                viewMapBtn.addActionListener(ev -> showMapDialog(rFinal));

                                // Website — open URI if present
                                websiteBtn.addActionListener(ev -> {
                                    URI uri = rFinal.getURI();
                                    if (uri != null) openWebpage(uri);
                                    else JOptionPane.showMessageDialog(ExploreView.this,
                                            "No website available for this restaurant.",
                                            "No website", JOptionPane.INFORMATION_MESSAGE);
                                });

                                buttons.add(viewMapBtn);
                                buttons.add(websiteBtn);

                                // Add components
                                restBox.add(name);
                                restBox.add(addressLabel);
                                restBox.add(priceRange);
                                restBox.add(Box.createRigidArea(new Dimension(0, 6)));
                                restBox.add(buttons);

                                restaurantsPanel.add(restBox);
                                restaurantsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JLabel err = new JLabel("Error loading restaurants.");
                        err.setAlignmentX(Component.LEFT_ALIGNMENT);
                        restaurantsPanel.add(err);
                    } finally {
                        restaurantsPanel.revalidate();
                        restaurantsPanel.repaint();
                    }
                }
            }.execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            restaurantsPanel.removeAll();
            JLabel err = new JLabel("Restaurant search initialization failed.");
            err.setAlignmentX(Component.LEFT_ALIGNMENT);
            restaurantsPanel.add(err);
            restaurantsPanel.revalidate();
        }

        // === Recipes column (social posts) ===
        JLabel recipesLabel = new JLabel("Explore Recipes");
        recipesLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel recipesPanel = new JPanel();
        recipesPanel.setBackground(Color.PINK);
        recipesPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < 6; i++) {
            JPanel recipeBox = new JPanel();
            recipeBox.setLayout(new BoxLayout(recipeBox, BoxLayout.Y_AXIS));
            recipeBox.setBackground(Color.WHITE);
            recipeBox.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            recipeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

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
            recipeBox.add(Box.createRigidArea(new Dimension(0, 6)));
            recipeBox.add(viewRecipeBtn);

            recipesPanel.add(recipeBox);
            recipesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        JScrollPane recipesScroll = new JScrollPane(recipesPanel);
        recipesScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // === Events column (social posts) ===
        JLabel eventsLabel = new JLabel("Explore Events");
        eventsLabel.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel eventsPanel = new JPanel();
        eventsPanel.setBackground(Color.PINK);
        eventsPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < 6; i++) {
            JPanel eventBox = new JPanel();
            eventBox.setLayout(new BoxLayout(eventBox, BoxLayout.Y_AXIS));
            eventBox.setBackground(Color.WHITE);
            eventBox.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            eventBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

            JLabel eventTitle = new JLabel(exampleEvent.getTitle());
            eventTitle.setFont(new Font("Roboto", Font.BOLD, 15));
            JLabel eventDesc = new JLabel(exampleEvent.getDescription());
            JLabel eventUser = new JLabel("Posted by: " + exampleEvent.getUser().getUsername());
            JButton viewEventBtn = new JButton("View Event");

            viewEventBtn.addActionListener(e -> viewManagerModel.setState("explore events view"));

            eventBox.add(eventTitle);
            eventBox.add(eventDesc);
            eventBox.add(eventUser);
            eventBox.add(Box.createRigidArea(new Dimension(0, 6)));
            eventBox.add(viewEventBtn);

            eventsPanel.add(eventBox);
            eventsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        JScrollPane eventsScroll = new JScrollPane(eventsPanel);
        eventsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Assemble columns
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(restaurantsLabel);
        leftPanel.add(restaurantsScroll);
        explorePanel.add(leftPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(recipesLabel);
        centerPanel.add(recipesScroll);
        explorePanel.add(centerPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(eventsLabel);
        rightPanel.add(eventsScroll);
        explorePanel.add(rightPanel);

        add(explorePanel, BorderLayout.CENTER);

        // Bottom menu
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        this.add(menuBar, BorderLayout.SOUTH);
    }

    /**
     * Show dialog with a MapPanel and buttons. MapView populates a MapViewModel in background,
     * and update of the MapPanel happens in SwingWorker.done() (on EDT).
     */
    private void showMapDialog(Restaurant restaurant) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Map / Details", true);
        dlg.setLayout(new BorderLayout(8, 8));

        MapPanel mapPanel = new MapPanel();
        // Give the MapPanel a sensible preferred size so dialog reserves space for it immediately.
        mapPanel.setPreferredSize(new Dimension(520, 320));
        // show a quick loading label inside the MapPanel (MapPanel.update() should overwrite it)
        mapPanel.update("Loading...", "", "", "", "");
        dlg.add(mapPanel, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton openMaps = new JButton("Open in Google Maps");
        final JButton openWebsite = new JButton("Open Website");
        JButton closeBtn = new JButton("Close");

        // Initially enable website button only if restaurant already has a URI
        openWebsite.setEnabled(restaurant.getURI() != null);

        openMaps.addActionListener(e -> {
            try {
                String q;
                String loc = restaurant.getLocation();
                if (loc != null && loc.contains(",")) {
                    q = URLEncoder.encode(loc, StandardCharsets.UTF_8.toString());
                } else {
                    q = URLEncoder.encode(restaurant.getAddress() == null ? restaurant.getName() : restaurant.getAddress(),
                            StandardCharsets.UTF_8.toString());
                }
                URI mapsUri = new URI("https://www.google.com/maps/search/?api=1&query=" + q);
                openWebpage(mapsUri);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unable to open maps link.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        openWebsite.addActionListener(e -> {
            URI uri = restaurant.getURI();
            if (uri != null) openWebpage(uri);
            else JOptionPane.showMessageDialog(this, "No website available for this restaurant.", "No website", JOptionPane.INFORMATION_MESSAGE);
        });

        closeBtn.addActionListener(e -> dlg.dispose());

        actions.add(openMaps);
        actions.add(openWebsite);
        actions.add(closeBtn);
        dlg.add(actions, BorderLayout.SOUTH);

        // Background: populate MapViewModel using your MapView; then update MapPanel in done()
        new SwingWorker<MapState, Void>() {
            @Override
            protected MapState doInBackground() {
                MapViewModel mapViewModel = new MapViewModel();
                // MapView will populate the viewModel (may perform network IO internally)
                new MapView(mapViewModel, restaurant);
                return mapViewModel.getState();
            }

            @Override
            protected void done() {
                try {
                    MapState state = get(); // runs on EDT
                    String name = state.getName();
                    String address = state.getAddress();
                    String phone = state.getPhone();
                    List<String> cuisinesList = state.getCuisines() == null ? new ArrayList<>() : state.getCuisines();
                    String cuisines = String.join(", ", cuisinesList);
                    String priceRaw = state.getPriceRange();
                    String price = (priceRaw == null || priceRaw.isBlank() || priceRaw.equals("?")) ? "N/A" : priceRaw;

                    mapPanel.update(name, address, phone, cuisines, price);

                    // enable website if restaurant got one (usually set during search)
                    openWebsite.setEnabled(restaurant.getURI() != null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    // fallback to Restaurant's own fields
                    mapPanel.update(
                            restaurant.getName(),
                            restaurant.getAddress(),
                            restaurant.getPhone(),
                            restaurant.getCuisines() == null ? "" : String.join(", ", restaurant.getCuisines()),
                            restaurant.getPriceRange() == null || restaurant.getPriceRange().equals("?") ? "N/A" : restaurant.getPriceRange()
                    );
                    openWebsite.setEnabled(restaurant.getURI() != null);
                }
            }
        }.execute();

        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    /** helper: open browser for a URI */
    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getViewName() {
        return viewName;
    }
}
