package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import javax.swing.JLabel;

import javax.swing.text.JTextComponent;
import java.awt.*;

public class CreateClubView extends JPanel {
    private final String viewName = "create club view";
    private final ViewManagerModel viewManagerModel;

    public CreateClubView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Create a Club");
        titleLabel.setFont(GUIConstants.FONT_TITLE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JTextArea titleArea = new JTextArea("Enter club title", 2,20);
        JTextArea bodyArea = new JTextArea("Enter club description", 6, 80);
        bodyArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea imagesArea = new JTextArea("Enter link to club profile image", 3, 80);
        imagesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea tagsArea = new JTextArea("Enter club tags seperated by commas", 1, 80);
        tagsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        textFIeldHints(titleArea, "Enter club title");
        textFIeldHints(bodyArea, "Enter club description");
        textFIeldHints(tagsArea, "Enter link to club profile image");
        textFIeldHints(imagesArea, "Enter club tags seperated by commas");

        contentPanel.add(titleArea);
        contentPanel.add(bodyArea);
        contentPanel.add(imagesArea);
        contentPanel.add(tagsArea);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        this.add(mainPanel);
    }

    /**
     * Function for TextField/TextAreas to display a grey hint message when no text has been entered.
     *
     * @param titleField The text component to add hint to
     * @param hint       the hint message
     */
    private void textFIeldHints(JTextComponent titleField, String hint) {

        titleField.setForeground(Color.GRAY);

        titleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (titleField.getText().equals(hint)) {
                    titleField.setText("");
                    titleField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (titleField.getText().isEmpty()) {
                    titleField.setForeground(Color.GRAY);
                    titleField.setText(hint);
                }
            }
        });
    }

    public String getViewName() {
        return viewName;
    }

}
