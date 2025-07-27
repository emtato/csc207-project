package view;

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


public class CreateNewPostView extends JFrame {
    private final JPanel contentPanel;
    private final JRadioButton recipes = new JRadioButton("Option 1", true);
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
        add(radioPanel, BorderLayout.NORTH);

        contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);
        recipePostView();

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
        JTextPane textPane = new JTextPane();
        contentPanel.add(textPane);
        JLabel larbel = new JLabel("hi");
        contentPanel.add(larbel);
    }

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
