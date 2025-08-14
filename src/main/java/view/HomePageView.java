package view;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import entity.Post;
import entity.Review;
import interface_adapter.ViewManagerModel;
import interface_adapter.fetch_post.FetchPostController;
import interface_adapter.fetch_review.FetchReviewController;
import interface_adapter.get_comments.GetCommentsController;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.like_post.LikePostController;
import view.ui_components.MenuBarPanel;
import view.ui_components.PostPanel;
import view.ui_components.ReviewPanel;

/**
 * The View for the Home Page.
 */
public class HomePageView extends JPanel {

    private final String viewName = "homepage view";
    private final ViewManagerModel viewManagerModel;
    private final HomePageViewModel homePageViewModel;
    private final JPanel feedPanel;

    private FetchPostController fetchPostController;
    private FetchReviewController fetchReviewController;
    private LikePostController likePostController;
    private GetCommentsController getCommentsController;

    public HomePageView(ViewManagerModel viewManagerModel, HomePageViewModel homePageViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.homePageViewModel = homePageViewModel;

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top tabs / buttons
        Dimension buttonSize = new Dimension(340, 30);
        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JButton forYouButton = new JButton("For You");
        forYouButton.setPreferredSize(buttonSize);
        forYouButton.setBorder(BorderFactory.createEmptyBorder());

        JButton followingButton = new JButton("Following");
        followingButton.setPreferredSize(buttonSize);
        followingButton.setBorder(BorderFactory.createEmptyBorder());

        JButton tagsButton = new JButton("Tags");
        tagsButton.setPreferredSize(buttonSize);
        tagsButton.setBorder(BorderFactory.createEmptyBorder());

        JButton refreshButton = new JButton("Refresh Homescreen");
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setBorder(BorderFactory.createEmptyBorder());
        refreshButton.addActionListener(e -> updateHomeFeed());

        JButton createButton = new JButton("NEW POST?");
        createButton.setPreferredSize(new Dimension(80, 30));
        createButton.setBorder(BorderFactory.createEmptyBorder());
        createButton.addActionListener(e -> {
            CreateNewPostView createNewPostView = viewManagerModel.getCreateNewPostView();
            createNewPostView.recipePostView();
            viewManagerModel.setState("create new post");
        });

        tabsPanel.add(forYouButton);
        tabsPanel.add(followingButton);
        tabsPanel.add(tagsButton);
        tabsPanel.add(refreshButton);
        tabsPanel.add(createButton);

        mainPanel.add(tabsPanel, BorderLayout.NORTH);

        // Scrollable feed panel
        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        JScrollPane mainScrollPane = new JScrollPane(feedPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setPreferredSize(new Dimension(1000, 760));
        mainPanel.add(mainScrollPane, BorderLayout.CENTER);

        // Bottom menu bar
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    public void updateHomeFeed() {
        SwingUtilities.invokeLater(() -> {
            feedPanel.removeAll();

            int maxNumberOfDisplayingPosts = 10;

            // Fetch posts safely
            ArrayList<Post> result = new ArrayList<>();
            if (fetchPostController != null) {
                fetchPostController.getRandomFeedPosts(maxNumberOfDisplayingPosts * 2);
                if (homePageViewModel.getState().getRandomPosts() != null) {
                    result.addAll(homePageViewModel.getState().getRandomPosts());
                }
            }

            // Fetch reviews safely
            ArrayList<Review> reviews = new ArrayList<>();
            if (fetchReviewController != null) {
                fetchReviewController.getRandomFeedReviews(maxNumberOfDisplayingPosts);
                if (homePageViewModel.getState().getRandomReviews() != null) {
                    reviews.addAll(homePageViewModel.getState().getRandomReviews());
                }
            }

            // Split posts into two halves for display
            int actualLength = result.size();
            ArrayList<Post> randomFeedPosts = new ArrayList<>();
            ArrayList<Post> randomFeedPosts2 = new ArrayList<>();
            if (actualLength > 0) {
                randomFeedPosts.addAll(result.subList(0, actualLength / 2));
                randomFeedPosts2.addAll(result.subList(actualLength / 2, actualLength));
            }

            // Add posts to feed panel
            for (int i = 0; i < randomFeedPosts.size(); i++) {
                JPanel feedRow = new JPanel();
                feedRow.setLayout(new BoxLayout(feedRow, BoxLayout.X_AXIS));
                feedRow.add(Box.createRigidArea(new Dimension(40, 0)));

                Post post1 = randomFeedPosts.get(i);
                PostPanel postPanel1 = new PostPanel(viewManagerModel, post1, 400, 400, likePostController);
                postPanel1.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
                feedRow.add(postPanel1);

                if (i < randomFeedPosts2.size()) {
                    Post post2 = randomFeedPosts2.get(i);
                    PostPanel postPanel2 = new PostPanel(viewManagerModel, post2, 600, 400, likePostController);
                    postPanel2.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
                    feedRow.add(postPanel2);
                }

                feedRow.add(Box.createHorizontalGlue());
                feedPanel.add(feedRow);
            }

            // Add reviews to feed panel
            for (Review review : reviews) {
                ReviewPanel reviewPanel = new ReviewPanel(viewManagerModel, review);
                reviewPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

                JPanel reviewRow = new JPanel();
                reviewRow.setLayout(new BoxLayout(reviewRow, BoxLayout.X_AXIS));
                reviewRow.add(Box.createRigidArea(new Dimension(40, 0)));
                reviewRow.add(reviewPanel);
                reviewRow.add(Box.createHorizontalGlue());

                feedPanel.add(reviewRow);
            }

            feedPanel.revalidate();
            feedPanel.repaint();
            revalidate();
            repaint();
        });
    }

    public String getViewName() {
        return viewName;
    }

    public void setLikePostController(LikePostController controller) {
        this.likePostController = controller;
    }

    public void setFetchPostController(FetchPostController controller) {
        this.fetchPostController = controller;
    }

    public void setFetchReviewController(FetchReviewController controller) {
        this.fetchReviewController = controller;
    }

    public void setGetCommentsController(GetCommentsController controller) {
        this.getCommentsController = controller;
    }
}
