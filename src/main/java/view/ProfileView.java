package view;

import entity.Post;
import interface_adapter.ViewManagerModel;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import view.ui_components.*;

import javax.swing.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

public class ProfileView extends JPanel implements PropertyChangeListener {
    private final String viewName = "profile";
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    private ProfileController profileController;

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


    public ProfileView(ProfileViewModel profileViewModel, ViewManagerModel viewManagerModel) {
        this.profileViewModel = profileViewModel;
        this.viewManagerModel = viewManagerModel;
        this.profileViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(ProfileViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMinimumSize(new Dimension(1000, 50));
        title.setFont(GUIConstants.FONT_TITLE);
        title.setForeground(Color.RED);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Dimension size = new Dimension(ProfileViewModel.MAIN_PANEL_WIDTH, ProfileViewModel.MAIN_PANEL_HEIGHT);
        mainPanel.setMaximumSize(size);
        mainPanel.setMinimumSize(size);

        profilePicture = new ProfilePictureLabel(this.profileViewModel.getState().getProfilePictureUrl(),
                ProfileViewModel.PFP_WIDTH, ProfileViewModel.PFP_HEIGHT);
        profilePicture.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(profilePicture);

        mainPanel.add(Box.createRigidArea(new Dimension(GUIConstants.COMPONENT_GAP_SIZE, ProfileViewModel.PFP_HEIGHT)));

        final JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfoPanel.setBackground(Color.WHITE);

        displayName = new GeneralJLabel(this.profileViewModel.getState().getDisplayName(), GUIConstants.TEXT_SIZE,
                GUIConstants.RED);
        userInfoPanel.add(displayName);

        username = new GeneralJLabel(this.profileViewModel.getState().getUsername(), GUIConstants.SMALL_TEXT_SIZE,
                GUIConstants.RED);
        userInfoPanel.add(username);

        bio = new JTextArea(ProfileViewModel.BIO_ROW_NUM,ProfileViewModel.BIO_COL_NUM);
        bio.setText(this.profileViewModel.getState().getBio());
        bio.setFont(GUIConstants.FONT_TEXT);
        bio.setEditable(false);
        bio.setBackground(Color.PINK);
        bio.setLineWrap(true);
        bio.setWrapStyleWord(true);
        userInfoPanel.add(bio);
        mainPanel.add(userInfoPanel);

        final JPanel profileButtons = new JPanel();
        profileButtons.setLayout(new BoxLayout(profileButtons, BoxLayout.Y_AXIS));
        profileButtons.setAlignmentX(Component.RIGHT_ALIGNMENT);

        editProfileButton = new JButton(ProfileViewModel.EDIT_PROFILE_BUTTON_LABEL);
        editProfileButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        final JLabel label = new JLabel();
        final LabelButtonPanel editButtonPanel = new LabelButtonPanel(label, editProfileButton);
        profileButtons.add(editButtonPanel);

        following = new JLabel(this.profileViewModel.getState().getNumFollowing()+" Following");
        followingButton = new JButton(ProfileViewModel.FOLLOWING_BUTTON_LABEL);
        final LabelButtonPanel followingButtonPanel = new LabelButtonPanel(following, followingButton);
        profileButtons.add(followingButtonPanel);

        followers = new JLabel(this.profileViewModel.getState().getNumFollowers()+" Followers");
        followersButton = new JButton(ProfileViewModel.FOLLOWERS_BUTTON_LABEL);
        final LabelButtonPanel followersButtonPanel = new LabelButtonPanel(followers, followersButton);
        profileButtons.add(followersButtonPanel);

        mainPanel.add(profileButtons);

        final JPanel profileContentPanel = new JPanel();
        profileContentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Dimension contentDimension = new Dimension(ProfileViewModel.CONTENT_PANEL_WIDTH,
                ProfileViewModel.CONTENT_PANEL_HEIGHT);
        profileContentPanel.setMaximumSize(contentDimension);
        profileContentPanel.setMinimumSize(contentDimension);
        profileContentPanel.setBackground(Color.WHITE);

        profileContent = new JPanel();
        refreshContent();
        final JScrollPane contentScrollPane = new JScrollPane(profileContent);
        profileContentPanel.add(contentScrollPane);


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        editProfileButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(editProfileButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        this.profileController.switchToEditProfileView(profileState.getUsername());
                    }
                }
        );

        followersButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(followersButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        this.profileController.switchToManageFollowersView(profileState.getUsername());
                    }
                }
        );

        followingButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(followingButton)) {
                        final ProfileState profileState = profileViewModel.getState();
                        this.profileController.switchToManageFollowingView(profileState.getUsername());
                    }
                }
        );

        this.add(title);
        this.add(mainPanel);
        this.add(profileContentPanel);
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        this.add(menuBar, BorderLayout.SOUTH);
    }

    private void refreshContent(){
        profileContent.removeAll();
        final HashMap<Long, Post> posts = this.profileViewModel.getState().getPosts();
        if (posts.size() > 0) {
            for (Long key : posts.keySet()) {
                final Post post = posts.get(key);
                PostPanel postPanel = new PostPanel(viewManagerModel, post, ProfileViewModel.POST_WIDTH,
                        ProfileViewModel.POST_HEIGHT);
                profileContent.add(postPanel);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            profilePicture.updateIcon(this.profileViewModel.getState().getProfilePictureUrl());
            displayName.setText(profileViewModel.getState().getDisplayName());
            username.setText(profileViewModel.getState().getUsername());
            bio.setText(profileViewModel.getState().getBio());
            following.setText(profileViewModel.getState().getNumFollowing()+" following");
            followers.setText(profileViewModel.getState().getNumFollowers()+" followers");
            refreshContent();
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setProfileController(ProfileController controller) {
        this.profileController = controller;
    }

}
