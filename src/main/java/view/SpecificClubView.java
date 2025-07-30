package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import javax.swing.JLabel;

public class SpecificClubView extends JPanel {
    private final String viewName = "specific club view";
    private final ViewManagerModel viewManagerModel;
    private final JPanel cardPanel;

    public SpecificClubView(ViewManagerModel viewManagerModel, JPanel cardPanel) {
        this.viewManagerModel = viewManagerModel;
        this.cardPanel = cardPanel;

        // Initialize the specific club view components here
        // For example, you can add labels, buttons, and other UI elements
        JLabel clubLabel = new JLabel("Welcome to the Specific Club!");
        clubLabel.setFont(GUIConstants.FONT_TITLE);
        add(clubLabel);
    }

    public String getViewName() {
        return viewName;
    }
}
