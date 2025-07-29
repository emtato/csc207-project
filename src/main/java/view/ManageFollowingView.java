package view;


import interface_adapter.manage_following.ManageFollowingViewModel;

import javax.swing.*;
import javax.swing.JLabel;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// TODO: use constants for the hardcoded parts
public class ManageFollowingView extends JPanel implements PropertyChangeListener {
    private final String viewName = "manage following";
    private final ManageFollowingViewModel manageFollowingViewModel;
    // TODO: declare controllers

    final JLabel title;

    // TODO: make a 'follower' panel thing
    private final ImageIcon profilePicture;
    private final JLabel profilePictureLabel;
    private final JLabel displayName;
    private final JLabel username;
    private final JButton unfollowButton;

    private final JButton backButton;


    public ManageFollowingView(ManageFollowingViewModel manageFollowingViewModel) {
        this.manageFollowingViewModel = manageFollowingViewModel;
        this.manageFollowingViewModel.addPropertyChangeListener(this);

        title = new JLabel(manageFollowingViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMinimumSize(new Dimension(1000, 50));

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setMaximumSize(new Dimension(1200, 300));
        mainPanel.setMinimumSize(new Dimension(1200, 300));

        JScrollPane scrollPane = new JScrollPane(mainPanel);

        final JPanel followingPanel = new JPanel();
        followingPanel.setLayout(new BoxLayout(followingPanel, BoxLayout.X_AXIS));
        followingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        followingPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        followingPanel.setMaximumSize(new Dimension(1200, 200));
        followingPanel.setMinimumSize(new Dimension(1200, 200));

        Image profilePictureImage =  new ImageIcon("src/main/java/view/temporary_sample_image.png").getImage();
        int newWidth = 200;
        int newHeight = 200;
        profilePictureImage = profilePictureImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        profilePicture = new ImageIcon(profilePictureImage);

        profilePictureLabel = new JLabel(profilePicture);
        profilePictureLabel.setMaximumSize(new Dimension(200, 200));
        profilePictureLabel.setMinimumSize(new Dimension(200, 200));
        profilePictureLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        followingPanel.add(profilePictureLabel);

        followingPanel.add(Box.createRigidArea(new Dimension(50, 200)));

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

        followingPanel.add(userInfoPanel);

        unfollowButton = new JButton("Unfollow");
        unfollowButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        unfollowButton.setAlignmentY(Component.TOP_ALIGNMENT);
        followingPanel.add(unfollowButton);

        mainPanel.add(followingPanel);

        backButton = new JButton("Back");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // TODO: write code for the action listeners for the buttons
        unfollowButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(unfollowButton)) {
                    }
                }
        );

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                    }
                }
        );

        this.add(title);
        this.add(scrollPane);
        this.add(backButton);
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
