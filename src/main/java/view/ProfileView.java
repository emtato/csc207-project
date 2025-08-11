package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import entity.Post;
import interface_adapter.ViewManagerModel;
import interface_adapter.like_post.LikePostController;
import interface_adapter.view_profile.ProfileController;
import interface_adapter.view_profile.ProfileState;
import interface_adapter.view_profile.ProfileViewModel;
import view.ui_components.GeneralJLabel;
import view.ui_components.LabelButtonPanel;
import view.ui_components.MenuBarPanel;
import view.ui_components.PostPanel;
import view.ui_components.ProfilePictureLabel;

public class ProfileView extends JPanel implements PropertyChangeListener {
    private static final int SCROLL_BAR_INCREMENNT = 16;
    private final String viewName = "profile";
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    private ProfileController profileController;
    private LikePostController likePostController;

    private final ProfilePictureLabel profilePicture;
    private final JLabel displayName;
    private final JLabel username;
    private final JTextArea bio;
    private final JButton editProfileButton;
    private final JLabel following;
    private final JButton followingButton;
    private final JLabel followers;
    private final JButton followersButton;
    private final JPanel profileContent;
    private final JButton refreshButton;
    private final JButton seeMyProfileButton;
    private final JButton seeOtherProfileButton;
    private final JPanel profileButtonsPanel;

    public ProfileView(ProfileViewModel profileViewModel, ViewManagerModel viewManagerModel) {
        this.profileViewModel = profileViewModel;
        this.viewManagerModel = viewManagerModel;
        this.profileViewModel.addPropertyChangeListener(this);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add title
        final JLabel title = createTitle();
        this.add(title);

        profilePicture = new ProfilePictureLabel(this.profileViewModel.getState().getProfilePictureUrl(),
                ProfileViewModel.PFP_WIDTH, ProfileViewModel.PFP_HEIGHT);
        profilePicture.setAlignmentX(Component.LEFT_ALIGNMENT);
        displayName = new GeneralJLabel(this.profileViewModel.getState().getDisplayName(), GUIConstants.TEXT_SIZE,
                GUIConstants.RED);
        username = new GeneralJLabel(this.profileViewModel.getState().getUsername(), GUIConstants.SMALL_TEXT_SIZE,
                GUIConstants.RED);
        bio = createBioTextArea();
        editProfileButton = new JButton(ProfileViewModel.EDIT_PROFILE_BUTTON_LABEL);
        editProfileButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        following = new JLabel(this.profileViewModel.getState().getNumFollowing() + " Following");
        followingButton = new JButton(ProfileViewModel.FOLLOWING_BUTTON_LABEL);
        followers = new JLabel(this.profileViewModel.getState().getNumFollowers() + " Followers");
        followersButton = new JButton(ProfileViewModel.FOLLOWERS_BUTTON_LABEL);
        profileContent = new JPanel();
        profileContent.setLayout(new BoxLayout(profileContent, BoxLayout.Y_AXIS));
        refreshButton = new JButton("Refresh");
        seeMyProfileButton = new JButton(ProfileViewModel.SEE_MY_PROFILE_BUTTON_LABEL);
        seeOtherProfileButton = new JButton(ProfileViewModel.SEE_OTHER_PROFILE_BUTTON_LABEL);
        profileButtonsPanel = new JPanel();

        createView();

        // add listeners
        addRefreshButtonListener();
        addEditProfileButtonListener();
        addFollowersButtonListener();
        addFollowingButtonListener();
        addOtherProfileButtonListener();
        addMyProfileButtonListener();
    }

