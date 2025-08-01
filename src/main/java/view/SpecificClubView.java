package view;

import entity.Account;
import entity.Post;
import interface_adapter.ViewManagerModel;
import view.UI_components.EventsPanel;
import view.UI_components.MenuBarPanel;
import view.UI_components.PostPanel;
import view.UI_components.RoundImagePanel;

import javax.swing.*;
import javax.swing.JLabel;
import java.awt.*;

public class SpecificClubView extends JPanel {
    private final String viewName = "specific club view";
    private final ViewManagerModel viewManagerModel;
    private final JPanel cardPanel;
    private Post postex2 = new Post(new Account("jinufan333", "WOOF ARF BARK BARK"), 2384723473L, "titler?", "IS THAT MY HANDSOME, ELEGANT, INTELLIGENT, CHARMING, KIND, THOUGHTFUL, STRONG, COURAGEOUS, CREATIVE, BRILLIANT, GENTLE, HUMBLE, GENEROUS, PASSIONATE, WISE, FUNNY, LOYAL, DEPENDABLE, GRACEFUL, RADIANT, CALM, CONFIDENT, WARM, COMPASSIONATE, WITTY, ADVENTUROUS, RESPECTFUL, SINCERE, MAGNETIC, BOLD, ARTICULATE, EMPATHETIC, INSPIRING, HONEST, PATIENT, POWERFUL, ATTENTIVE, UPLIFTING, CLASSY, FRIENDLY, RELIABLE, AMBITIOUS, INTUITIVE, TALENTED, SUPPORTIVE, GROUNDED, DETERMINED, CHARISMATIC, EXTRAORDINARY, TRUSTWORTHY, NOBLE, DIGNIFIED, PERCEPTIVE, INNOVATIVE, REFINED, CONSIDERATE, BALANCED, OPEN-MINDED, COMPOSED, IMAGINATIVE, MINDFUL, OPTIMISTIC, VIRTUOUS, NOBLE-HEARTED, WELL-SPOKEN, QUICK-WITTED, DEEP, PHILOSOPHICAL, FEARLESS, AFFECTIONATE, EXPRESSIVE, EMOTIONALLY INTELLIGENT, RESOURCEFUL, DELIGHTFUL, FASCINATING, SHARP, SELFLESS, DRIVEN, ASSERTIVE, AUTHENTIC, VIBRANT, PLAYFUL, OBSERVANT, SKILLFUL, GENEROUS-SPIRITED, PRACTICAL, COMFORTING, BRAVE, WISE-HEARTED, ENTHUSIASTIC, DEPENDABLE, TACTFUL, ENDURING, DISCREET, WELL-MANNERED, COMPOSED, MATURE, TASTEFUL, JOYFUL, UNDERSTANDING, GENUINE, BRILLIANT-MINDED, ENCOURAGING, WELL-ROUNDED, MAGNETIC, DYNAMIC, RADIANT, RADIANT-SPIRITED, SOULFUL, RADIANT-HEARTED, INSIGHTFUL, CREATIVE-SOULED, JUSTICE-MINDED, RELIABLE-HEARTED, TENDER, UPLIFTING-MINDED, PERSEVERING, DEVOTED, ANGELIC, DOWN-TO-EARTH, GOLDEN-HEARTED, GENTLE-SPIRITED, CLEVER, COURAGEOUS-HEARTED, COURTEOUS, HARMONIOUS, LOYAL-MINDED, BEAUTIFUL-SOULED, EASYGOING, SINCERE-HEARTED, RESPECTFUL-MINDED, COMFORTING-VOICED, CONFIDENT-MINDED, EMOTIONALLY STRONG, RESPECTFUL-SOULED, IMAGINATIVE-HEARTED, PROTECTIVE, NOBLE-MINDED, CONFIDENT-SOULED, WISE-EYED, LOVING, SERENE, MAGNETIC-SOULED, EXPRESSIVE-EYED, BRILLIANT-HEARTED, INSPIRING-MINDED, AND ABSOLUTELY UNFORGETTABLE JINU SPOTTED?!?? \n haha get it jinu is sustenance");

