package view.ui_components;

import view.GUIConstants;

import java.awt.Color;
import java.awt.Font;

public class GeneralJLabel extends javax.swing.JLabel {

    public GeneralJLabel(String text, int textSize, Color color, int style) {
        setFont(new Font(GUIConstants.FONT, style, textSize));
        setText(text);
        setForeground(color);
    }
    public GeneralJLabel(String text, int textSize, Color color) {
        setFont(new Font(GUIConstants.FONT, Font.PLAIN, textSize));
        setText(text);
        setForeground(color);
    }
}
