package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.JLabel;


import entity.Account;
import entity.Post;
import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;

/**
 * The View for the Home Page.
 */
public class HomePageView extends JPanel {

    private final String viewName = "homepage view";
    private final ViewManagerModel viewManagerModel;

    private String steps = "1. smash 4 glorbles of bean paste into a sock, microwave till it sings\n" + "2.sprinkle in 2 blinks of mystery flakes, scream gently\n" + "3.serve upside-down on a warm tile \n \n \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n hi\nhih\nhi\njo";
    private Recipe trialpost = new Recipe(new Account("meow", "woof"), 483958292, "repice for glunking", "i made it for the tiger but the bird keeps taking it", new ArrayList<>(Arrays.asList("glorbles", "beans", "tile", "dandelion")), steps, new ArrayList<>(Arrays.asList("yeah")));


    public HomePageView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;

        JPanel mainPanel = new JPanel(new BorderLayout());

        // top tabs to switch between feeds
        Dimension buttonSize = new Dimension(450, GUIConstants.MAIN_BUTTON1_HEIGHT);

        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton forYouButton = new JButton("For You");
        forYouButton.setPreferredSize(buttonSize);
        JButton followingButton = new JButton("Following");
        followingButton.setPreferredSize(buttonSize);
        JButton tagsButton = new JButton("Tags");
        tagsButton.setPreferredSize(buttonSize);

        tabsPanel.add(forYouButton);
        tabsPanel.add(followingButton);
        tabsPanel.add(tagsButton);

        mainPanel.add(tabsPanel, BorderLayout.NORTH);

        // scrollable feed panel
        JPanel feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(feedPanel, BorderLayout.CENTER);

        for (int i = 0; i < 5; i++) {
            PostPanel postPanel = new PostPanel(viewManagerModel, trialpost, 1200, 600);
            feedPanel.add(postPanel);
            feedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane mainScrollPane = new JScrollPane(wrapperPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainScrollPane.setPreferredSize(new Dimension(1200, 800));
        mainPanel.add(mainScrollPane, BorderLayout.CENTER);

        // bottom menu bar
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    public String getViewName() {
        return viewName;
    }

}
