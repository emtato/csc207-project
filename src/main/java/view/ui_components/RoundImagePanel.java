package view.ui_components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.image.BufferedImage;

public class RoundImagePanel extends JPanel {
    private static final String DEFAULT_RESOURCE = "images/Homemade-French-Fries_8.jpg";
    private Image image;

    public RoundImagePanel(String pathOrUrl) {
        setOpaque(false);
        loadImage(pathOrUrl);
    }

    private void loadImage(String pathOrUrl) {
        String candidate = (pathOrUrl == null || pathOrUrl.trim().isEmpty()) ? DEFAULT_RESOURCE : pathOrUrl.trim();
        // Try HTTP(S) URL
        if (candidate.startsWith("http://") || candidate.startsWith("https://")) {
            try {
                image = new ImageIcon(new URL(candidate)).getImage();
                if (image != null) return;
            } catch (MalformedURLException ignored) { }
        }
        // Try file system path
        File f = new File(candidate);
        if (image == null && f.exists() && f.isFile()) {
            image = new ImageIcon(candidate).getImage();
            if (image != null) return;
        }
        // Try classpath resource (with and without leading slash)
        if (image == null) {
            URL resource = getClass().getClassLoader().getResource(stripLeadingSlash(candidate));
            if (resource != null) {
                image = new ImageIcon(resource).getImage();
            }
        }
        // Fallback to default
        if (image == null) {
            URL def = getClass().getClassLoader().getResource(stripLeadingSlash(DEFAULT_RESOURCE));
            if (def != null) {
                image = new ImageIcon(def).getImage();
            }
        }
        // As last resort create blank placeholder
        if (image == null) {
            image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = ((BufferedImage) image).createGraphics();
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0,0,50,50);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("?", 22, 28);
            g2.dispose();
        }
    }

    private String stripLeadingSlash(String s) { return s.startsWith("/") ? s.substring(1) : s; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D.Float circle = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        g2.setClip(circle);
        g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        g2.dispose();
    }
}
