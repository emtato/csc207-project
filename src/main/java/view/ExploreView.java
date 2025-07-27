package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;


import javax.swing.*;

/* Description:
 * explore restaurants, events, and recipes
 */

public class ExploreView extends JPanel {

    public ExploreView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        JLabel exploreLabel = new JLabel("Explore");
        topPanel.add(exploreLabel);
        this.add(topPanel);


        JPanel explorePanels = new JPanel();
        explorePanels.setLayout(new GridLayout(2, 3));

        JLabel exploreRestaurantsLabel = new JLabel("Explore Restaurants");
        JLabel exploreRecipesLabel = new JLabel("Explore Recipes");
        JLabel exploreEventsLabel = new JLabel("Explore Events");

        JButton exploreRestaurantsButton = new JButton("View");
        JButton exploreRecipesButton = new JButton("View");
        JButton exploreEventsButton = new JButton("View");

//        exploreEventsButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                ExploreEventsView.main(null);
//            }
//        });

        explorePanels.add(exploreRestaurantsLabel);
        explorePanels.add(exploreRecipesLabel);
        explorePanels.add(exploreEventsLabel);
        explorePanels.add(exploreRestaurantsButton);
        explorePanels.add(exploreRecipesButton);
        explorePanels.add(exploreEventsButton);
        this.add(explorePanels);

        JPanel bottomPanel = new JPanel();
        JButton homeButton = new JButton("Home");
        JButton settingsButton = new JButton("Settings");
        JButton profileButton = new JButton("Profile");
        bottomPanel.add(homeButton);
        bottomPanel.add(settingsButton);
        bottomPanel.add(profileButton);
        this.add(bottomPanel);
    }




    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ExploreView());
        frame.pack();
        frame.setVisible(true);
    }


}
