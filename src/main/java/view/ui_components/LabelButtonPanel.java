package view.ui_components;

import view.GUIConstants;

import javax.swing.*;

/**
 * A panel containing a label and a button.
 */
public class LabelButtonPanel extends JPanel {
    public LabelButtonPanel(JLabel label, JButton button) {
        this.setBackground(GUIConstants.WHITE);
        label.setForeground(GUIConstants.RED);
        label.setFont(GUIConstants.SMALL_FONT_TEXT);
        button.setForeground(GUIConstants.BLACK);
        button.setFont(GUIConstants.SMALL_FONT_TEXT);

        this.add(label);
        this.add(button);
    }
}
