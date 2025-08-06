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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import entity.User;
import interface_adapter.manage_following.ManageFollowingController;
import interface_adapter.manage_following.ManageFollowingState;
import interface_adapter.manage_following.ManageFollowingViewModel;
import interface_adapter.view_profile.ProfileController;
import view.ui_components.UserInfoPanel;

public class ManageFollowingView extends JPanel implements PropertyChangeListener {
    private final String viewName = "manage following";
    private final ManageFollowingViewModel manageFollowingViewModel;
    private ManageFollowingController manageFollowingController;
    private ProfileController profileController;

    final JLabel title;

    private final JPanel followingPanel;
    private final JPanel requestsPanel;
    private final ArrayList<UserInfoPanel> followingPanels;
    private final ArrayList<UserInfoPanel> requestedPanels;
    private final JTextField followAccountInput;
    private final JPanel scrollPanels;
    private final JScrollPane followingScrollPane;
    private final JScrollPane requestsScrollPane;

    private final JButton backButton;

    public ManageFollowingView(ManageFollowingViewModel manageFollowingViewModel) {
        this.manageFollowingViewModel = manageFollowingViewModel;
        this.manageFollowingViewModel.addPropertyChangeListener(this);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // instantiate variables
        title = new JLabel(ManageFollowingViewModel.TITLE_LABEL);
        followingPanel = new JPanel();
        requestsPanel = new JPanel();
        followingScrollPane = new JScrollPane(followingPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        requestsScrollPane = new JScrollPane(requestsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        followingPanels = new ArrayList<>();
        requestedPanels = new ArrayList<>();
        backButton = new JButton(ManageFollowingViewModel.BACK_BUTTON_LABEL);
        followAccountInput = new JTextField(ManageFollowingViewModel.INPUT_PROMPT);
        scrollPanels = new JPanel();

        // create the view
        createUiComponents();
        refreshScreen();
    }

    private void createUiComponents() {

        // add title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        title.setForeground(GUIConstants.RED);
        this.add(title);

        // add main panel
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scrollPanels.setLayout(new BoxLayout(scrollPanels, BoxLayout.Y_AXIS));
        followingPanel.setLayout(new BoxLayout(followingPanel, BoxLayout.Y_AXIS));
        scrollPanels.add(followingScrollPane);
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
        scrollPanels.add(requestsScrollPane);
        mainPanel.add(scrollPanels);

        final JButton followButton = new JButton(ManageFollowingViewModel.FOLLOW_BUTTON_LABEL);
        mainPanel.add(followAccountInput);
        mainPanel.add(followButton);
        this.add(mainPanel);

        // add back button
        this.add(backButton);

        // add listeners
        followButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(followButton)) {
                        final ManageFollowingState currentState = manageFollowingViewModel.getState();
                        this.manageFollowingController.executeFollow(currentState.getUsername(),
                                currentState.getOtherUsername());
                    }
                }
        );

        followAccountInput.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final ManageFollowingState currentState = manageFollowingViewModel.getState();
                currentState.setOtherUsername(followAccountInput.getText());
                manageFollowingViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                        final ManageFollowingState currentState = manageFollowingViewModel.getState();
                        this.profileController.executeViewProfile(currentState.getUsername());
                        this.manageFollowingController.switchToProfileView();
                    }
                }
        );
    }

    private void refreshScreen() {
        this.followingPanels.clear();
        this.requestedPanels.clear();
        followingPanel.removeAll();
        requestsPanel.removeAll();
        final JLabel followingLabel = new JLabel("Following");
        followingPanel.add(followingLabel);
        final JLabel requestsLabel = new JLabel("Requested To Follow");
        requestsPanel.add(requestsLabel);
        final ArrayList<User> following = this.manageFollowingViewModel.getState().getFollowing();
        for (User account : following) {
            final JButton removeButton = new JButton(ManageFollowingViewModel.REMOVE_FOLLOW_LABEL);
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

        for (UserInfoPanel userPanel : followingPanels) {
            userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            followingPanel.add(userPanel);
        }
        final ArrayList<User> requested = this.manageFollowingViewModel.getState().getRequested();
        for (User account : requested) {
            final JButton removeButton = new JButton(ManageFollowingViewModel.UNREQUEST_LABEL);
            removeButton.addActionListener(
                    event -> {
                        if (event.getSource().equals(removeButton)) {
                            this.manageFollowingController.executeUnfollow(
                                    this.manageFollowingViewModel.getState().getUsername(), account.getUsername());
                        }
                    }
            );
            this.requestedPanels.add(new UserInfoPanel(account.getProfilePictureUrl(), account.getUsername(),
                    account.getDisplayName(), removeButton));
        }

        for (UserInfoPanel userPanel : requestedPanels) {
            userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            requestsPanel.add(userPanel);
        }
        final Dimension screenSize = new Dimension(ManageFollowingViewModel.PANEL_WIDTH,
                ManageFollowingViewModel.PANEL_HEIGHT);
        followAccountInput.setPreferredSize(screenSize);

        followingPanel.revalidate();
        followingPanel.repaint();
        followingScrollPane.revalidate();
        followingScrollPane.repaint();

        requestsPanel.revalidate();
        requestsPanel.repaint();
        requestsScrollPane.revalidate();
        requestsScrollPane.repaint();

        scrollPanels.setPreferredSize(screenSize);
        scrollPanels.revalidate();
        scrollPanels.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            refreshScreen();
            followAccountInput.setText(this.manageFollowingViewModel.getState().getOtherUsername());
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