    private void createView() {
        // add main panel containting user information and buttons to go to other profile related views
        final JPanel mainPanel = createMainPanel();
        mainPanel.add(profilePicture);
        mainPanel.add(Box.createRigidArea(new Dimension(GUIConstants.COMPONENT_GAP_SIZE, ProfileViewModel.PFP_HEIGHT)));

        final JPanel userInfoPanel = createUserInfoPanel();
        userInfoPanel.add(displayName);
        userInfoPanel.add(username);
        userInfoPanel.add(bio);

        mainPanel.add(userInfoPanel);

        profileButtonsPanel.setLayout(new BoxLayout(profileButtonsPanel, BoxLayout.Y_AXIS));
        final LabelButtonPanel editButtonPanel = new LabelButtonPanel(new JLabel(), editProfileButton);
        profileButtonsPanel.add(editButtonPanel);

        final LabelButtonPanel followingButtonPanel = new LabelButtonPanel(following, followingButton);
        profileButtonsPanel.add(followingButtonPanel);

        final LabelButtonPanel followersButtonPanel = new LabelButtonPanel(followers, followersButton);
        profileButtonsPanel.add(followersButtonPanel);

        final LabelButtonPanel seeOtherProfileButtonPanel = new LabelButtonPanel(new JLabel(), seeOtherProfileButton);
        profileButtonsPanel.add(seeOtherProfileButtonPanel);

        final JScrollPane buttonsPanelScrollPane = new JScrollPane(profileButtonsPanel);

        mainPanel.add(buttonsPanelScrollPane);
        this.add(mainPanel);

        // add profile content scroll panel containing the user's posts
        final JScrollPane profileContentPanel = createProfileContentScrollPane();
        profileContentPanel.getVerticalScrollBar().setUnitIncrement(SCROLL_BAR_INCREMENNT);

        this.add(profileContentPanel);

        // add a refresh button that executes the view profile use case on press (refreshes the profile view)
        this.add(refreshButton);

        // add a menu bar
        final MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        this.add(menuBar);
    }

