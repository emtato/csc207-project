package view;

import entity.Review;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class ReviewStarSystem extends JPanel {

    private final JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JLabel[] starLabels = new JLabel[5];
    private double rating = 0;

    private final ImageIcon emptyStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/empty_star.png")));
    private final ImageIcon halfStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/half_star.png")));
    private final ImageIcon fullStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/filled_star.png")));

    private final Review review;
    private final JLabel ratingLabel = new JLabel("Rating: N/A");

    public ReviewStarSystem(Review review) {
        this.review = review;
        setLayout(new FlowLayout());

        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel(emptyStarIcon);
            starLabels[i] = star;
            starPanel.add(star);
        }

        starPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateRatingFromMouse(e);
            }
        });

        starPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateRatingFromMouse(e);
            }
        });

        add(starPanel);
        add(ratingLabel);

        setSize(550, 175);
        setVisible(true);
    }

    private void updateRatingFromMouse(MouseEvent e) {
        int x = Math.max(0, Math.min(e.getX(), starPanel.getWidth())); // Clamp within bounds
        double rawRating = (5.0 * x) / starPanel.getWidth();           // Map to 0.0–5.0
        rating = Math.round(rawRating * 2) / 2.0;                      // Round to nearest 0.5

        review.setRating(rating);

        updateStarIcons();
        ratingLabel.setText("Rating: " + rating);
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

    public double getRating() {
        return rating;
    }

    // TESTING
    public static void main(String[] args) {
        // Example usage:
        entity.Account acc = new entity.Account("testUser", "testPassword"); // Assuming Account exists
        Review review = new Review(acc, 1, "Great Place!", "Had a wonderful time.");
        SwingUtilities.invokeLater(() -> new ReviewStarSystem(review));
    }
}
