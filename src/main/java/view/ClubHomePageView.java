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

import java.awt.image.BufferedImage;
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

    // Layout constants for Explore Clubs cards
    private static final int EXPLORE_CARD_WIDTH = 370;
    private static final int EXPLORE_CARD_HEIGHT = 130;
    private static final int IMAGE_WIDTH = 100; // width of the round image
    private static final int H_GAP = 5; // BorderLayout hgap used in explore card
    private static final int BORDER_PADDING_TOTAL = 20; // left + right (10 each)
    private static final int CENTER_AVAILABLE_WIDTH = EXPLORE_CARD_WIDTH - BORDER_PADDING_TOTAL - IMAGE_WIDTH - H_GAP; // width available for text + button area
    private static final int JOIN_BUTTON_WIDTH = 90;
    private static final int JOIN_BUTTON_HEIGHT = 28;
    private static final int TITLE_LABEL_WIDTH = Math.max(60, CENTER_AVAILABLE_WIDTH - JOIN_BUTTON_WIDTH - 30); // leave small gap
    private static final int DESCRIPTION_WIDTH = CENTER_AVAILABLE_WIDTH; // full width below button row

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
        // Safely obtain the username; fall back to stored currentUsername if Account not loaded
        String username;
        if (app.Session.getCurrentAccount() != null) {
            username = app.Session.getCurrentAccount().getUsername();
        } else {
            username = app.Session.getCurrentUsername();
        }
        if (listClubsController != null && username != null) {
            System.out.println("[ClubHome][DEBUG] Refresh fetch clubs for user='" + username + "'");
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

        String img = club.getImageUrl();
        System.out.println("[ClubHome][DEBUG] Rendering member club id=" + club.getId() + " name='" + club.getName() + "' imageUrl='" + img + "'");
        RoundImagePanel roundPanel = new RoundImagePanel(img);
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
        // Restore original fixed height so panels do not grow taller
        explorePanel.setPreferredSize(new Dimension(EXPLORE_CARD_WIDTH, EXPLORE_CARD_HEIGHT));
        explorePanel.setMaximumSize(new Dimension(EXPLORE_CARD_WIDTH, EXPLORE_CARD_HEIGHT));
        explorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String img = club.getImageUrl();
        System.out.println("[ClubHome][DEBUG] Rendering explore club id=" + club.getId() + " name='" + club.getName() + "' imageUrl='" + img + "'");
        RoundImagePanel exploreRoundPanel = new RoundImagePanel(img);
        exploreRoundPanel.setPreferredSize(new Dimension(100, 100));
        JPanel imageWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        imageWrapper.setBackground(GUIConstants.WHITE);
        imageWrapper.add(exploreRoundPanel);
        explorePanel.add(imageWrapper, BorderLayout.WEST);

        JPanel exploreTextPanel = createExploreTextPanel(club);
        explorePanel.add(exploreTextPanel, BorderLayout.CENTER);

        return explorePanel;
    }

    private JPanel createExploreTextPanel(Club club) {
        JPanel exploreTextPanel = new JPanel();
        exploreTextPanel.setLayout(new BoxLayout(exploreTextPanel, BoxLayout.Y_AXIS));
        exploreTextPanel.setBackground(GUIConstants.WHITE);
        exploreTextPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title row with join button on the right (use GridBagLayout so button height stays small)
        JPanel titleAndButtonPanel = new JPanel(new GridBagLayout());
        titleAndButtonPanel.setBackground(GUIConstants.WHITE);
        titleAndButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.NORTHWEST;

        String title = club.getName() == null ? "" : escapeHtml(club.getName());
        String titleHtml = "<html><div style='width:" + TITLE_LABEL_WIDTH + "px; margin:0; padding:0;'>" + title + "</div></html>";
        JLabel exploreLabel = new JLabel(titleHtml);
        exploreLabel.setFont(GUIConstants.FONT_TEXT);
        exploreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        exploreLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        titleAndButtonPanel.add(exploreLabel, gbc);

        JButton joinButton = createJoinButton(club);
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx = 1; gbcBtn.gridy = 0; gbcBtn.weightx = 0; gbcBtn.weighty = 0; gbcBtn.insets = new Insets(0, 8, 0, 0); gbcBtn.anchor = GridBagConstraints.NORTH; gbcBtn.fill = GridBagConstraints.NONE;
        titleAndButtonPanel.add(joinButton, gbcBtn);

        exploreTextPanel.add(titleAndButtonPanel);
        exploreTextPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Dynamic truncation with ellipsis based on remaining vertical space in fixed-height card
        String rawDesc = club.getDescription() == null ? "" : escapeHtml(club.getDescription());
        int titleHeight = titleAndButtonPanel.getPreferredSize().height; // includes wrapped title lines
        String descHtml = buildDescriptionHtml(rawDesc, titleHeight);
        JLabel exploreDescription = new JLabel(descHtml);
        exploreDescription.setFont(GUIConstants.SMALL_FONT_TEXT);
        exploreDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        exploreTextPanel.add(exploreDescription);

        return exploreTextPanel;
    }

    // Build HTML for description truncated to fit remaining space with ellipsis if needed
    private String buildDescriptionHtml(String text, int titleHeight) {
        int availableHeight = EXPLORE_CARD_HEIGHT - 20 /* top+bottom border padding */ - titleHeight - 5 /* spacer */;
        if (availableHeight <= 0) {
            return wrapHtml("...");
        }
        Font descFont = GUIConstants.SMALL_FONT_TEXT != null ? GUIConstants.SMALL_FONT_TEXT : new JLabel().getFont().deriveFont(11f);
        // Measure using temporary graphics
        BufferedImage bi = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setFont(descFont);
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight();
        int maxLines = Math.max(1, availableHeight / lineHeight);
        java.util.List<String> lines = wrapTextToWidth(text, fm, DESCRIPTION_WIDTH, maxLines);
        boolean truncated = didTruncate(text, lines);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            sb.append(lines.get(i));
            if (i < lines.size() - 1) sb.append("<br>");
        }
        if (truncated) {
            // Ensure last line ends with ellipsis (avoid duplicate)
            if (!sb.toString().endsWith("...")) {
                sb.append("...");
            }
        }
        g2.dispose();
        return "<html><div style='width:" + DESCRIPTION_WIDTH + "px; margin:0; padding:0;'>" + sb + "</div></html>";
    }

    private boolean didTruncate(String original, java.util.List<String> producedLines) {
        String joined = String.join(" ", producedLines).replace("...", "");
        // Rough check: if produced text (sans ellipsis) shorter than original it's truncated
        return original.length() > joined.length();
    }

    private java.util.List<String> wrapTextToWidth(String text, FontMetrics fm, int maxWidth, int maxLines) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String candidate = current.length() == 0 ? word : current + " " + word;
            if (fm.stringWidth(candidate) <= maxWidth) {
                current.setLength(0);
                current.append(candidate);
            } else {
                // commit current line
                if (current.length() > 0) {
                    lines.add(current.toString());
                } else {
                    // single long word; hard cut
                    lines.add(cutWordToFit(word, fm, maxWidth));
                }
                if (lines.size() == maxLines) {
                    return lines; // no space for more lines
                }
                current.setLength(0);
                current.append(word);
            }
            if (i == words.length - 1 && current.length() > 0) {
                lines.add(current.toString());
            }
            if (lines.size() == maxLines) break;
        }
        // If exceeded lines, ensure last line fits and leave rest for truncation
        if (lines.size() > maxLines) {
            return lines.subList(0, maxLines);
        }
        return lines;
    }

    private String cutWordToFit(String word, FontMetrics fm, int maxWidth) {
        if (fm.stringWidth(word) <= maxWidth) return word;
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (fm.stringWidth(sb.toString() + c + "...") > maxWidth) break;
            sb.append(c);
        }
        return sb.toString();
    }

    private String wrapHtml(String inner) {
        return "<html><div style='width:" + DESCRIPTION_WIDTH + "px; margin:0; padding:0;'>" + inner + "</div></html>";
    }

    private JButton createJoinButton(Club club) {
        JButton joinButton = new JButton("Join") {
            @Override public Dimension getPreferredSize() { return new Dimension(JOIN_BUTTON_WIDTH, JOIN_BUTTON_HEIGHT); }
            @Override public Dimension getMinimumSize() { return getPreferredSize(); }
            @Override public Dimension getMaximumSize() { return getPreferredSize(); }
            @Override public void updateUI() {
                super.updateUI(); // ensure UI delegate (and its listeners) are installed
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorder(BorderFactory.createEmptyBorder());
                setOpaque(false);
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 220, 220));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.setColor(Color.DARK_GRAY);
                FontMetrics fm = g2.getFontMetrics();
                String txt = getText();
                int tx = (getWidth() - fm.stringWidth(txt)) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(txt, Math.max(2, tx), ty);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
            }
        };
        // Ensure UI initialized now that we've overridden updateUI
        joinButton.updateUI();

        // Remove original captured username; resolve dynamically on click.
        joinButton.addActionListener(e -> {
            String dynamicUsername = app.Session.getCurrentAccount() != null ?
                    app.Session.getCurrentAccount().getUsername() : app.Session.getCurrentUsername();
            System.out.println("[ClubHome][DEBUG] Join button clicked clubId=" + club.getId() +
                    " user=" + dynamicUsername + " controllerNull=" + (joinClubController == null));
            if (joinClubController == null) {
                JOptionPane.showMessageDialog(this, "Join controller not initialized yet.", "Join Club", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dynamicUsername == null || dynamicUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, "User session not ready. Please log in again.", "Join Club", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                joinClubController.join(dynamicUsername, club.getId());
                // Optionally trigger a refresh to reflect new membership sooner if presenter latency.
                if (listClubsController != null) {
                    listClubsController.fetch(dynamicUsername);
                }
            } catch (Exception ex) {
                System.out.println("[ClubHome][DEBUG] Join action error: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Failed to join club: " + ex.getMessage(), "Join Club", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Remove tooltip that showed full text to avoid confusion
        joinButton.setToolTipText(null);
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
            ClubState state = clubViewModel.getState();
            System.out.println("[ClubHome][DEBUG] State update: memberClubs=" + state.getMemberClubs().size() + " nonMemberClubs=" + state.getNonMemberClubs().size() + " announcements=" + state.getAnnouncements().size());
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

    // Allow other views (e.g., CreateNewPostView) to trigger a clubs refresh after posting
    public void reloadClubs() {
        refresh();
    }

    // Simple HTML escape for label content
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
