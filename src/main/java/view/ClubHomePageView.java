package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.JLabel;


import data_access.*;
import entity.Account;
import entity.Post;
import interface_adapter.ViewManagerModel;
import view.ui_components.MenuBarPanel;
import view.ui_components.PostPanel;
import view.ui_components.RoundImagePanel;


/**
 * The View for the Club Home Page.
 * This view displays the club's home page with a title.
 */
public class ClubHomePageView extends JPanel {

    private final String viewName = "club view";
    private final ViewManagerModel viewManagerModel;
    private final JPanel cardPanel;
    private final DBClubsDataAccessObject clubsDataAccessObject = new DBClubsDataAccessObject();
    private final UserDataAccessObject userDataAccessObject = FileUserDataAccessObject.getInstance();
    private Post postex2 = new Post(new Account("jinufan333", "WOOF ARF BARK BARK"), 2384723473L, "titler?", "IS THAT MY HANDSOME, ELEGANT, INTELLIGENT, CHARMING, KIND, THOUGHTFUL, STRONG, COURAGEOUS, CREATIVE, BRILLIANT, GENTLE, HUMBLE, GENEROUS, PASSIONATE, WISE, FUNNY, LOYAL, DEPENDABLE, GRACEFUL, RADIANT, CALM, CONFIDENT, WARM, COMPASSIONATE, WITTY, ADVENTUROUS, RESPECTFUL, SINCERE, MAGNETIC, BOLD, ARTICULATE, EMPATHETIC, INSPIRING, HONEST, PATIENT, POWERFUL, ATTENTIVE, UPLIFTING, CLASSY, FRIENDLY, RELIABLE, AMBITIOUS, INTUITIVE, TALENTED, SUPPORTIVE, GROUNDED, DETERMINED, CHARISMATIC, EXTRAORDINARY, TRUSTWORTHY, NOBLE, DIGNIFIED, PERCEPTIVE, INNOVATIVE, REFINED, CONSIDERATE, BALANCED, OPEN-MINDED, COMPOSED, IMAGINATIVE, MINDFUL, OPTIMISTIC, VIRTUOUS, NOBLE-HEARTED, WELL-SPOKEN, QUICK-WITTED, DEEP, PHILOSOPHICAL, FEARLESS, AFFECTIONATE, EXPRESSIVE, EMOTIONALLY INTELLIGENT, RESOURCEFUL, DELIGHTFUL, FASCINATING, SHARP, SELFLESS, DRIVEN, ASSERTIVE, AUTHENTIC, VIBRANT, PLAYFUL, OBSERVANT, SKILLFUL, GENEROUS-SPIRITED, PRACTICAL, COMFORTING, BRAVE, WISE-HEARTED, ENTHUSIASTIC, DEPENDABLE, TACTFUL, ENDURING, DISCREET, WELL-MANNERED, COMPOSED, MATURE, TASTEFUL, JOYFUL, UNDERSTANDING, GENUINE, BRILLIANT-MINDED, ENCOURAGING, WELL-ROUNDED, MAGNETIC, DYNAMIC, RADIANT, RADIANT-SPIRITED, SOULFUL, RADIANT-HEARTED, INSIGHTFUL, CREATIVE-SOULED, JUSTICE-MINDED, RELIABLE-HEARTED, TENDER, UPLIFTING-MINDED, PERSEVERING, DEVOTED, ANGELIC, DOWN-TO-EARTH, GOLDEN-HEARTED, GENTLE-SPIRITED, CLEVER, COURAGEOUS-HEARTED, COURTEOUS, HARMONIOUS, LOYAL-MINDED, BEAUTIFUL-SOULED, EASYGOING, SINCERE-HEARTED, RESPECTFUL-MINDED, COMFORTING-VOICED, CONFIDENT-MINDED, EMOTIONALLY STRONG, RESPECTFUL-SOULED, IMAGINATIVE-HEARTED, PROTECTIVE, NOBLE-MINDED, CONFIDENT-SOULED, WISE-EYED, LOVING, SERENE, MAGNETIC-SOULED, EXPRESSIVE-EYED, BRILLIANT-HEARTED, INSPIRING-MINDED, AND ABSOLUTELY UNFORGETTABLE JINU SPOTTED?!?? \n haha get it jinu is sustenance");
    private PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject = FilePostCommentLikesDataAccessObject.getInstance();

    public ClubHomePageView(ViewManagerModel viewManagerModel, JPanel cardPanel) {

        this.viewManagerModel = viewManagerModel;
        this.cardPanel = cardPanel;

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());

        // my clubs panel
        JPanel myClubsPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("My Clubs");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        myClubsPanel.add(title, BorderLayout.NORTH);