    /**
     * Constructor for the SpecificClubView.
     *
     * @param viewManagerModel The ViewManagerModel that manages the state of the view.
     * @param cardPanel        The JPanel that contains this view.
     */
    public SpecificClubView(ViewManagerModel viewManagerModel, JPanel cardPanel) {
        this.viewManagerModel = viewManagerModel;
        this.cardPanel = cardPanel;

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        RoundImagePanel exploreRoundPanel = new RoundImagePanel("images/Homemade-French-Fries_8.jpg");
        exploreRoundPanel.setPreferredSize(new Dimension(150, 150));  // Reduced from 250x250
        headerPanel.add(exploreRoundPanel);

        JPanel headerTextPanel = new JPanel(new BorderLayout(10, 5));  // Added gaps

        JLabel titleLabel = new JLabel("Club Name");
        titleLabel.setFont(GUIConstants.FONT_TITLE);
        headerTextPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel descriptionLabel = new JLabel("This is a description of the club. It can be a bit longer to provide more details about the club's activities and purpose.");
        descriptionLabel.setFont(GUIConstants.FONT_TEXT);
        headerTextPanel.add(descriptionLabel, BorderLayout.CENTER);

        headerPanel.add(headerTextPanel);

        JPanel membersPanel = new JPanel(new BorderLayout());

        ImageIcon membersIcon = new ImageIcon("images/toppng.com-white-person-icon-people-white-icon-abstract-backgrounds-436x368.png");
        Image scaledImage = membersIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        membersPanel.add(iconLabel, BorderLayout.NORTH);

        JLabel membersLabel = new JLabel("10");
        membersLabel.setFont(GUIConstants.FONT_TEXT);
        membersPanel.add(membersLabel, BorderLayout.SOUTH);

        headerPanel.add(membersPanel);

        JPanel leftPanel = new JPanel(new BorderLayout());

        JPanel announcementsPanel = new JPanel(new BorderLayout());

        JLabel announcementsLabel = new JLabel("Announcements");
        announcementsLabel.setFont(GUIConstants.FONT_HEADER);
        announcementsPanel.add(announcementsLabel, BorderLayout.NORTH);

        // Create a panel to hold all posts
        JPanel postsContainer = new JPanel();
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(GUIConstants.WHITE);

        // Add posts vertically in announcements
        for (int i = 0; i < 3; i++) {
            PostPanel postPanel = new PostPanel(viewManagerModel, postex2, 500, 400, cardPanel);
            postPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
            postPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            postsContainer.add(postPanel);
            postsContainer.add(Box.createVerticalStrut(10));
        }

        // Create scroll pane for announcements posts
        JScrollPane scrollPane = new JScrollPane(postsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        announcementsPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(announcementsPanel, BorderLayout.NORTH);

        // Events section
        JPanel eventsPanel = new JPanel(new BorderLayout());
        JLabel eventsLabel = new JLabel("Events");
        eventsLabel.setFont(GUIConstants.FONT_HEADER);
        eventsPanel.add(eventsLabel, BorderLayout.NORTH);

        // Create a panel to hold all events
        JPanel eventsContainer = new JPanel();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(GUIConstants.WHITE);

        // Add posts vertically in events
        for (int i = 0; i < 5; i++) {
            EventsPanel postPanel = new EventsPanel(viewManagerModel);
            postPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
            postPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            eventsContainer.add(postPanel);
            eventsContainer.add(Box.createVerticalStrut(10));
        }

        // Create scroll pane for events posts
        JScrollPane eventsScrollPane = new JScrollPane(eventsContainer);
        eventsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        eventsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        eventsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        eventsScrollPane.setBorder(null);
        eventsScrollPane.setPreferredSize(new Dimension(500, 300));

        eventsPanel.add(eventsScrollPane, BorderLayout.CENTER);
        leftPanel.add(eventsPanel, BorderLayout.CENTER);

        JPanel feedPanel = new JPanel(new BorderLayout());
        JLabel feedLabel = new JLabel("Feed");
        feedLabel.setFont(GUIConstants.FONT_HEADER);
        feedPanel.add(feedLabel, BorderLayout.NORTH);

        // Create a panel to hold all feed posts
        JPanel feedContainer = new JPanel();
        feedContainer.setLayout(new BoxLayout(feedContainer, BoxLayout.Y_AXIS));
        feedContainer.setBackground(GUIConstants.WHITE);

        // Add posts in rows
        for (int i = 0; i < 3; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
            rowPanel.setBackground(GUIConstants.WHITE);

            // Add three posts per row
            for (int j = 0; j < 2; j++) {
                PostPanel postPanel = new PostPanel(viewManagerModel, postex2, 500, 400, cardPanel);
                postPanel.setPreferredSize(new Dimension(500, 400));
                rowPanel.add(postPanel);
            }

            rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            feedContainer.add(rowPanel);
            feedContainer.add(Box.createVerticalStrut(10));
        }

        // Create scroll pane for posts
        JScrollPane feedScrollPane = new JScrollPane(feedContainer);
        feedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        feedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        feedScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        feedScrollPane.setBorder(null);
        feedScrollPane.setPreferredSize(new Dimension(1000, 800));

        feedPanel.add(feedScrollPane, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(feedPanel, BorderLayout.EAST);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)), BorderLayout.CENTER);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);

    }

    public String getViewName() {
        return viewName;
    }
}
