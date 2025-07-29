package view;


import interface_adapter.manage_followers.ManageFollowersController;
import interface_adapter.manage_followers.ManageFollowersViewModel;

import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// TODO: use constants for the hardcoded parts
public class ManageFollowersView extends JPanel implements PropertyChangeListener {
    private final String viewName = "manage followers";
    private final ManageFollowersViewModel manageFollowersViewModel;
    private ManageFollowersController manageFollowersController;

    final JLabel title;

    // TODO: make a 'follower' panel thing
    private final ImageIcon profilePicture;
    private final JLabel profilePictureLabel;
    private final JLabel displayName;
    private final JLabel username;
    private final JButton removeFollowerButton;

    private final JButton backButton;


    public ManageFollowersView(ManageFollowersViewModel manageFollowersViewModel) {
        this.manageFollowersViewModel = manageFollowersViewModel;
        this.manageFollowersViewModel.addPropertyChangeListener(this);

        title = new JLabel(manageFollowersViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMinimumSize(new Dimension(1000, 50));

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setMaximumSize(new Dimension(1200, 300));
        mainPanel.setMinimumSize(new Dimension(1200, 300));

        JScrollPane scrollPane = new JScrollPane(mainPanel);

        final JPanel followerPanel = new JPanel();
        followerPanel.setLayout(new BoxLayout(followerPanel, BoxLayout.X_AXIS));
        followerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        followerPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        followerPanel.setMaximumSize(new Dimension(1200, 200));
        followerPanel.setMinimumSize(new Dimension(1200, 200));

        Image profilePictureImage =  new ImageIcon("src/main/java/view/temporary_sample_image.png").getImage();
        int newWidth = 200;
        int newHeight = 200;
        profilePictureImage = profilePictureImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        profilePicture = new ImageIcon(profilePictureImage);

        profilePictureLabel = new JLabel(profilePicture);
        profilePictureLabel.setMaximumSize(new Dimension(200, 200));
        profilePictureLabel.setMinimumSize(new Dimension(200, 200));
        profilePictureLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        followerPanel.add(profilePictureLabel);

        followerPanel.add(Box.createRigidArea(new Dimension(50, 200)));

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

        followerPanel.add(userInfoPanel);

        removeFollowerButton = new JButton("Remove Follower");
        removeFollowerButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        removeFollowerButton.setAlignmentY(Component.TOP_ALIGNMENT);
        followerPanel.add(removeFollowerButton);

        mainPanel.add(followerPanel);

        backButton = new JButton("Back");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // TODO: write code for the action listeners for the buttons
        removeFollowerButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(removeFollowerButton)) {
                    }
                }
        );

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                        this.manageFollowersController.switchToProfileView();
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

    public void setManageFollowersController(ManageFollowersController controller) {
        this.manageFollowersController = controller;
    }

}
