package view;


import interface_adapter.profile.ProfileViewModel;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// TODO: use constants for the hardcoded parts
public class ProfileView extends JPanel implements PropertyChangeListener {
    private final String viewName = "profile";
    private final ProfileViewModel profileViewModel;
    // TODO: declare controllers

    final JLabel title;
    private final ImageIcon profilePicture;
    private final JLabel profilePictureLabel;
    private final JLabel displayName;
    private final JLabel username;
    private final JTextArea bio;
    private final JButton editProfileButton;
    private final JLabel following;
    private final JButton followingButton;
    private final JLabel followers;
    private final JButton followersButton;
    private final JTextArea profileContent;

    // TODO: reuse code for duplicate buttons
    private final JButton homeButton;
    private final JButton mapButton;
    private final JButton notificationsButton;
    private final JButton settingsButton;
    private final JButton profileButton;


    public ProfileView(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
        this.profileViewModel.addPropertyChangeListener(this);

        title = new JLabel(ProfileViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMinimumSize(new Dimension(1000, 50));

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setMaximumSize(new Dimension(1200, 300));
        mainPanel.setMinimumSize(new Dimension(1200, 300));

        Image profilePictureImage =  new ImageIcon("src/main/java/view/temporary_sample_image.png").getImage();
        int newWidth = 200;
        int newHeight = 200;
        profilePictureImage = profilePictureImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        profilePicture = new ImageIcon(profilePictureImage);

        profilePictureLabel = new JLabel(profilePicture);
        profilePictureLabel.setMaximumSize(new Dimension(200, 200));
        profilePictureLabel.setMinimumSize(new Dimension(200, 200));
        profilePictureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(profilePictureLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(50, 200)));

        final JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setMaximumSize(new Dimension(500, 200));
        userInfoPanel.setMinimumSize(new Dimension(500, 200));
        userInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        displayName = new JLabel("Display Name");
        displayName.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfoPanel.add(displayName);

        username = new JLabel("@Username");
        username.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfoPanel.add(username);

        bio = new JTextArea(ProfileViewModel.BIO_ROW_NUM,ProfileViewModel.BIO_COL_NUM);
        bio.setText("Bio.\n1234567890123456789012345678901234567890");
        bio.setEditable(false);
        bio.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfoPanel.add(bio);
        mainPanel.add(userInfoPanel);


        final JPanel profileButtons = new JPanel();
        profileButtons.setLayout(new BoxLayout(profileButtons, BoxLayout.Y_AXIS));
        profileButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileButtons.setMaximumSize(new Dimension(500, 200));
        profileButtons.setMinimumSize(new Dimension(500, 200));


        editProfileButton = new JButton(ProfileViewModel.EDIT_PROFILE_BUTTON_LABEL);
        editProfileButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        profileButtons.add(editProfileButton);

        final JPanel followingPanel = new JPanel();
        followingPanel.setLayout(new BoxLayout(followingPanel, BoxLayout.X_AXIS));
        followingPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        following = new JLabel("10 Following");
        followingButton = new JButton(ProfileViewModel.FOLLOWING_BUTTON_LABEL);
        followingPanel.add(following);
        followingPanel.add(followingButton);
        profileButtons.add(followingPanel);

        final JPanel followersPanel = new JPanel();
        followersPanel.setLayout(new BoxLayout(followersPanel, BoxLayout.X_AXIS));
        followersPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        followers = new JLabel("10 Followers");
        followersButton = new JButton(ProfileViewModel.FOLLOWERS_BUTTON_LABEL);
        followersPanel.add(followers);
        followersPanel.add(followersButton);
        profileButtons.add(followersPanel);

        mainPanel.add(profileButtons);


        final JPanel profileContentPanel = new JPanel();
        profileContentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileContentPanel.setMaximumSize(new Dimension(1200, 250));
        profileContentPanel.setMinimumSize(new Dimension(1200, 250));

        profileContent = new JTextArea(ProfileViewModel.CONTENT_ROW_NUM,ProfileViewModel.CONTENT_COL_NUM);
        profileContent.setText("Posts go here\nmore posts");
        profileContentPanel.add(profileContent);


        final JPanel generalButtons = new JPanel();
        generalButtons.setAlignmentY(Component.CENTER_ALIGNMENT);

        homeButton = new JButton("Home");
        generalButtons.add(homeButton);

        mapButton = new JButton("Map");
        generalButtons.add(mapButton);

        notificationsButton = new JButton("Notifications");
        generalButtons.add(notificationsButton);

        settingsButton = new JButton("Settings");
        generalButtons.add(settingsButton);

        profileButton = new JButton("Profile");
        generalButtons.add(profileButton);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // TODO: write code for the action listeners for the buttons
        editProfileButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(editProfileButton)) {
                    }
                }
        );

        followersButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(followersButton)) {
                    }
                }
        );

        followingButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(followingButton)) {
                    }
                }
        );

        this.add(title);
        this.add(mainPanel);
        this.add(profileContentPanel);
        this.add(generalButtons);
    }

    // TODO: implement the propertyChange function and set controllers
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {

        }
    }

    public String getViewName() {
        return viewName;
    }
    
    // set controllers
    
}