package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.JLabel;


import data_access.*;
import entity.Account;
import entity.Club;
import entity.Post;
import interface_adapter.ViewManagerModel;
import interface_adapter.like_post.LikePostController;
import view.ui_components.MenuBarPanel;
import view.ui_components.PostPanel;
import view.ui_components.RoundImagePanel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * The View for the Club Home Page.
 * This view displays the club's home page with a title.
 */
public class ClubHomePageView extends JPanel {

    private final String viewName = "club view";
    private final ViewManagerModel viewManagerModel;
    private final JPanel cardPanel;
    private final PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject = FilePostCommentLikesDataAccessObject.getInstance();
    private final DBClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject = FileUserDataAccessObject.getInstance();
    private Post postex2 = new Post(new Account("jinufan333", "WOOF ARF BARK BARK"), 2384723473L, "titler?", "IS THAT MY HANDSOME, ELEGANT, INTELLIGENT, CHARMING, KIND, THOUGHTFUL, STRONG, COURAGEOUS, CREATIVE, BRILLIANT, GENTLE, HUMBLE, GENEROUS, PASSIONATE, WISE, FUNNY, LOYAL, DEPENDABLE, GRACEFUL, RADIANT, CALM, CONFIDENT, WARM, COMPASSIONATE, WITTY, ADVENTUROUS, RESPECTFUL, SINCERE, MAGNETIC, BOLD, ARTICULATE, EMPATHETIC, INSPIRING, HONEST, PATIENT, POWERFUL, ATTENTIVE, UPLIFTING, CLASSY, FRIENDLY, RELIABLE, AMBITIOUS, INTUITIVE, TALENTED, SUPPORTIVE, GROUNDED, DETERMINED, CHARISMATIC, EXTRAORDINARY, TRUSTWORTHY, NOBLE, DIGNIFIED, PERCEPTIVE, INNOVATIVE, REFINED, CONSIDERATE, BALANCED, OPEN-MINDED, COMPOSED, IMAGINATIVE, MINDFUL, OPTIMISTIC, VIRTUOUS, NOBLE-HEARTED, WELL-SPOKEN, QUICK-WITTED, DEEP, PHILOSOPHICAL, FEARLESS, AFFECTIONATE, EXPRESSIVE, EMOTIONALLY INTELLIGENT, RESOURCEFUL, DELIGHTFUL, FASCINATING, SHARP, SELFLESS, DRIVEN, ASSERTIVE, AUTHENTIC, VIBRANT, PLAYFUL, OBSERVANT, SKILLFUL, GENEROUS-SPIRITED, PRACTICAL, COMFORTING, BRAVE, WISE-HEARTED, ENTHUSIASTIC, DEPENDABLE, TACTFUL, ENDURING, DISCREET, WELL-MANNERED, COMPOSED, MATURE, TASTEFUL, JOYFUL, UNDERSTANDING, GENUINE, BRILLIANT-MINDED, ENCOURAGING, WELL-ROUNDED, MAGNETIC, DYNAMIC, RADIANT, RADIANT-SPIRITED, SOULFUL, RADIANT-HEARTED, INSIGHTFUL, CREATIVE-SOULED, JUSTICE-MINDED, RELIABLE-HEARTED, TENDER, UPLIFTING-MINDED, PERSEVERING, DEVOTED, ANGELIC, DOWN-TO-EARTH, GOLDEN-HEARTED, GENTLE-SPIRITED, CLEVER, COURAGEOUS-HEARTED, COURTEOUS, HARMONIOUS, LOYAL-MINDED, BEAUTIFUL-SOULED, EASYGOING, SINCERE-HEARTED, RESPECTFUL-MINDED, COMFORTING-VOICED, CONFIDENT-MINDED, EMOTIONALLY STRONG, RESPECTFUL-SOULED, IMAGINATIVE-HEARTED, PROTECTIVE, NOBLE-MINDED, CONFIDENT-SOULED, WISE-EYED, LOVING, SERENE, MAGNETIC-SOULED, EXPRESSIVE-EYED, BRILLIANT-HEARTED, INSPIRING-MINDED, AND ABSOLUTELY UNFORGETTABLE JINU SPOTTED?!?? \n haha get it jinu is sustenance");
    private LikePostController likePostController;

