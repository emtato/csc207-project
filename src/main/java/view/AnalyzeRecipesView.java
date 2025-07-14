package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/* Description:
 * ^ • ω • ^
 */

public class AnalyzeRecipesView extends JPanel {

    private final String viewName = "recipe view";


    public AnalyzeRecipesView() {
        JLabel title = new JLabel(""); //get recipe/post title
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new FlowLayout()); //pannel for main ui buttons (persists across views)
        JPanel rightPanel = new JPanel(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel centerPanel = new JPanel();

        //top
        //middle
        JTextArea recipeText = new JTextArea();

    }
}
