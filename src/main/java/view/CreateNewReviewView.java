package view;

import app.Session;
import data_access.FilePostCommentLikesDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Review;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_review_view.CreateReviewController; // optional: if present
import interface_adapter.create_review_view.CreateReviewPresenter; // may or may not exist in your codebase
import interface_adapter.create_review_view.CreateReviewViewModel; // may or may not exist
import use_case.create_review.CreateReviewInputData;
import use_case.create_review.CreateReviewInteractor;
import use_case.create_review.CreateReviewInputBoundary;
import view.ui_components.RoundedButton;

import javax.swing.*;
import java.awt.*;

/**
 * View for creating a new review. Controller may be injected via setCreateReviewController(...).
 * There are two convenience showDialog overloads:
 *  - showDialog(Window owner, ViewManagerModel model) will try to auto-create a controller (best-effort)
 *  - showDialog(Window owner, ViewManagerModel model, CreateReviewController controller) accepts a pre-built controller
 */
public class CreateNewReviewView extends JPanel {

    private final ViewManagerModel viewManagerModel;
    private final JTextField titleField;
    private final JTextArea reviewTextArea;
    private final ReviewStarSystem starSystem;
    private final RoundedButton submitButton;
    private final RoundedButton cancelButton;
    private final String viewName = "create new review";

    // controller (can be injected by caller; if null we try to auto-create in showDialog)
    private CreateReviewController createReviewController;

    public CreateNewReviewView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== Top section =====
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        topPanel.add(titleLabel);
        topPanel.add(titleField);

        // ===== Center section =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel reviewLabel = new JLabel("Your Review:");
        reviewTextArea = new JTextArea(8, 40);
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(reviewTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Inline star rating (we create a temp Review to pass into the star system — it's just for UI)
        JLabel ratingLabel = new JLabel("Your Rating:");
        Review tempReview = new Review(null, 0, "", "");
        starSystem = new ReviewStarSystem(tempReview);

        centerPanel.add(reviewLabel);
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(ratingLabel);
        centerPanel.add(starSystem);

        // ===== Bottom section =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        submitButton = new RoundedButton("Submit Review");
        cancelButton = new RoundedButton("Cancel");

        submitButton.addActionListener(e -> submitReview());
        // Use the named view "explore view" to match your ExploreView.getViewName()
        cancelButton.addActionListener(e -> viewManagerModel.setState("explore view"));

        bottomPanel.add(submitButton);
        bottomPanel.add(cancelButton);

        // Add sections to main panel
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Setter to inject the controller from outside (bootstrapping code should call this if desired).
     */
    public void setCreateReviewController(CreateReviewController controller) {
        this.createReviewController = controller;
    }

    private void submitReview() {
        String title = titleField.getText().trim();
        String text = reviewTextArea.getText().trim();
        double rating = starSystem.getRating();

        if (title.isEmpty() || text.isEmpty() || rating < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields and select a rating.",
                    "Incomplete Review",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // IMPORTANT: ensure a user is logged in
        if (Session.getCurrentAccount() == null) {
            JOptionPane.showMessageDialog(this,
                    "You must be logged in to submit a review.",
                    "Not logged in",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ensure controller exists
        if (createReviewController == null) {
            JOptionPane.showMessageDialog(this,
                    "Unable to submit review right now (no controller available).",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        CreateReviewInputData input = new CreateReviewInputData(
                Session.getCurrentAccount(),
                title,
                text,
                rating,
                new java.util.ArrayList<>()
        );

        try {
            createReviewController.execute(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to submit review: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Review submitted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        try {
            if (viewManagerModel.getHomePageView() != null) {
                viewManagerModel.getHomePageView().updateHomeFeed();
            }
        } catch (Exception ignored) {}

        viewManagerModel.setState("homepage view");
    }


    public static void showDialog(Window owner, ViewManagerModel model) {
        JDialog dlg = new JDialog(owner, "Create Review", Dialog.ModalityType.APPLICATION_MODAL);
        CreateNewReviewView content = new CreateNewReviewView(model);

        // Best-effort: try to auto-create a default controller chain
        boolean controllerSet = false;
        try {
            // Attempt to construct standard objects if present in your codebase.
            PostCommentsLikesDataAccessObject postDAO = FilePostCommentLikesDataAccessObject.getInstance();
            UserDataAccessObject userDAO = FileUserDataAccessObject.getInstance();

            // Create a lightweight presenter/viewmodel if your project defines them.
            // We attempt to reflect-forwards typical class names; if they're missing this will throw.
            try {
                CreateReviewViewModel viewModel = new CreateReviewViewModel();
                CreateReviewPresenter presenter = new CreateReviewPresenter(viewModel);
                CreateReviewInputBoundary interactor = new CreateReviewInteractor(postDAO, userDAO, presenter);
                CreateReviewController controller = new CreateReviewController(interactor);
                content.setCreateReviewController(controller);
                controllerSet = true;
            } catch (Throwable t) {
                // If your CreateReviewPresenter/ViewModel classes aren't present (yet),
                // fall back to creating the interactor with a simple no-op presenter via reflection-free path:
                try {
                    Object presenterFallback = new Object();
                    CreateReviewInputBoundary interactor = new CreateReviewInteractor(postDAO, userDAO, null);
                    CreateReviewController controller = new CreateReviewController(interactor);
                    content.setCreateReviewController(controller);
                    controllerSet = true;
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable t) {
            System.err.println("Auto-create controller failed: " + t.getMessage());
        }

        // If unable to create a controller, disable submit and show helpful tooltip
        if (!controllerSet) {
            content.submitButton.setEnabled(false);
            content.submitButton.setToolTipText("No controller available — review submission disabled. Provide a controller via setCreateReviewController(...)");
            // Show brief message so users (or devs) see what happened
            JOptionPane.showMessageDialog(owner,
                    "Unable to auto-create the CreateReviewController. You can still pass a controller programmatically using content.setCreateReviewController(controller).",
                    "Controller unavailable",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        dlg.setContentPane(content);
        dlg.pack();
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true);
    }

    public String getViewName() {
        return viewName;
    }
}