    public ClubHomePageView(ViewManagerModel viewManagerModel, JPanel cardPanel) {

        this.viewManagerModel = viewManagerModel;
        this.cardPanel = cardPanel;
        this.clubsDataAccessObject = new DBClubsDataAccessObject(postCommentsLikesDataAccessObject);

        // Initial setup
        JPanel mainPanel = createMainPanel();
        this.add(mainPanel);

        // Add a component listener to refresh when shown
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refresh();
            }
        });
    }

    // Add a refresh method
    public void refresh() {
        this.removeAll();
        this.add(createMainPanel());
        this.revalidate();
        this.repaint();
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel headerPanel = new JPanel(new BorderLayout());

        // my clubs panel
        JPanel myClubsPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("My Clubs");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        myClubsPanel.add(title, BorderLayout.NORTH);

        JPanel clubsListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String currentUsername = userDataAccessObject.getCurrentUsername();

        // Get all clubs and filter them
        ArrayList<Club> allClubs = clubsDataAccessObject.getAllClubs();
        ArrayList<Club> memberClubs = new ArrayList<>();
        ArrayList<Club> nonMemberClubs = new ArrayList<>();

        // Sort clubs into member and non-member lists based on user's saved club memberships
        Account currentUser = (Account) userDataAccessObject.get(currentUsername);
        ArrayList<String> userClubIds = currentUser != null && currentUser.getClubs() != null ?
                                      currentUser.getClubs() : new ArrayList<>();

        for (Club club : allClubs) {
            if (userClubIds.contains(String.valueOf(club.getId()))) {
                memberClubs.add(club);
            } else {
                nonMemberClubs.add(club);
            }
        }

        // Display user's clubs
        for (Club club : memberClubs) {
            JPanel clubIconPanel = new JPanel(new BorderLayout());
            clubIconPanel.setPreferredSize(new Dimension(150, 150));
            clubIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            RoundImagePanel roundPanel = new RoundImagePanel("images/Homemade-French-Fries_8.jpg");
            roundPanel.setPreferredSize(new Dimension(100, 100));
            JPanel centeringPanel = new JPanel();
            centeringPanel.add(roundPanel);
            centeringPanel.setBackground(GUIConstants.WHITE);
            clubIconPanel.add(centeringPanel, BorderLayout.CENTER);

            JLabel clubIconName = new JLabel(club.getName());
            clubIconName.setFont(GUIConstants.FONT_TEXT);
            clubIconName.setHorizontalAlignment(SwingConstants.CENTER);
            clubIconPanel.add(clubIconName, BorderLayout.SOUTH);
            clubIconPanel.setBackground(GUIConstants.WHITE);

            clubIconPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SpecificClubView specificClubView = new SpecificClubView(viewManagerModel, cardPanel, club);
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
        announcementsTitle.setHorizontalAlignment(SwingConstants.LEFT);
        announcementsPanel.add(announcementsTitle, BorderLayout.NORTH);

        // Create a panel to hold all posts
        JPanel postsContainer = new JPanel();
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(GUIConstants.WHITE);
        postsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Collect posts from all clubs the user is a member of
        ArrayList<Post> clubPosts = new ArrayList<>();
        for (Club club : memberClubs) {
            if (club.getPosts() != null) {
                clubPosts.addAll(club.getPosts());
            }
        }

        // Add posts in rows of two
        for (int i = 0; i < clubPosts.size(); i += 2) {
            JPanel feedRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            feedRow.setBackground(GUIConstants.WHITE);

            // Add first post
            PostPanel postPanel = new PostPanel(viewManagerModel, clubPosts.get(i), 500, 400, likePostController);
            postPanel.setMaximumSize(new Dimension(500, 420));
            feedRow.add(postPanel);

            // Add second post if it exists
            if (i + 1 < clubPosts.size()) {
                PostPanel postTwo = new PostPanel(viewManagerModel, clubPosts.get(i + 1), 500, 400, likePostController);
                postTwo.setMaximumSize(new Dimension(500, 420));
                feedRow.add(postTwo);
            }

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

        // Display clubs user is not a member of
        for (Club club : nonMemberClubs) {
            JPanel explorePanel = new JPanel(new BorderLayout(5, 5));
            explorePanel.setBackground(GUIConstants.WHITE);
            explorePanel.setMaximumSize(new Dimension(370, 130));
            explorePanel.setPreferredSize(new Dimension(370, 130));
            explorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            RoundImagePanel exploreRoundPanel = new RoundImagePanel("images/Homemade-French-Fries_8.jpg");
            exploreRoundPanel.setPreferredSize(new Dimension(100, 100));

            JPanel imageWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
            imageWrapper.setBackground(GUIConstants.WHITE);
            imageWrapper.add(exploreRoundPanel);
            explorePanel.add(imageWrapper, BorderLayout.WEST);

            JPanel exploreTextPanel = new JPanel();
            exploreTextPanel.setLayout(new BoxLayout(exploreTextPanel, BoxLayout.Y_AXIS));
            exploreTextPanel.setBackground(GUIConstants.WHITE);

            // Panel for club name and join button
            JPanel titleAndButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titleAndButtonPanel.setBackground(GUIConstants.WHITE);

            JLabel exploreLabel = new JLabel(club.getName());
            exploreLabel.setFont(GUIConstants.FONT_TEXT);
            titleAndButtonPanel.add(exploreLabel);

            JButton joinButton = new JButton("Join");
            joinButton.setBackground(GUIConstants.RED);
            joinButton.setForeground(Color.WHITE);
            joinButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            joinButton.addActionListener(e -> {
                ArrayList<Account> members = club.getMembers();
                members.add(new Account(currentUsername, ""));

                // Update club with new member
                clubsDataAccessObject.writeClub(
                    club.getId(),
                    members,
                    club.getName(),
                    club.getDescription(),
                    club.getPosts(),
                    club.getTags()
                );

                // Update the user's data to include the new club membership
                Account updatedUser = (Account) userDataAccessObject.get(currentUsername);
                if (updatedUser != null) {
                    ArrayList<String> clubList = updatedUser.getClubs();
                    if (clubList == null) {
                        clubList = new ArrayList<>();
                    }
                    clubList.add(String.valueOf(club.getId()));
                    updatedUser.setClubs(clubList);
                    userDataAccessObject.save(updatedUser);
                }

                // Remove all components and rebuild the view
                this.removeAll();

                // Create new ClubHomePageView content
                JPanel newMainPanel = createMainPanel();
                this.add(newMainPanel);

                // Force the panel to redraw
                this.revalidate();
                this.repaint();
            });

            titleAndButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            titleAndButtonPanel.add(joinButton);

            exploreTextPanel.add(titleAndButtonPanel);
            exploreTextPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            JLabel exploreDescription = new JLabel("<html><div style='width:200px'>" +
                    club.getDescription() +
                    "</div></html>");
            exploreDescription.setFont(GUIConstants.SMALL_FONT_TEXT);
            exploreDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
            exploreTextPanel.add(exploreDescription);

            explorePanel.add(exploreTextPanel);
            exploringPanel.add(explorePanel);
            exploringPanel.add(Box.createRigidArea(new Dimension(0, 10)));
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

        return mainPanel;
    }

    public String getViewName() {
        return viewName;
    }
    public void setLikePostController(LikePostController controller) {
        this.likePostController = controller;
    }
}
