package view;

import data_access.DataStorage;
import entity.Account;
import entity.Recipe;
import interface_adapter.ViewManagerModel;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;


public class CreateNewPostView extends JPanel {
    private final JPanel contentPanel;
    private final ViewManagerModel viewManagerModel;
    private final JRadioButton recipes = new JRadioButton("post new recipe :3 ");
    private final JRadioButton option2 = new JRadioButton("Option 2");
    private final JRadioButton option3 = new JRadioButton("Option 3");
    private final String viewName = "create new post";
    public CreateNewPostView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
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

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        menuBar.setPreferredSize(new Dimension(1200, 100));

        contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);
        add(menuBar, BorderLayout.SOUTH);

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
        JTextArea titleArea = new JTextArea("Enter post title", 2,20);
        JTextArea bodyArea = new JTextArea("Enter recipe description", 6, 80);
        bodyArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea imagesArea = new JTextArea("Enter link to images, separated by commas, must end in .jpg, .png, etc", 3, 80);
        imagesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea ingredientsListArea = new JTextArea("Enter list of ingredients separated by commas", 5, 80);
        ingredientsListArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea stepsArea = new JTextArea("Enter steps to make the yum yum", 17, 80);
        stepsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea cuisinesArea = new JTextArea("Enter cuisines and tags separated by commas if u want", 1, 80);
        cuisinesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        textFIeldHints(titleArea, "Enter post title");
        textFIeldHints(bodyArea, "Enter recipe description");
        textFIeldHints(ingredientsListArea, "Enter list of ingredients separated by commas");
        textFIeldHints(stepsArea, "Enter steps to make the yum yum");
        textFIeldHints(cuisinesArea, "Enter cuisines and tags separated by commas if u want");
        textFIeldHints(imagesArea, "Enter link to images, separated by commas, must end in .jpg, .png, etc");

        contentPanel.add(titleArea);
        contentPanel.add(bodyArea);
        contentPanel.add(imagesArea);
        contentPanel.add(ingredientsListArea);
        contentPanel.add(stepsArea);
        contentPanel.add(cuisinesArea);

        JButton okButton = new JButton("send it \uD83E\uDEE3");
        okButton.setFont(new Font("papyrus", Font.BOLD, 20));
        okButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        String title = titleArea.getText();
                        String body = bodyArea.getText();
                        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(ingredientsListArea.getText().split(",")));
                        String steps = stepsArea.getText();
                        ArrayList<String> cuisines = new ArrayList<>(Arrays.asList(cuisinesArea.getText().split(",")));
                        Account user = new Account("r", "y");
                        ArrayList<String> tags = new ArrayList<>(Arrays.asList(cuisinesArea.getText().split(",")));
                        ArrayList<String> imagesList = new ArrayList<>(Arrays.asList(imagesArea.getText().split(",")));
                        Recipe repice = new Recipe(user, 843417361846184L, title, body, ingredients, steps, cuisines);

                        System.out.println("repice obj creted");
                        DataStorage dataStorage = new DataStorage();
                        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                        map.put("ingredients", ingredients);
                        map.put("steps", new ArrayList(Arrays.asList(steps)));
                        map.put("cuisines", cuisines);
                        long postID = (long)(Math.random() * 1_000_000_000_000L);
                        dataStorage.writePost(postID, new Account("a", "b"), title, "recipe", body, map, tags, imagesList);

                        viewManagerModel.setState("homepage view");
                        HomePageView homePageView = new HomePageView(viewManagerModel, new JPanel());
                        homePageView.updateHomeFeed();
                    }

                }
        );
        contentPanel.add(okButton);


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

    public String getViewName(){
        return viewName;
    }

}
