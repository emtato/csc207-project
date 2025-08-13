package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import entity.Club;
import entity.Post;
import interface_adapter.ViewManagerModel;
import interface_adapter.list_clubs.ListClubsController;
import interface_adapter.join_club.JoinClubController;
import interface_adapter.clubs_home.ClubState;
import interface_adapter.clubs_home.ClubViewModel;
import interface_adapter.like_post.LikePostController;
import interface_adapter.specific_club.SpecificClubController;
import interface_adapter.specific_club.SpecificClubViewModel;
import view.ui_components.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ClubHomePageView extends JPanel implements PropertyChangeListener {
    private final String viewName = "club view";
    private final ViewManagerModel viewManagerModel;
    private final ClubViewModel clubViewModel;
    private ListClubsController listClubsController;
    private JoinClubController joinClubController;
    private final JPanel cardPanel;
    private LikePostController likePostController;
    private final SpecificClubViewModel specificClubViewModel;
    private SpecificClubController specificClubController;  // Remove final to allow setting later

    public ClubHomePageView(ViewManagerModel viewManagerModel,
                          ClubViewModel clubViewModel,
                          JPanel cardPanel,
                          SpecificClubViewModel specificClubViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.clubViewModel = clubViewModel;
        this.cardPanel = cardPanel;
        this.specificClubViewModel = specificClubViewModel;

        clubViewModel.addPropertyChangeListener(this);

        JPanel mainPanel = createMainPanel();
        this.add(mainPanel);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refresh();
            }
        });
    }

    private void refresh() {
        String username = app.Session.getCurrentAccount().getUsername();
        if (listClubsController != null) {
            listClubsController.fetch(username);
        }
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        ClubState state = clubViewModel.getState();

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createMyClubsPanel(state), BorderLayout.NORTH);
        headerPanel.add(createAnnouncementsPanel(state), BorderLayout.SOUTH);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)), BorderLayout.NORTH);
        mainPanel.add(headerPanel, BorderLayout.CENTER);
        mainPanel.add(createExploreClubsPanel(state), BorderLayout.EAST);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createMyClubsPanel(ClubState state) {
        JPanel myClubsPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("My Clubs");
        title.setFont(GUIConstants.FONT_TITLE);
        myClubsPanel.add(title, BorderLayout.NORTH);

        JPanel clubsListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (Club club : state.getMemberClubs()) {
            clubsListPanel.add(createClubIcon(club));
        }

        myClubsPanel.add(clubsListPanel, BorderLayout.CENTER);
        return myClubsPanel;
    }

    private JPanel createAnnouncementsPanel(ClubState state) {
        JPanel announcementsPanel = new JPanel(new BorderLayout(0, 5));
        announcementsPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel announcementsTitle = new JLabel("Announcements");
        announcementsTitle.setFont(GUIConstants.FONT_TITLE);
        announcementsPanel.add(announcementsTitle, BorderLayout.NORTH);

        JPanel postsContainer = new JPanel();
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(GUIConstants.WHITE);

        for (int i = 0; i < state.getAnnouncements().size(); i += 2) {
            postsContainer.add(createPostRow(state.getAnnouncements(), i));
            postsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        JScrollPane scrollPane = new JScrollPane(postsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(1100, 400));

        announcementsPanel.add(scrollPane, BorderLayout.CENTER);
        return announcementsPanel;
    }

    private JPanel createExploreClubsPanel(ClubState state) {
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

        for (Club club : state.getNonMemberClubs()) {
            exploringPanel.add(createExploreClubPanel(club));
            exploringPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        exploreClubsPanel.add(exploringPanel);
        exploreClubsPanel.add(createCreateClubPanel());

        return exploreClubsPanel;
    }

    private JPanel createPostRow(java.util.List<Post> posts, int startIndex) {
        JPanel feedRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        feedRow.setBackground(GUIConstants.WHITE);

        PostPanel postPanel = new PostPanel(viewManagerModel, posts.get(startIndex), 500, 400, likePostController);
        postPanel.setMaximumSize(new Dimension(500, 420));
        feedRow.add(postPanel);

        if (startIndex + 1 < posts.size()) {
            PostPanel postTwo = new PostPanel(viewManagerModel, posts.get(startIndex + 1), 500, 400, likePostController);
            postTwo.setMaximumSize(new Dimension(500, 420));
            feedRow.add(postTwo);
        }

        return feedRow;
    }

    private JPanel createClubIcon(Club club) {
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
                // Get the existing SpecificClubView instance from ViewManagerModel
                SpecificClubView existingView = viewManagerModel.getSpecificClubView();
                // Update it with the new club context
                existingView.updateClub(club);
                viewManagerModel.setState(existingView.getViewName());
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

        return clubIconPanel;
    }

    private JPanel createExploreClubPanel(Club club) {
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

        JPanel exploreTextPanel = createExploreTextPanel(club);
        explorePanel.add(exploreTextPanel);

        return explorePanel;
    }

    private JPanel createExploreTextPanel(Club club) {
        JPanel exploreTextPanel = new JPanel();
        exploreTextPanel.setLayout(new BoxLayout(exploreTextPanel, BoxLayout.Y_AXIS));
        exploreTextPanel.setBackground(GUIConstants.WHITE);

        JPanel titleAndButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleAndButtonPanel.setBackground(GUIConstants.WHITE);

        JLabel exploreLabel = new JLabel(club.getName());
        exploreLabel.setFont(GUIConstants.FONT_TEXT);
        titleAndButtonPanel.add(exploreLabel);

        JButton joinButton = createJoinButton(club);
        titleAndButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        titleAndButtonPanel.add(joinButton);

        exploreTextPanel.add(titleAndButtonPanel);
        exploreTextPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel exploreDescription = new JLabel("<html><div style='width:200px'>" +
                club.getDescription() + "</div></html>");
        exploreDescription.setFont(GUIConstants.SMALL_FONT_TEXT);
        exploreDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        exploreTextPanel.add(exploreDescription);

        return exploreTextPanel;
    }

    private JButton createJoinButton(Club club) {
        JButton joinButton = new JButton("Join Club");
        String username = app.Session.getCurrentAccount().getUsername();
        joinButton.addActionListener(e -> {
            if (joinClubController != null) {
                joinClubController.join(username, club.getId());
            }
        });
        return joinButton;
    }

    private JPanel createCreateClubPanel() {
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
                // Navigate to create club view without passing controllers - they should be set up in AppBuilder
                viewManagerModel.setState("create club view");
            }
        });

        return createClubPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            this.removeAll();
            this.add(createMainPanel());
            this.revalidate();
            this.repaint();
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setLikePostController(LikePostController controller) {
        this.likePostController = controller;
    }

    public void setListClubsController(ListClubsController controller) { this.listClubsController = controller; }
    public void setJoinClubController(JoinClubController controller) { this.joinClubController = controller; }
    public void setSpecificClubController(SpecificClubController controller) { this.specificClubController = controller; }
}
