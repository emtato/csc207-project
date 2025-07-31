package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.net.URL;

class RoundImagePanel extends JPanel {
    private final Image image;

    public RoundImagePanel(String imagePath) {
        setOpaque(false);
        URL imageUrl = getClass().getClassLoader().getResource(imagePath);
        if (imageUrl == null) {
            throw new IllegalArgumentException("Could not find resource: " + imagePath);
        }
        ImageIcon icon = new ImageIcon(imageUrl);
        this.image = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Float circle = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        g2.setClip(circle);

        g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        g2.dispose();
    }
}
