package view;

import data_access.FilePostCommentLikesDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;
import interface_adapter.ViewManagerModel;
import interface_adapter.analyze_recipe.AnalyzeRecipeViewModel;
import interface_adapter.create_post_view.CreatePostController;
import interface_adapter.create_post_view.CreatePostPresenter;
import interface_adapter.get_comments.GetCommentsViewModel;
import interface_adapter.like_post.LikePostController;
import interface_adapter.create_post_view.CreatePostViewModel;
import use_case.create_post.CreatePostInteractor;
import view.ui_components.EventsPanel;
import view.ui_components.MenuBarPanel;
import view.ui_components.PostPanel;
import view.ui_components.RoundImagePanel;

import javax.swing.*;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SpecificClubView extends JPanel {
    private final String viewName = "specific club view";
    private final ViewManagerModel viewManagerModel;
    private final JPanel cardPanel;
    private final GetCommentsViewModel getCommentsViewModel;
    private final AnalyzeRecipeViewModel analyzeRecipeViewModel;
    private PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject = FilePostCommentLikesDataAccessObject.getInstance();
    private LikePostController likePostController;
    private final Club club;

    /**
     * Constructor for the SpecificClubView.
     *
     * @param viewManagerModel The ViewManagerModel that manages the state of the view.
     * @param cardPanel        The JPanel that contains this view.
     * @param club              The Club object containing club information.
     */
    public SpecificClubView(ViewManagerModel viewManagerModel, JPanel cardPanel, Club club) {
        this.viewManagerModel = viewManagerModel;
        this.cardPanel = cardPanel;
        this.getCommentsViewModel = new GetCommentsViewModel();
        this.analyzeRecipeViewModel = new AnalyzeRecipeViewModel();
        this.club = club;

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        RoundImagePanel exploreRoundPanel = new RoundImagePanel("images/Homemade-French-Fries_8.jpg");
        exploreRoundPanel.setPreferredSize(new Dimension(150, 150));
        headerPanel.add(exploreRoundPanel);

        JPanel headerTextPanel = new JPanel(new BorderLayout(10, 5));

        JLabel titleLabel = new JLabel(club.getName());
        titleLabel.setFont(GUIConstants.FONT_TITLE);
        headerTextPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel descriptionLabel = new JLabel("<html><div style='width: 700px;'>" + club.getDescription() + "</div></html>");
        descriptionLabel.setFont(GUIConstants.FONT_TEXT);
        headerTextPanel.add(descriptionLabel, BorderLayout.CENTER);

        headerPanel.add(headerTextPanel);

        JPanel membersPanel = new JPanel(new BorderLayout());

        ImageIcon membersIcon = new ImageIcon(getClass().getResource("/images/toppng.com-white-person-icon-people-white-icon-abstract-backgrounds-436x368.png"));
        Image scaledImage = membersIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        membersPanel.add(iconLabel, BorderLayout.NORTH);

        JLabel membersLabel = new JLabel(String.valueOf(club.getMembers().size()));
        membersLabel.setFont(GUIConstants.FONT_TEXT);
        membersPanel.add(membersLabel, BorderLayout.SOUTH);

        headerPanel.add(membersPanel);

        // Create Post Button
        JPanel createPostPanel = new JPanel(new BorderLayout());
        JButton createPostButton = new JButton("Create Post");
        createPostButton.setFont(GUIConstants.FONT_TEXT);
        createPostButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createPostButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Account currentUser = app.Session.getCurrentAccount();
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(null,
                        "Please log in to create a post",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                PostCommentsLikesDataAccessObject postDAO = FilePostCommentLikesDataAccessObject.getInstance();
                UserDataAccessObject userDAO = FileUserDataAccessObject.getInstance();
                CreatePostViewModel createPostViewModel = new CreatePostViewModel();

                // Create the required objects for post creation
                CreatePostPresenter createPostPresenter = new CreatePostPresenter(createPostViewModel);
                CreatePostInteractor createPostInteractor = new CreatePostInteractor(postDAO, userDAO, createPostPresenter);
                CreatePostController createPostController = new CreatePostController(createPostInteractor);

                // Create the view and set its controller
                CreateNewPostView createNewPostView = new CreateNewPostView(viewManagerModel, createPostViewModel, club);
                createNewPostView.setCreatePostController(createPostController);

                cardPanel.add(createNewPostView, createNewPostView.getViewName());
                viewManagerModel.setState(createNewPostView.getViewName());
            }
        });
        createPostPanel.add(createPostButton, BorderLayout.CENTER);
        headerPanel.add(createPostPanel);

        JPanel leftPanel = new JPanel(new BorderLayout());

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
            // Add announcement posts in rows of two
            for (int i = 0; i < announcements.size(); i += 2) {
                JPanel feedRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                feedRow.setBackground(GUIConstants.WHITE);

                // Add first post
                PostPanel postPanel = new PostPanel(viewManagerModel, announcements.get(i), 500, 400, likePostController);
                postPanel.setMaximumSize(new Dimension(500, 420));
                feedRow.add(postPanel);

                // Add second post if it exists
                if (i + 1 < announcements.size()) {
                    PostPanel postTwo = new PostPanel(viewManagerModel, announcements.get(i + 1), 500, 400, likePostController);
                    postTwo.setMaximumSize(new Dimension(500, 420));
                    feedRow.add(postTwo);
                }

                postsContainer.add(feedRow);
                postsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
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

        JPanel feedPanel = new JPanel(new BorderLayout());
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

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(feedPanel, BorderLayout.EAST);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)), BorderLayout.CENTER);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);

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

    public String getViewName() {
        return viewName;
    }
    public void setLikePostController(LikePostController controller) {
        this.likePostController = controller;
    }
}
