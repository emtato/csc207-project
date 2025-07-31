package view;

import data_access.DataStorage;
import entity.Account;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;


public class CreateNewPostView extends JFrame {
    private final JPanel contentPanel;
    private final JRadioButton recipes = new JRadioButton("post new recipe :3 ");
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
        JTextArea stepsField = new JTextArea("Enter steps to make the yum yum", 20, 80);
        stepsField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea cuisinesField = new JTextArea("Enter cuisines and tags separated by commas if u want", 1, 80);
        cuisinesField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        textFIeldHints(titleField, "Enter post title");
        textFIeldHints(bodyField, "Enter recipe description");
        textFIeldHints(ingredientsList, "Enter list of ingredients separated by commas");
        textFIeldHints(stepsField, "Enter steps to make the yum yum");
        textFIeldHints(cuisinesField, "Enter cuisines and tags separated by commas if u want");

        contentPanel.add(titleField);
        contentPanel.add(bodyField);
        contentPanel.add(ingredientsList);
        contentPanel.add(stepsField);
        contentPanel.add(cuisinesField);

        JButton okButton = new JButton("send it \uD83E\uDEE3");
        okButton.setFont(new Font("papyrus", Font.BOLD, 20));
        okButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        String title = titleField.getText();
                        String body = bodyField.getText();
                        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(ingredientsList.getText().split(",")));
                        String steps = stepsField.getText();
                        ArrayList<String> cuisines = new ArrayList<>(Arrays.asList(cuisinesField.getText().split(",")));
                        Account user = new Account("r", "y");
                        ArrayList<String> tags = new ArrayList<>(Arrays.asList(cuisinesField.getText().split(",")));
                        Recipe repice = new Recipe(user, 843417361846184L, title, body, ingredients, steps, cuisines);

                        System.out.println("repice obj creted");
                        DataStorage dataStorage = new DataStorage();
                        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                        map.put("ingredients", ingredients);
                        map.put("steps", new ArrayList(Arrays.asList(steps)));
                        map.put("cuisines", cuisines);
                        long postID = (long)(Math.random() * 1_000_000_000_000L);
                        dataStorage.writePost(postID, new Account("a", "b"), title, "recipe", body, map, tags);
                        //TODO: send this somewher to db or sum????? idk help!! also associate this with actual account user nd stuff
                    }

                }
        );
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
