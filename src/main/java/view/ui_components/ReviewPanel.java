package view.ui_components;

import data_access.FilePostCommentLikesDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Review;
import interface_adapter.ViewManagerModel;
import interface_adapter.get_comments.GetCommentsViewModel;
import interface_adapter.like_post.LikePostController;
import use_case.like_post.LikePostInteractor;
import use_case.like_post.LikePostInputBoundary;
import view.PostView;
import view.ReviewStarSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ReviewPanel extends JPanel {

    private final ViewManagerModel viewManagerModel;
    private final Review review;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    // Fonts
    private final Font fontTitle = new Font("Roboto", Font.BOLD, 16);
    private final Font subTitle = new Font("Roboto", Font.PLAIN, 15);
    private final Font text = new Font("Roboto", Font.PLAIN, 13);

    // Middle content
    private final JTextPane reviewText = new JTextPane();

    // Buttons
    private final RoundedButton likeButton = new RoundedButton("Like");
    private final RoundedButton saveButton = new RoundedButton("Add to list");
    private final RoundedButton shareButton = new RoundedButton("Share");
    private final RoundedButton viewFullPost = new RoundedButton("view full post");

    private final JLabel title;
    private final JLabel subtitle;

    private boolean liked;
    private final JPanel centerPanel;
    private final JPanel bottomPanel;
    private final LikePostController likePostController;
    private GetCommentsViewModel getCommentsViewModel;

    // Default constructor with small parameter list
    public ReviewPanel(ViewManagerModel viewManagerModel, Review review) {
        this(viewManagerModel, review, 800, 300, createDefaultLikeController());
    }

    // Full constructor (kept for flexibility)
    public ReviewPanel(ViewManagerModel viewManagerModel, Review review, int postWidth, int postHeight,
                       LikePostController likePostController) {
        if (review == null) throw new IllegalArgumentException("Review cannot be null");

        this.viewManagerModel = viewManagerModel;
        this.review = review;
        this.likePostController = likePostController;

        setLayout(new BorderLayout());

        // ===== TOP =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        title = new JLabel(review.getTitle());
        title.setFont(fontTitle);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);

        subtitle = new JLabel(review.getUser().getUsername() + " | " +
                review.getDateTime().format(formatter) + " | " +
                review.getLikes() + " likes");
        subtitle.setFont(subTitle);
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(subtitle);

        JLabel tags = new JLabel("tags: " + review.getTags());
        tags.setFont(text);
        tags.setForeground(Color.LIGHT_GRAY);
        tags.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(tags);

        // ===== CENTER =====
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        reviewText.setEditable(false);
        reviewText.setPreferredSize(new Dimension(postWidth, postHeight));
        reviewText.setMaximumSize(new Dimension(postWidth, Integer.MAX_VALUE));
        reviewText.setText(review.getDescription());
        centerPanel.add(reviewText);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Inline star rating (updates review.rating)
        ReviewStarSystem starSystem = new ReviewStarSystem(review);
        centerPanel.add(starSystem);

        // ===== BOTTOM =====
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ArrayList<JButton> bottomButtons = new ArrayList<>();
        if (liked) likeButton.setText("Unlike");

        bottomButtons.add(likeButton);
        bottomButtons.add(saveButton);
        bottomButtons.add(shareButton);
        bottomButtons.add(viewFullPost);

        for (JButton button : bottomButtons) {
            button.setFont(text);
            button.addActionListener(e -> {
                try {
                    actionPerformed(e);
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            bottomPanel.add(button);
        }

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private static LikePostController createDefaultLikeController() {
        PostCommentsLikesDataAccessObject dao = FilePostCommentLikesDataAccessObject.getInstance();
        LikePostInputBoundary interactor = new LikePostInteractor(dao);
        return new LikePostController(interactor);
    }

    public void actionPerformed(ActionEvent e) throws IOException, InterruptedException {
        if (e.getSource() == likeButton) {
            boolean isLiking = !liked;
            likePostController.likePost(review.getID(), isLiking);

            if (isLiking) {
                likeButton.setText("Unlike");
                review.setLikes(review.getLikes() + 1);
            } else {
                likeButton.setText("Like");
                review.setLikes(review.getLikes() - 1);
            }
            liked = isLiking;

            subtitle.setText(review.getUser().getUsername() + " | " +
                    review.getDateTime().format(formatter) + " | " +
                    review.getLikes() + " likes");
        }
        if (e.getSource() == saveButton) {
            JOptionPane.showMessageDialog(null, "Save not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == shareButton) {
            JOptionPane.showMessageDialog(null, "Share this ID: " + review.getID(), "Share", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == viewFullPost) {
            viewManagerModel.setState("review view");
            PostView currentPostView = viewManagerModel.getPostView();
            currentPostView.displayPost(this.review);
        }
    }

    public void setGetCommentsViewModel(GetCommentsViewModel getCommentsViewModel) {
        this.getCommentsViewModel = getCommentsViewModel;
    }
}