    private JLabel createTitle() {
        final JLabel title = new JLabel(ProfileViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        title.setForeground(Color.RED);
        return title;
    }

    private JPanel createMainPanel() {
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Dimension size = new Dimension(ProfileViewModel.MAIN_PANEL_WIDTH, ProfileViewModel.MAIN_PANEL_HEIGHT);
        mainPanel.setMaximumSize(size);
        mainPanel.setMinimumSize(size);
        return mainPanel;
    }

    private JPanel createUserInfoPanel() {
        final JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfoPanel.setBackground(Color.WHITE);
        final Dimension size = new Dimension(ProfileViewModel.INFO_PANEL_WIDTH, ProfileViewModel.INFO_PANEL_HEIGHT);
        userInfoPanel.setMaximumSize(size);
        return userInfoPanel;
    }

    private JTextArea createBioTextArea() {
        final JTextArea bioArea = new JTextArea(ProfileViewModel.BIO_ROW_NUM, ProfileViewModel.BIO_COL_NUM);
        bioArea.setText(this.profileViewModel.getState().getBio());
        bioArea.setFont(GUIConstants.FONT_TEXT);
        bioArea.setEditable(false);
        bioArea.setBackground(Color.PINK);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        return bioArea;
    }

    private JScrollPane createProfileContentScrollPane() {
        final JScrollPane profileContentPanel = new JScrollPane(profileContent);
        profileContentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Dimension contentDimension = new Dimension(ProfileViewModel.CONTENT_PANEL_WIDTH,
                ProfileViewModel.CONTENT_PANEL_HEIGHT);
        profileContentPanel.setMaximumSize(contentDimension);
        profileContentPanel.setMinimumSize(contentDimension);
        profileContentPanel.setBackground(Color.WHITE);
        refreshContent();
        return profileContentPanel;
    }

    private void addRefreshButtonListener() {
        refreshButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(refreshButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        this.profileController.executeViewProfile(profileState.getCurrentUsername(),
                                profileState.getUsername());
                    }
                }
        );
    }

    private void addEditProfileButtonListener() {
        editProfileButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(editProfileButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        this.profileController.switchToEditProfileView(profileState.getUsername());
                    }
                }
        );
    }

    private void addFollowersButtonListener() {
        followersButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(followersButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        this.profileController.switchToManageFollowersView(profileState.getUsername());
                    }
                }
        );
    }

    private void addFollowingButtonListener() {
        followingButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(followingButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        this.profileController.switchToManageFollowingView(profileState.getUsername());
                    }
                }
        );
    }

    private void addMyProfileButtonListener() {
        seeMyProfileButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(seeMyProfileButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        this.profileController.executeViewProfile(profileState.getCurrentUsername(),
                                profileState.getCurrentUsername());
                    }
                }
        );
    }

    private void addOtherProfileButtonListener() {
        seeOtherProfileButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(seeOtherProfileButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        final String targetUsername = JOptionPane.showInputDialog(this, "Enter target username:");
                        if (targetUsername != null && !targetUsername.isEmpty()) {
                            this.profileController.executeViewProfile(profileState.getCurrentUsername(),
                                    targetUsername);
                        }
                    }
                }
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ProfileState profileState = this.profileViewModel.getState();
        if (evt.getPropertyName().equals("My Profile Viewed") || evt.getPropertyName().equals("Other Profile Viewed")) {
            setFields(profileState, evt.getPropertyName());
        }
        else if (evt.getPropertyName().equals("User not found")) {
            final String targetUsername = JOptionPane.showInputDialog(this,
                    "User not found, enter a different username: ");
            if (targetUsername != null && !targetUsername.isEmpty()) {
                this.profileController.executeViewProfile(profileState.getCurrentUsername(),
                        targetUsername);
            }
        }
        else if (evt.getPropertyName().equals("Account is private")) {
            JOptionPane.showMessageDialog(this, "Account is private", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFields(ProfileState state, String propertyName) {
        profilePicture.updateIcon(state.getProfilePictureUrl());
        displayName.setText(state.getDisplayName());
        username.setText(state.getUsername());
        bio.setText(state.getBio());
        following.setText(state.getNumFollowing() + " following");
        followers.setText(state.getNumFollowers() + " followers");

        profileButtonsPanel.removeAll();
        if ("My Profile Viewed".equals(propertyName)) {
            final LabelButtonPanel editButtonPanel = new LabelButtonPanel(new JLabel(), editProfileButton);
            profileButtonsPanel.add(editButtonPanel);

            final LabelButtonPanel followingButtonPanel = new LabelButtonPanel(following, followingButton);
            profileButtonsPanel.add(followingButtonPanel);

            final LabelButtonPanel followersButtonPanel = new LabelButtonPanel(followers, followersButton);
            profileButtonsPanel.add(followersButtonPanel);

            profileButtonsPanel.add(new LabelButtonPanel(new JLabel(), seeOtherProfileButton));
        }
        else if ("Other Profile Viewed".equals(propertyName)) {
            final JLabel currentStatusLabel = new GeneralJLabel(
                    "You are viewing " + state.getDisplayName() + "'s profile", GUIConstants.SMALL_TEXT_SIZE,
                    GUIConstants.RED);
            final JLabel numFollowingLabel = new GeneralJLabel(state.getNumFollowing() + " Following",
                    GUIConstants.SMALL_TEXT_SIZE, GUIConstants.BLACK);
            final JLabel numFollowersLabel = new GeneralJLabel(state.getNumFollowers() + " Followers",
                    GUIConstants.SMALL_TEXT_SIZE, GUIConstants.BLACK);

            profileButtonsPanel.add(currentStatusLabel);
            profileButtonsPanel.add(numFollowingLabel);
            profileButtonsPanel.add(numFollowersLabel);

            profileButtonsPanel.add(new LabelButtonPanel(new JLabel(), seeMyProfileButton));
            profileButtonsPanel.setBackground(GUIConstants.WHITE);
        }
        refreshContent();
    }

    private void refreshContent() {
        profileContent.removeAll();
        final Map<Long, Post> posts = this.profileViewModel.getState().getPosts();
        if (!posts.isEmpty()) {
            for (Long id : posts.keySet()) {
                final Post post = posts.get(id);
                if (post != null) {
                    final PostPanel postPanel = new PostPanel(viewManagerModel, post, ProfileViewModel.POST_WIDTH,
                            ProfileViewModel.POST_HEIGHT, likePostController);
                    profileContent.add(postPanel);
                }
            }
        }
        this.revalidate();
        this.repaint();
    }

    public String getViewName() {
        return viewName;
    }

    public void setProfileController(ProfileController controller) {
        this.profileController = controller;
    }

    public void setLikePostController(LikePostController controller) {
        this.likePostController = controller;
    }

}
