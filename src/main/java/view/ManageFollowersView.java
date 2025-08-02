package view;


import entity.User;
import interface_adapter.manage_followers.ManageFollowersController;
import interface_adapter.manage_followers.ManageFollowersState;
import interface_adapter.manage_followers.ManageFollowersViewModel;
import interface_adapter.profile.ProfileController;
import view.ui_components.UserInfoPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class ManageFollowersView extends JPanel implements PropertyChangeListener {
    private final String viewName = "manage followers";
    private final ManageFollowersViewModel manageFollowersViewModel;
    private ManageFollowersController manageFollowersController;
    private ProfileController profileController;

    final JLabel title;

    private final JPanel mainPanel;
    private final ArrayList<UserInfoPanel> followersPanels;

    private final JButton backButton;

    public ManageFollowersView(ManageFollowersViewModel manageFollowersViewModel) {
        this.manageFollowersViewModel = manageFollowersViewModel;
        this.manageFollowersViewModel.addPropertyChangeListener(this);

        title = new JLabel(ManageFollowersViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        title.setForeground(GUIConstants.RED);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(mainPanel);

        this.followersPanels = new ArrayList<>();

        backButton = new JButton("Back to Profile");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                        final ManageFollowersState currentState = manageFollowersViewModel.getState();
                        this.profileController.executeViewProfile(currentState.getUsername());
                        this.manageFollowersController.switchToProfileView();
                    }
                }
        );

        this.add(title);
        this.add(scrollPane);
        this.add(backButton);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            this.followersPanels.clear();
            mainPanel.removeAll();
            final ArrayList<User> followers = this.manageFollowersViewModel.getState().getFollowers();
            for (User account : followers) {
                final JButton removeButton = new JButton(ManageFollowersViewModel.REMOVE_LABEL);
                removeButton.addActionListener(
                        event -> {
                            if (event.getSource().equals(removeButton)) {
                                this.manageFollowersController.executeRemoveFollower(
                                        this.manageFollowersViewModel.getState().getUsername(), account.getUsername());
                            }
                        }
                );
                this.followersPanels.add(new UserInfoPanel(account.getProfilePictureUrl(), account.getUsername(),
                        account.getDisplayName(), removeButton));
            }

            for (UserInfoPanel followerPanel : followersPanels) {
                mainPanel.add(followerPanel);
            }
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setManageFollowersController(ManageFollowersController controller) {
        this.manageFollowersController = controller;
    }
    public void setProfileController(ProfileController controller) {
        this.profileController = controller;
    }

}