        JPanel clubsListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        for (int i = 0; i < 8; i++) {
            // club icon
            JPanel clubIconPanel = new JPanel(new BorderLayout());
            clubIconPanel.setPreferredSize(new Dimension(150, 150));
            clubIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            RoundImagePanel roundPanel = new RoundImagePanel("images/Homemade-French-Fries_8.jpg");
            roundPanel.setPreferredSize(new Dimension(100, 100));
            JPanel centeringPanel = new JPanel();
            centeringPanel.add(roundPanel);
            centeringPanel.setBackground(GUIConstants.WHITE);
            clubIconPanel.add(centeringPanel, BorderLayout.CENTER);

            JLabel clubIconName = new JLabel("Club " + (i + 1));
            clubIconName.setFont(GUIConstants.FONT_TEXT);
            clubIconName.setHorizontalAlignment(SwingConstants.CENTER);
            clubIconPanel.add(clubIconName, BorderLayout.SOUTH);
            clubIconPanel.setBackground(GUIConstants.WHITE);

            clubIconPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SpecificClubView specificClubView = new SpecificClubView(viewManagerModel, cardPanel);
                    cardPanel.add(specificClubView, specificClubView.getViewName());
                    viewManagerModel.setState(specificClubView.getViewName());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    clubIconPanel.setBackground(Color.LIGHT_GRAY);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    clubIconPanel.setBackground(GUIConstants.WHITE);
                }
            });

            clubsListPanel.add(clubIconPanel);
        }

        myClubsPanel.add(clubsListPanel, BorderLayout.CENTER);

        // club announcements panel
        JPanel announcementsPanel = new JPanel(new BorderLayout(0, 5));
        announcementsPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel announcementsTitle = new JLabel("Announcements");
        announcementsTitle.setFont(GUIConstants.FONT_TITLE);
        announcementsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        announcementsPanel.add(announcementsTitle, BorderLayout.NORTH);

        // Create a panel to hold all posts
        JPanel postsContainer = new JPanel();
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(GUIConstants.WHITE);
        postsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add posts in rows of two
        for (int i = 0; i < 3; i++) {
            JPanel feedRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            feedRow.setBackground(GUIConstants.WHITE);

            PostPanel postPanel = new PostPanel(viewManagerModel, postex2, 500, 400, postCommentsLikesDataAccessObject);
            postPanel.setMaximumSize(new Dimension(500, 420));
            feedRow.add(postPanel);

            PostPanel postTwo = new PostPanel(viewManagerModel, postex2, 500, 400, postCommentsLikesDataAccessObject);
            postTwo.setMaximumSize(new Dimension(500, 420));
            feedRow.add(postTwo);

            postsContainer.add(feedRow);
            postsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Create scroll pane for posts
        JScrollPane scrollPane = new JScrollPane(postsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(1100, 400));

        announcementsPanel.add(scrollPane, BorderLayout.CENTER);

        headerPanel.add(myClubsPanel, BorderLayout.NORTH);
        headerPanel.add(announcementsPanel, BorderLayout.SOUTH);

        // explore clubs panel
        JPanel exploreClubsPanel = new JPanel();
        exploreClubsPanel.setLayout(new BoxLayout(exploreClubsPanel, BoxLayout.Y_AXIS));

        JLabel exploreTitle = new JLabel("Explore Clubs");
        exploreTitle.setFont(GUIConstants.FONT_TITLE);
        exploreTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        exploreClubsPanel.add(exploreTitle);
        exploreClubsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel exploringPanel = new JPanel();
        exploringPanel.setLayout(new BoxLayout(exploringPanel, BoxLayout.Y_AXIS));
        exploringPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < 4; i++) {
            JPanel explorePanel = new JPanel(new BorderLayout(5, 5));
            explorePanel.setBackground(GUIConstants.WHITE);
            explorePanel.setMaximumSize(new Dimension(370, 130));
            explorePanel.setPreferredSize(new Dimension(370, 130));
            explorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            RoundImagePanel exploreRoundPanel = new RoundImagePanel("images/Homemade-French-Fries_8.jpg");
            exploreRoundPanel.setPreferredSize(new Dimension(100, 100));

            // Add a wrapper panel to maintain the round panel's size
            JPanel imageWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
            imageWrapper.setBackground(GUIConstants.WHITE);
            imageWrapper.add(exploreRoundPanel);
            explorePanel.add(imageWrapper, BorderLayout.WEST);

            JPanel exploreTextPanel = new JPanel();
            exploreTextPanel.setLayout(new BoxLayout(exploreTextPanel, BoxLayout.Y_AXIS));
            exploreTextPanel.setBackground(GUIConstants.WHITE);

            JLabel exploreLabel = new JLabel("Club " + (i + 1));
            exploreLabel.setFont(GUIConstants.FONT_TEXT);
            exploreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel exploreDescription = new JLabel("<html><div style='width:200px'>" +
                    "Explore the wonders of Club " + (i + 1) + " and join the community!" +
                    "</div></html>");
            exploreDescription.setFont(GUIConstants.SMALL_FONT_TEXT);
            exploreDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

            exploreTextPanel.add(exploreLabel);
            exploreTextPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            exploreTextPanel.add(exploreDescription);

            explorePanel.add(exploreTextPanel);
            exploringPanel.add(explorePanel);

            if (i < 4) {
                exploringPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        exploreClubsPanel.add(exploringPanel);
        exploreClubsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel createClubPanel = new JPanel(new BorderLayout());
        createClubPanel.setPreferredSize(new Dimension(150, 100));
        createClubPanel.setMaximumSize(new Dimension(370, 100));
        createClubPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        createClubPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createClubPanel.setBackground(GUIConstants.RED);

        JLabel clubIconName = new JLabel("Create Club");
        clubIconName.setFont(GUIConstants.FONT_HEADER);
        clubIconName.setForeground(Color.WHITE);
        clubIconName.setHorizontalAlignment(SwingConstants.CENTER);
        createClubPanel.add(clubIconName, BorderLayout.CENTER);

        createClubPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String currentUsername = userDataAccessObject.getCurrentUsername();
                Account currentUser = (Account) userDataAccessObject.get(currentUsername);
                CreateClubView createClubView = new CreateClubView(viewManagerModel, clubsDataAccessObject, currentUser);
                cardPanel.add(createClubView, createClubView.getViewName());
                viewManagerModel.setState(createClubView.getViewName());
            }
        });

        exploreClubsPanel.add(createClubPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)), BorderLayout.NORTH);

        mainPanel.add(headerPanel, BorderLayout.CENTER);
        mainPanel.add(exploreClubsPanel, BorderLayout.EAST);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);

    }

    public String getViewName() {
        return viewName;
    }
}
