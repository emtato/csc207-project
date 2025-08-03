package view.ui_components;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import view.GUIConstants;

/**
 * A panel containing a label and a text field or text area.
 */
public class LabelTextPanel extends JPanel {
    public LabelTextPanel(JLabel label, JTextField textField) {
        this.setBackground(GUIConstants.WHITE);
        label.setForeground(GUIConstants.RED);
        label.setFont(GUIConstants.SMALL_FONT_TEXT);
        textField.setForeground(GUIConstants.BLACK);
        textField.setBackground(GUIConstants.PINK);
        textField.setFont(GUIConstants.SMALL_FONT_TEXT);

        this.add(label);
        this.add(textField);
    }
    public LabelTextPanel(JLabel label, JTextArea textField) {
        this.setBackground(GUIConstants.WHITE);
        label.setForeground(GUIConstants.RED);
        label.setFont(GUIConstants.SMALL_FONT_TEXT);
        textField.setForeground(GUIConstants.BLACK);
        textField.setBackground(GUIConstants.PINK);
        textField.setFont(GUIConstants.SMALL_FONT_TEXT);

        this.add(label);
        this.add(textField);
    }
}
