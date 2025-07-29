package view;


import interface_adapter.ViewManagerModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;


import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// TODO: Account (modify username, email options), Appearance (fontsize + or -, dark mode)

// TODO: use constants for the hardcoded parts
public class SettingsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "settings";
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    // TODO: declare controllers

    final JLabel title;
    private final JLabel header;
    private final JLabel privacyLabel;
    private final JLabel privacyHeading;
    private final JToggleButton accountPrivacyToggle;

    // TODO: reuse code for duplicate buttons
    private final JButton homeButton;
    private final JButton mapButton;
    private final JButton notificationsButton;
    private final JButton settingsButton;
    private final JButton profileButton;

    public SettingsView(SettingsViewModel settingsViewModel, ViewManagerModel viewManagerModel) {
        this.settingsViewModel = settingsViewModel;
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel.addPropertyChangeListener(this);

        title = new JLabel(SettingsViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMinimumSize(new Dimension(1000, 50));

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setMaximumSize(new Dimension(1500, 1030));
        mainPanel.setMinimumSize(new Dimension(1500, 1030));

        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.setMaximumSize(new Dimension(200, 1030));
        leftPanel.setMinimumSize(new Dimension(200, 1030));

        header = new JLabel("Settings");
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(header);

        mainPanel.add(leftPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        final JPanel settingsPanel = new JPanel();
        settingsPanel.setMaximumSize(new Dimension(1200, 1030));
        settingsPanel.setMinimumSize(new Dimension(1200, 1030));
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        privacyHeading = new JLabel(SettingsViewModel.PRIVACY_HEADING);
        settingsPanel.add(privacyHeading);

        final JPanel privacyPanel = new JPanel();
        privacyPanel.setLayout(new BoxLayout(privacyPanel, BoxLayout.X_AXIS));

        privacyLabel = new JLabel(SettingsViewModel.ACCOUNT_PRIVACY_LABEL);
        privacyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        privacyPanel.add(privacyLabel);

        privacyPanel.add(Box.createHorizontalGlue());

        accountPrivacyToggle = new JToggleButton(SettingsViewModel.ACCOUNT_PRIVACY_TOGGLE_ON);
        accountPrivacyToggle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        privacyPanel.add(accountPrivacyToggle);

        mainPanel.add(privacyPanel);

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

        // TODO: write code for the action listeners below

        accountPrivacyToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (accountPrivacyToggle.isSelected()) {
                    System.out.println("Toggle Button is ON");
                } else{
                    System.out.println("Toggle Button is OFF");
                }
            }
        });

        this.add(title);
        this.add(mainPanel);
        this.add(generalButtons);
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        add(menuBar, BorderLayout.NORTH);
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
