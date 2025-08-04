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
    private final ArrayList<UserInfoPanel> followingPanels;
    private final JTextField followAccountInput;

    private final JButton backButton;


    public ManageFollowingView(ManageFollowingViewModel manageFollowingViewModel) {
        this.manageFollowingViewModel = manageFollowingViewModel;
        this.manageFollowingViewModel.addPropertyChangeListener(this);

        title = new JLabel(ManageFollowingViewModel.TITLE_LABEL);
        followingPanel = new JPanel();
        followingPanels = new ArrayList<>();
        backButton = new JButton("Back to Profile");
        followAccountInput = new JTextField("Enter username to follow");

        createUIComponents();
        refreshScreen();
    }

    private void createUIComponents() {

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(GUIConstants.FONT_TITLE);
        title.setForeground(GUIConstants.RED);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        followingPanel.setLayout(new BoxLayout(followingPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(followingPanel);
        mainPanel.add(scrollPane);

        final JButton followButton = new JButton("Follow");
        mainPanel.add(followAccountInput);
        mainPanel.add(followButton);

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

        this.add(title);
        this.add(mainPanel);
        this.add(backButton);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void refreshScreen() {
        this.followingPanels.clear();
        followingPanel.removeAll();
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

        for (UserInfoPanel userPanel : followingPanels) {
            followingPanel.add(userPanel);
        }
        final Dimension screenSize = new Dimension(ManageFollowingViewModel.PANEL_WIDTH,
                ManageFollowingViewModel.PANEL_HEIGHT);
        followingPanel.setPreferredSize(screenSize);
        followAccountInput.setPreferredSize(screenSize);

        followingPanel.revalidate();
        followingPanel.repaint();
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
