package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import view.GUIConstants;

// TODO: implement Review into this

public class ReviewStarSystem extends JFrame {

    private final JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JLabel[] starLabels = new JLabel[5];
    private double rating = 0.0;

    private final ImageIcon emptyStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/empty_star.png")));
    private final ImageIcon halfStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/half_star.png")));
    private final ImageIcon fullStarIcon = new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/images/filled_star.png")));

    public ReviewStarSystem() {
        setTitle("Star Rating System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        setSize(GUIConstants.STANDARD_PANEL_WIDTH, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateRatingFromMouse(MouseEvent e) {
        int x = Math.max(0, Math.min(e.getX(), starPanel.getWidth())); // Clamp within bounds
        double rawRating = (5.0 * x) / starPanel.getWidth();           // Map to 0.0–5.0
        rating = Math.round(rawRating * 2) / 2.0;                      // Round to nearest 0.5
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

    public double getRating() {
        return rating;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReviewStarSystem::new);
    }
}
