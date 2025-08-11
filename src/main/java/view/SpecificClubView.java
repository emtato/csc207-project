package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_post_view.CreatePostController;
import interface_adapter.create_post_view.CreatePostPresenter;
import interface_adapter.create_post_view.CreatePostViewModel;
import interface_adapter.specific_club.SpecificClubController;
import interface_adapter.specific_club.SpecificClubViewModel;
import interface_adapter.like_post.LikePostController;
import entity.Club;
import entity.Post;
import entity.Account;
import view.ui_components.*;
import app.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SpecificClubView extends JPanel implements PropertyChangeListener {
    private final String viewName = "specific club view";
    private final ViewManagerModel viewManagerModel;
    private final JPanel cardPanel;
    private final SpecificClubViewModel specificClubViewModel;
    private SpecificClubController specificClubController;
    private LikePostController likePostController;
    private Club club;

    /**
     * Constructor for the SpecificClubView.
     *
     * @param viewManagerModel The ViewManagerModel that manages the state of the view.
     * @param cardPanel        The JPanel that contains this view.
     * @param club              The Club object containing club information.
     * @param specificClubViewModel The view model for specific club view
     * @param specificClubController The controller for specific club features
     */
    public SpecificClubView(ViewManagerModel viewManagerModel,
                          JPanel cardPanel,
                          Club club,
                          SpecificClubViewModel specificClubViewModel,
                          SpecificClubController specificClubController) {
        this.viewManagerModel = viewManagerModel;
        this.cardPanel = cardPanel;
        this.club = club;
        this.specificClubViewModel = specificClubViewModel;
        this.specificClubController = specificClubController;

        specificClubViewModel.addPropertyChangeListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        setupHeaderPanel(mainPanel);
        setupContentPanels(mainPanel);
        setupMenuBar(mainPanel);

        this.add(mainPanel);
    }

    private void setupHeaderPanel(JPanel mainPanel) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Club image
        RoundImagePanel exploreRoundPanel = new RoundImagePanel("images/Homemade-French-Fries_8.jpg");
        exploreRoundPanel.setPreferredSize(new Dimension(150, 150));
        headerPanel.add(exploreRoundPanel);

        // Club info
        JPanel headerTextPanel = createHeaderTextPanel();
        headerPanel.add(headerTextPanel);

        // Members count
        JPanel membersPanel = createMembersPanel();
        headerPanel.add(membersPanel);

        // Buttons
        JPanel buttonsPanel = createButtonsPanel();
        headerPanel.add(buttonsPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private JPanel createHeaderTextPanel() {
        JPanel headerTextPanel = new JPanel(new BorderLayout(10, 5));

        JLabel titleLabel = new JLabel(club.getName());
        titleLabel.setFont(GUIConstants.FONT_TITLE);
        headerTextPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel descriptionLabel = new JLabel("<html><div style='width: 700px;'>" +
            club.getDescription() + "</div></html>");
        descriptionLabel.setFont(GUIConstants.FONT_TEXT);
        headerTextPanel.add(descriptionLabel, BorderLayout.CENTER);

        return headerTextPanel;
    }

    private JPanel createMembersPanel() {
        JPanel membersPanel = new JPanel(new BorderLayout());

        ImageIcon membersIcon = new ImageIcon(getClass().getResource(
            "/images/toppng.com-white-person-icon-people-white-icon-abstract-backgrounds-436x368.png"));
        Image scaledImage = membersIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        membersPanel.add(iconLabel, BorderLayout.NORTH);

        JLabel membersLabel = new JLabel(String.valueOf(club.getMembers().size()));
        membersLabel.setFont(GUIConstants.FONT_TEXT);
        membersPanel.add(membersLabel, BorderLayout.SOUTH);

        return membersPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Create Post Button
        JButton createPostButton = new JButton("Create Post");
        createPostButton.setFont(GUIConstants.FONT_TEXT);
        createPostButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createPostButton.addActionListener(e -> handleCreatePost());
        buttonsPanel.add(createPostButton);

        // Leave Club Button
        JButton leaveClubButton = new JButton("Leave Club");
        leaveClubButton.setFont(GUIConstants.FONT_TEXT);
        leaveClubButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leaveClubButton.addActionListener(e -> handleLeaveClub());
        buttonsPanel.add(leaveClubButton);

        return buttonsPanel;
    }

    private void handleCreatePost() {
        Account currentUser = Session.getCurrentAccount();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null,
                    "Please log in to create a post",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the existing CreateNewPostView from ViewManagerModel
        CreateNewPostView existingView = viewManagerModel.getCreateNewPostView();
        // Update it with the current club context
        existingView.setClub(club);
        viewManagerModel.setState(existingView.getViewName());
    }

    private void handleLeaveClub() {
        Account currentUser = Session.getCurrentAccount();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null,
                    "Please log in to leave the club",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to leave " + club.getName() + "?",
                "Leave Club",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            specificClubController.leaveClub(currentUser.getUsername(), club.getId());
            viewManagerModel.setState("clubs view");
        }
    }

    private void setupContentPanels(JPanel mainPanel) {
        JPanel leftPanel = new JPanel(new BorderLayout());
        setupAnnouncementsPanel(leftPanel);
        setupEventsPanel(leftPanel);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel feedPanel = new JPanel(new BorderLayout());
        setupFeedPanel(feedPanel);
        mainPanel.add(feedPanel, BorderLayout.EAST);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)), BorderLayout.CENTER);
    }

    private void setupAnnouncementsPanel(JPanel leftPanel) {
        JPanel announcementsPanel = new JPanel(new BorderLayout());

        JLabel announcementsLabel = new JLabel("Announcements");
        announcementsLabel.setFont(GUIConstants.FONT_HEADER);
        announcementsPanel.add(announcementsLabel, BorderLayout.NORTH);

        // Create a panel to hold all posts
        JPanel postsContainer = new JPanel();
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(GUIConstants.WHITE);

        ArrayList<Post> clubPosts = club.getPosts();
        ArrayList<Post> announcements = new ArrayList<>();

        // Filter for announcement posts
        if (clubPosts != null) {
            for (Post post : clubPosts) {
                System.out.println("Post ID: " + post.getID() + ", Type: " + post.getType());
                if (post.getType() != null && post.getType().equals("announcement")) {
                    announcements.add(post);
                    System.out.println("Added to announcements");
                }
            }
        }

        System.out.println("Total announcements found: " + announcements.size());

        if (!announcements.isEmpty()) {
            // Add announcement posts vertically
            for (Post announcement : announcements) {
                PostPanel postPanel = new PostPanel(viewManagerModel, announcement, 500, 400, likePostController);
                postPanel.setMaximumSize(new Dimension(500, 420));
                postPanel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center align the post panel

                postsContainer.add(postPanel);
                postsContainer.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing between posts
            }
        } else {
            JLabel noPostsLabel = new JLabel("No announcements yet!");
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsContainer.add(noPostsLabel);
        }

        // Create scroll pane for announcements posts
        JScrollPane scrollPane = new JScrollPane(postsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        announcementsPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(announcementsPanel, BorderLayout.NORTH);
    }

    private void setupEventsPanel(JPanel leftPanel) {
        // Events section
        JPanel eventsPanel = new JPanel(new BorderLayout());
        JLabel eventsLabel = new JLabel("Events");
        eventsLabel.setFont(GUIConstants.FONT_HEADER);
        eventsPanel.add(eventsLabel, BorderLayout.NORTH);

        // Create a panel to hold all events
        JPanel eventsContainer = new JPanel();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(GUIConstants.WHITE);

        // Add posts vertically in events
        for (int i = 0; i < 5; i++) {
            EventsPanel postPanel = new EventsPanel(viewManagerModel);
            postPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
            postPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            eventsContainer.add(postPanel);
            eventsContainer.add(Box.createVerticalStrut(10));
        }

        // Create scroll pane for events posts
        JScrollPane eventsScrollPane = new JScrollPane(eventsContainer);
        eventsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        eventsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        eventsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        eventsScrollPane.setBorder(null);
        eventsScrollPane.setPreferredSize(new Dimension(500, 300));

        eventsPanel.add(eventsScrollPane, BorderLayout.CENTER);
        leftPanel.add(eventsPanel, BorderLayout.CENTER);
    }

    private void setupFeedPanel(JPanel feedPanel) {
        JLabel feedLabel = new JLabel("Feed");
        feedLabel.setFont(GUIConstants.FONT_HEADER);
        feedPanel.add(feedLabel, BorderLayout.NORTH);

        // Create a panel to hold all feed posts
        JPanel feedContainer = new JPanel();
        feedContainer.setLayout(new BoxLayout(feedContainer, BoxLayout.Y_AXIS));
        feedContainer.setBackground(GUIConstants.WHITE);

        setupPostsPanel(feedContainer);

        // Create scroll pane for posts
        JScrollPane feedScrollPane = new JScrollPane(feedContainer);
        feedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        feedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        feedScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        feedScrollPane.setBorder(null);
        feedScrollPane.setPreferredSize(new Dimension(1000, 800));

        feedPanel.add(feedScrollPane, BorderLayout.CENTER);
    }

    private void setupPostsPanel(JPanel postsPanel) {
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(GUIConstants.WHITE);

        ArrayList<Post> clubPosts = club.getPosts();
        ArrayList<Post> regularPosts = new ArrayList<>();

        // Filter out announcement posts
        if (clubPosts != null) {
            for (Post post : clubPosts) {
                if (post.getType() == null || !post.getType().equals("announcement")) {
                    regularPosts.add(post);
                }
            }
        }

        if (!regularPosts.isEmpty()) {
            for (int i = 0; i < regularPosts.size(); i += 2) {
                JPanel feedRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                feedRow.setBackground(GUIConstants.WHITE);

                // Add first post
                PostPanel postPanel = new PostPanel(viewManagerModel, regularPosts.get(i), 500, 400, likePostController);
                postPanel.setMaximumSize(new Dimension(500, 420));
                feedRow.add(postPanel);

                // Add second post if it exists
                if (i + 1 < regularPosts.size()) {
                    PostPanel postTwo = new PostPanel(viewManagerModel, regularPosts.get(i + 1), 500, 400, likePostController);
                    postTwo.setMaximumSize(new Dimension(500, 420));
                    feedRow.add(postTwo);
                }

                postsPanel.add(feedRow);
                postsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        } else {
            JLabel noPostsLabel = new JLabel("No posts yet!");
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(noPostsLabel);
        }
    }

    private void setupMenuBar(JPanel mainPanel) {
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            // Handle state changes from the view model
            this.revalidate();
            this.repaint();
        }
    }

    public void setSpecificClubController(SpecificClubController controller) {
        this.specificClubController = controller;
    }

    public String getViewName() {
        return viewName;
    }

    public void setLikePostController(LikePostController controller) {
        this.likePostController = controller;
    }

    public void updateClub(Club newClub) {
        this.club = newClub;
        this.removeAll();
        JPanel mainPanel = new JPanel(new BorderLayout());
        setupHeaderPanel(mainPanel);
        setupContentPanels(mainPanel);
        setupMenuBar(mainPanel);
        this.add(mainPanel);
        this.revalidate();
        this.repaint();
    }
}
