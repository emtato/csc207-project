package view;

import entity.Recipe;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Emilia on 2025-07-27!
 * Description:
 * ^ • ω • ^
 */

// This view is for the user to fill in information for their new post. It could be a recipe, an event, clubs or images
// Emilia- recipes

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;


public class CreateNewPostView extends JFrame {
    private final JPanel contentPanel;
    private final JRadioButton recipes = new JRadioButton("Option 1");
    private final JRadioButton option2 = new JRadioButton("Option 2");
    private final JRadioButton option3 = new JRadioButton("Option 3");

    public CreateNewPostView() {
        setTitle("New Post Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setLayout(new BorderLayout());

        ButtonGroup group = new ButtonGroup();
        group.add(recipes);
        group.add(option2);
        group.add(option3);

        JPanel radioPanel = new JPanel();
        radioPanel.add(recipes);
        radioPanel.add(option2);
        radioPanel.add(option3);
        JLabel label = new JLabel("select what type of post u wanna make", SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(label);
        topPanel.add(radioPanel);
        add(topPanel, BorderLayout.NORTH);

        contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);

        recipes.addActionListener(e -> {
            actionPerformed(e);
        });
        option2.addActionListener(e -> {
            actionPerformed(e);
        });
        option3.addActionListener(e -> {
            actionPerformed(e);
        });

        setVisible(true);
    }

    /**
     * Actionlistener function to delegate selections to functions to load desired views
     *
     * @param e actionevent
     */
    public void actionPerformed(ActionEvent e) {
        contentPanel.removeAll();

        if (e.getSource() == recipes) {
            recipePostView();
        }
        else if (e.getSource() == option2) {
            function2();
        }
        else if (e.getSource() == option3) {
            function3();
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void recipePostView() {
        System.out.println(recipes.isSelected());
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JTextField titleField = new JTextField("Enter post title", 20);
        JTextArea bodyField = new JTextArea("Enter recipe description", 10, 80);
        bodyField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea ingredientsList = new JTextArea("Enter list of ingredients separated by commas", 20, 80);
        ingredientsList.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea steps = new JTextArea("Enter steps to make the yum yum", 20, 80);
        steps.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea cuisines = new JTextArea("Enter cuisine separated by commas if u want", 1, 80);
        cuisines.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        textFIeldHints(titleField, "Enter post title");
        textFIeldHints(bodyField, "Enter recipe description");
        textFIeldHints(ingredientsList, "Enter list of ingredients separated by commas");
        textFIeldHints(steps, "Enter steps to make the yum yum");
        textFIeldHints(cuisines, "Enter cuisine separated by commas if u want");

        contentPanel.add(titleField);
        contentPanel.add(bodyField);
        contentPanel.add(ingredientsList);
        contentPanel.add(steps);
        contentPanel.add(cuisines);

        JButton okButton = new JButton("send it \uD83E\uDEE3");
        okButton.setFont(new Font("papyrus", Font.BOLD, 20));

        contentPanel.add(okButton);
        // Recipe rep = new Recipe();

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

//end of recipe new post view
//---------------------------------------------------------------------------------------------------------------------

    private void function2() {
        JLabel larbel = new JLabel("meow");
        contentPanel.add(larbel);

    }

    private void function3() {
        JLabel larbel = new JLabel("mo");
        contentPanel.add(larbel);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateNewPostView::new);
    }
}
