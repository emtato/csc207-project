package view;

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

    public AnalyzeRecipesView() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel bottomPanel = new JPanel(new FlowLayout()); //pannel for main ui buttons (persists across views)
        JPanel rightPanel = new JPanel(new BoxLayout(this, BoxLayout.Y_AXIS));
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


    }

    //@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == analyzeButton) {
            System.out.println("analyzeButton");
        }
    }
}
