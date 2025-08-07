package view;

import java.awt.Component;
import java.awt.Dimension;
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
import interface_adapter.manage_following.ManageFollowingViewModel;
import interface_adapter.view_profile.ProfileController;
import view.ui_components.UserInfoPanel;

public class ManageFollowersView extends JPanel implements PropertyChangeListener {
    private final String viewName = "manage followers";
    private final ManageFollowersViewModel manageFollowersViewModel;
    private ManageFollowersController manageFollowersController;
    private ProfileController profileController;

    private final JLabel title;

    private final JPanel mainPanel;
    private final JPanel followersPanel;
    private final JPanel requestsPanel;
    private final ArrayList<UserInfoPanel> followersPanels;
    private final ArrayList<UserInfoPanel> requestersPanels;
    private final ArrayList<JButton> acceptFollowRequestButtons;
    private final JScrollPane followersScrollPane;
    private final JScrollPane requestsScrollPane;

    private final JButton backButton;

    public ManageFollowersView(ManageFollowersViewModel manageFollowersViewModel) {
        this.manageFollowersViewModel = manageFollowersViewModel;
        this.manageFollowersViewModel.addPropertyChangeListener(this);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel(ManageFollowersViewModel.TITLE_LABEL);
        mainPanel = new JPanel();
        followersPanel = new JPanel();
        requestsPanel = new JPanel();
        followersPanels = new ArrayList<>();
        requestersPanels = new ArrayList<>();
        followersScrollPane = new JScrollPane(followersPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        requestsScrollPane = new JScrollPane(requestsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        acceptFollowRequestButtons = new ArrayList<>();
        backButton = new JButton(ManageFollowersViewModel.BACK_LABEL);

        createUiComponents();
    }

    private void createUiComponents() {

        // add title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        title.setForeground(GUIConstants.RED);
        this.add(title);

        // add main panel as scroll pane
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        followersPanel.setLayout(new BoxLayout(followersPanel, BoxLayout.Y_AXIS));
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));

        final JLabel followersLabel = new JLabel("Followers");
        mainPanel.add(followersLabel);
        mainPanel.add(followersScrollPane);
        final JLabel requestsLabel = new JLabel("Follow Requests");
        mainPanel.add(requestsLabel);
        mainPanel.add(requestsScrollPane);

        this.add(mainPanel);

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
        this.requestersPanels.clear();
        this.acceptFollowRequestButtons.clear();
        followersPanel.removeAll();
        requestsPanel.removeAll();
        final ArrayList<User> followers = state.getFollowers();
        for (User account : followers) {
            final JButton removeButton = new JButton(ManageFollowersViewModel.REMOVE_FOLLOW_LABEL);
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
            followersPanel.add(followerPanel);
        }
        final ArrayList<User> requesters = state.getRequesters();
        for (User account : requesters) {
            final JButton removeButton = new JButton(ManageFollowersViewModel.REMOVE_REQUEST_LABEL);
            removeButton.addActionListener(
                    event -> {
                        if (event.getSource().equals(removeButton)) {
                            this.manageFollowersController.executeRemoveRequester(
                                    state.getUsername(), account.getUsername());
                        }
                    }
            );
            this.requestersPanels.add(new UserInfoPanel(account.getProfilePictureUrl(), account.getUsername(),
                    account.getDisplayName(), removeButton));
            final JButton acceptButton = new JButton(ManageFollowersViewModel.ACCEPT_REQUEST_LABEL
                    + " from: " + account.getUsername());
            acceptButton.addActionListener(
                    event -> {
                        if (event.getSource().equals(acceptButton)) {
                            this.manageFollowersController.executeAcceptRequester(
                                    state.getUsername(), account.getUsername());
                        }
                    }
            );
            this.acceptFollowRequestButtons.add(acceptButton);
        }

        for (int i = 0; i < requestersPanels.size(); i++) {
            requestsPanel.add(requestersPanels.get(i).addButton(acceptFollowRequestButtons.get(i)));
        }

        final Dimension screenSize = new Dimension(ManageFollowingViewModel.PANEL_WIDTH,
                ManageFollowingViewModel.PANEL_HEIGHT);
        followersPanel.setPreferredSize(screenSize);

        followersPanel.revalidate();
        followersPanel.repaint();
        followersScrollPane.revalidate();
        followersScrollPane.repaint();

        requestsPanel.revalidate();
        requestsPanel.repaint();
        requestsScrollPane.revalidate();
        requestsScrollPane.repaint();
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
