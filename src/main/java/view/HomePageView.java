package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;


import javax.swing.*;


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
    private final JPanel cardPanel;
    private String steps = "1. smash 4 glorbles of bean paste into a sock, microwave till it sings\n" + "2.sprinkle in 2 blinks of mystery flakes, scream gently\n" + "3.serve upside-down on a warm tile \n \n \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n hi\nhih\nhi\njo";
    private Recipe trialpost = new Recipe(new Account("meow", "woof"), 483958292, "repice for glunking", "i made it for the tiger but the bird keeps taking it", new ArrayList<>(Arrays.asList("glorbles", "beans", "tile", "dandelion")), steps, new ArrayList<>(Arrays.asList("yeah")));
    private Post postex2 = new Post(new Account("jinufan333", "WOOF ARF BARK BARK"), 2384723473L, "titler?", "IS THAT MY HANDSOME, ELEGANT, INTELLIGENT, CHARMING, KIND, THOUGHTFUL, STRONG, COURAGEOUS, CREATIVE, BRILLIANT, GENTLE, HUMBLE, GENEROUS, PASSIONATE, WISE, FUNNY, LOYAL, DEPENDABLE, GRACEFUL, RADIANT, CALM, CONFIDENT, WARM, COMPASSIONATE, WITTY, ADVENTUROUS, RESPECTFUL, SINCERE, MAGNETIC, BOLD, ARTICULATE, EMPATHETIC, INSPIRING, HONEST, PATIENT, POWERFUL, ATTENTIVE, UPLIFTING, CLASSY, FRIENDLY, RELIABLE, AMBITIOUS, INTUITIVE, TALENTED, SUPPORTIVE, GROUNDED, DETERMINED, CHARISMATIC, EXTRAORDINARY, TRUSTWORTHY, NOBLE, DIGNIFIED, PERCEPTIVE, INNOVATIVE, REFINED, CONSIDERATE, BALANCED, OPEN-MINDED, COMPOSED, IMAGINATIVE, MINDFUL, OPTIMISTIC, VIRTUOUS, NOBLE-HEARTED, WELL-SPOKEN, QUICK-WITTED, DEEP, PHILOSOPHICAL, FEARLESS, AFFECTIONATE, EXPRESSIVE, EMOTIONALLY INTELLIGENT, RESOURCEFUL, DELIGHTFUL, FASCINATING, SHARP, SELFLESS, DRIVEN, ASSERTIVE, AUTHENTIC, VIBRANT, PLAYFUL, OBSERVANT, SKILLFUL, GENEROUS-SPIRITED, PRACTICAL, COMFORTING, BRAVE, WISE-HEARTED, ENTHUSIASTIC, DEPENDABLE, TACTFUL, ENDURING, DISCREET, WELL-MANNERED, COMPOSED, MATURE, TASTEFUL, JOYFUL, UNDERSTANDING, GENUINE, BRILLIANT-MINDED, ENCOURAGING, WELL-ROUNDED, MAGNETIC, DYNAMIC, RADIANT, RADIANT-SPIRITED, SOULFUL, RADIANT-HEARTED, INSIGHTFUL, CREATIVE-SOULED, JUSTICE-MINDED, RELIABLE-HEARTED, TENDER, UPLIFTING-MINDED, PERSEVERING, DEVOTED, ANGELIC, DOWN-TO-EARTH, GOLDEN-HEARTED, GENTLE-SPIRITED, CLEVER, COURAGEOUS-HEARTED, COURTEOUS, HARMONIOUS, LOYAL-MINDED, BEAUTIFUL-SOULED, EASYGOING, SINCERE-HEARTED, RESPECTFUL-MINDED, COMFORTING-VOICED, CONFIDENT-MINDED, EMOTIONALLY STRONG, RESPECTFUL-SOULED, IMAGINATIVE-HEARTED, PROTECTIVE, NOBLE-MINDED, CONFIDENT-SOULED, WISE-EYED, LOVING, SERENE, MAGNETIC-SOULED, EXPRESSIVE-EYED, BRILLIANT-HEARTED, INSPIRING-MINDED, AND ABSOLUTELY UNFORGETTABLE JINU SPOTTED?!??");

    public HomePageView(ViewManagerModel viewManagerModel, JPanel cardPanel) {
        this.viewManagerModel = viewManagerModel;
        this.cardPanel = cardPanel;
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
            JPanel feedRow = new JPanel();
            feedRow.setLayout(new BoxLayout(feedRow, BoxLayout.X_AXIS));
            PostPanel postPanel = new PostPanel(viewManagerModel, trialpost, 1000, 400, cardPanel);
            feedRow.setMaximumSize(new Dimension(2000, 420));
            feedRow.add(postPanel);

            feedRow.add(Box.createRigidArea(new Dimension(20, 0))); // spacing
            feedRow.add(new PostPanel(viewManagerModel, postex2, 1000, 400, cardPanel)); // second post
            feedRow.add(Box.createHorizontalGlue());

            feedRow.add(Box.createRigidArea(new Dimension(20, 0))); // spacing
            feedRow.add(new JLabel("placeholder for clubs list", 22, Color.RED, 3)); // clubs
            feedRow.add(Box.createHorizontalGlue());

            feedPanel.add(feedRow);
            //feedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
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
