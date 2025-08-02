package view;

import entity.User;
import interface_adapter.manage_followers.ManageFollowersViewModel;
import interface_adapter.manage_following.ManageFollowingController;
import interface_adapter.manage_following.ManageFollowingState;
import interface_adapter.manage_following.ManageFollowingViewModel;
import interface_adapter.profile.ProfileController;
import view.ui_components.UserInfoPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ManageFollowingView extends JPanel implements PropertyChangeListener {
    private final String viewName = "manage following";
    private final ManageFollowingViewModel manageFollowingViewModel;
    private ManageFollowingController manageFollowingController;
    private ProfileController profileController;

    final JLabel title;

    private final JPanel mainPanel;
    private final ArrayList<UserInfoPanel> followingPanels;

    private final JButton backButton;


    public ManageFollowingView(ManageFollowingViewModel manageFollowingViewModel) {
        this.manageFollowingViewModel = manageFollowingViewModel;
        this.manageFollowingViewModel.addPropertyChangeListener(this);

        title = new JLabel(ManageFollowingViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        title.setForeground(GUIConstants.RED);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(mainPanel);

        this.followingPanels = new ArrayList<>();

        backButton = new JButton("Back to Profile");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                        final ManageFollowingState currentState = manageFollowingViewModel.getState();
                        this.profileController.executeViewProfile(currentState.getUsername());
                        this.manageFollowingController.switchToProfileView();
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
            this.followingPanels.clear();
            mainPanel.removeAll();
            final ArrayList<User> following = this.manageFollowingViewModel.getState().getFollowing();
            for (User account : following) {
                final JButton removeButton = new JButton(ManageFollowingViewModel.REMOVE_LABEL);
                removeButton.addActionListener(
                        event -> {
                            if (event.getSource().equals(removeButton)) {
                                this.manageFollowingController.executeUnfollow(
                                        this.manageFollowingViewModel.getState().getUsername(), account.getUsername());
                            }
                        }
                );
                this.followingPanels.add(new UserInfoPanel(account.getProfilePictureUrl(), account.getUsername(),
                        account.getDisplayName(), removeButton));
            }

            for (UserInfoPanel followingPanel : followingPanels) {
                mainPanel.add(followingPanel);
            }
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setManageFollowingController(ManageFollowingController controller) {
        this.manageFollowingController = controller;
    }
    public void setProfileController(ProfileController controller) {
        this.profileController = controller;
    }

}
