package view;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import entity.User;
import interface_adapter.manage_followers.ManageFollowersController;
import interface_adapter.manage_followers.ManageFollowersState;
import interface_adapter.manage_followers.ManageFollowersViewModel;
import interface_adapter.view_profile.ProfileController;
import view.ui_components.UserInfoPanel;

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
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel(ManageFollowersViewModel.TITLE_LABEL);
        mainPanel = new JPanel();
        followersPanels = new ArrayList<>();
        backButton = new JButton(ManageFollowersViewModel.BACK_LABEL);

        createUIComponents();
    }

    private void createUIComponents() {

        // add title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        title.setForeground(GUIConstants.RED);
        this.add(title);

        // add main panel as scroll pane
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        this.add(scrollPane);

        // add back button and add action listener to the back button
        this.add(backButton);

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                        final ManageFollowersState currentState = manageFollowersViewModel.getState();
                        this.profileController.executeViewProfile(currentState.getUsername());
                        this.manageFollowersController.switchToProfileView();
                    }
                }
        );
    }

    private void refreshScreen(ManageFollowersState state) {
        this.followersPanels.clear();
        mainPanel.removeAll();
        final ArrayList<User> followers = state.getFollowers();
        for (User account : followers) {
            final JButton removeButton = new JButton(ManageFollowersViewModel.REMOVE_LABEL);
            removeButton.addActionListener(
                    event -> {
                        if (event.getSource().equals(removeButton)) {
                            this.manageFollowersController.executeRemoveFollower(
                                    state.getUsername(), account.getUsername());
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            refreshScreen(this.manageFollowersViewModel.getState());
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
