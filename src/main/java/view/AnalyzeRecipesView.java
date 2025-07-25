package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.analyze_recipes.AnalyzeRecipesViewModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/* Description:
 * ^ • ω • ^
 */

public class AnalyzeRecipesView extends JPanel {

    private final String viewName = "recipe view";
    private final AnalyzeRecipesViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    //middle
    JTextArea recipeText = new JTextArea();
    //bottom
    JButton backButton = new JButton("Back");
    JButton mapsButton = new JButton("Maps");
    JButton slopButton = new JButton("Feed");
    JButton settingsButton = new JButton("Settings");
    JButton profileButton = new JButton("Profile");
    //right
    JButton likeButton = new JButton("Like");
    JButton analyzeButton = new JButton("Analyze");
    JButton saveButton = new JButton("Add to list");
    JButton shareButton = new JButton("Share");

    public AnalyzeRecipesView(AnalyzeRecipesViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        //this.viewModel.addPropertyChangeListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel bottomPanel = new JPanel(new FlowLayout()); //pannel for main ui buttons (persists across views)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JPanel centerPanel = new JPanel();

        //top
        JLabel title = new JLabel("HELLLOOOO aaiaiaiee"); //recipe/post title
        topPanel.add(title);
        JLabel subtitle = new JLabel("meowers"); // post author and date
        JLabel tags = new JLabel("tags");
        topPanel.add(subtitle);
        topPanel.add(tags);

        //middle
        recipeText.setEditable(false);

        //bottom
        bottomPanel.add(backButton);
        bottomPanel.add(mapsButton);
        bottomPanel.add(slopButton);
        bottomPanel.add(settingsButton);
        bottomPanel.add(profileButton);

        //right
        rightPanel.add(likeButton);
        rightPanel.add(analyzeButton);
        rightPanel.add(saveButton);
        rightPanel.add(shareButton);


        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        add(menuBar, BorderLayout.NORTH);

    }

    //@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == analyzeButton) {
            System.out.println("analyzeButton");
        }
    }

    public String getViewName() {
        return viewName;
    }
}
