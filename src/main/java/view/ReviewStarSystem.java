package view;

import entity.Review;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class ReviewStarSystem extends JFrame {

    private final JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JLabel[] starLabels = new JLabel[5];
    private double rating = 0.0;
    private final Review review;

    private final ImageIcon emptyStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/empty_star.png")));
    private final ImageIcon halfStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/half_star.png")));
    private final ImageIcon fullStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/filled_star.png")));

    public ReviewStarSystem(Review review) {
        this.review = review;

        setTitle("Star Rating System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Use existing rating if set
        if (review.getRating() >= 0 && review.getRating() <= 5) {
            rating = review.getRating();
        }

        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel(emptyStarIcon);
            starLabels[i] = star;
            starPanel.add(star);
        }

        updateStarIcons(); // Set initial display

        starPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateRatingFromMouse(e);
                saveRatingToReview();
            }
        });

        starPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateRatingFromMouse(e);
                saveRatingToReview();
            }
        });

        add(starPanel);
        setSize(550, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateRatingFromMouse(MouseEvent e) {
        int x = Math.max(0, Math.min(e.getX(), starPanel.getWidth())); // Clamp to panel bounds
        double rawRating = (5.0 * x) / starPanel.getWidth();
        rating = Math.round(rawRating * 2) / 2.0;
        updateStarIcons();
    }

    private void updateStarIcons() {
        for (int i = 0; i < 5; i++) {
            if (i + 1 <= rating) {
                starLabels[i].setIcon(fullStarIcon);
            } else if (i + 0.5 == rating) {
                starLabels[i].setIcon(halfStarIcon);
            } else {
                starLabels[i].setIcon(emptyStarIcon);
            }
        }
    }

    private void saveRatingToReview() {
        int rounded = (int) Math.round(rating);
        review.setRating(rounded);
    }

    public static void main(String[] args) {
        // Test setup
        entity.Account user = new entity.Account("testUser", "pass");
        Review r = new Review(user, 123, "Good food", "Delicious meal.");
        SwingUtilities.invokeLater(() -> new ReviewStarSystem(r));
    }
}
