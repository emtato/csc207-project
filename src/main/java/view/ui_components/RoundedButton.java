package view.ui_components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Emilia on 2025-07-27!
 * Description: Custom rounded cornered button for pretty
 * ^ • ω • ^
 */


public class RoundedButton extends JButton {
    public RoundedButton(String label) {
        super(label);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // back
        g2.setColor(new Color(255, 182, 193)); //rose
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);


        super.paintComponent(g2);
    }
}
