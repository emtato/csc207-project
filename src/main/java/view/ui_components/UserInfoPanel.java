package view.ui_components;

import view.GUIConstants;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

public class UserInfoPanel extends JPanel {
    public UserInfoPanel(String profilePictureUrl, String username, String displayName, JButton button) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        final ProfilePictureLabel pfp = new ProfilePictureLabel(profilePictureUrl, GUIConstants.PROFILE_PICTURE_WIDTH,
                GUIConstants.PROFILE_PICTURE_HEIGHT);
        pfp.setAlignmentX(LEFT_ALIGNMENT);
        this.add(pfp);
        final JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        final JLabel usernameLabel = new GeneralJLabel(username, GUIConstants.TEXT_SIZE, GUIConstants.RED);
        final JLabel displayNameLabel =
                new GeneralJLabel(displayName, GUIConstants.SMALL_TEXT_SIZE, GUIConstants.BLACK);
        usernameLabel.setAlignmentX(LEFT_ALIGNMENT);
        displayNameLabel.setAlignmentX(LEFT_ALIGNMENT);
        verticalPanel.add(usernameLabel);
        verticalPanel.add(displayNameLabel);
        this.add(verticalPanel);
        button.setForeground(GUIConstants.RED);
        this.add(button);
    }
}
