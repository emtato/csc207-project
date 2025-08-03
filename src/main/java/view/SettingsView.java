package view;


import interface_adapter.ViewManagerModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;
import view.ui_components.GeneralJLabel;
import view.ui_components.MenuBarPanel;

import javax.swing.*;
import javax.swing.JLabel;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SettingsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "settings";
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    private SettingsController settingsController;
    private LogoutController logoutController;

    private final JToggleButton accountPrivacyToggle;
    private final JToggleButton notificationsToggle;

    public SettingsView(SettingsViewModel settingsViewModel, ViewManagerModel viewManagerModel) {
        this.settingsViewModel = settingsViewModel;
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel.addPropertyChangeListener(this);

        final JLabel title = new GeneralJLabel(SettingsViewModel.TITLE_LABEL,
                GUIConstants.TITLE_SIZE, GUIConstants.RED);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel settingsPanel = new JPanel();
        final Dimension settingsPanelDimension = new Dimension(SettingsViewModel.SETTINGS_PANEL_WIDTH,
                SettingsViewModel.SETTINGS_PANEL_HEIGHT);
        settingsPanel.setMaximumSize(settingsPanelDimension);
        settingsPanel.setMinimumSize(settingsPanelDimension);
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPanel.setBackground(GUIConstants.RED);

        final JLabel privacyHeading = new GeneralJLabel(SettingsViewModel.PRIVACY_HEADING,
                GUIConstants.HEADER_SIZE, GUIConstants.WHITE);
        settingsPanel.add(privacyHeading);

        final JPanel privacyPanel = new JPanel();
        privacyPanel.setLayout(new BoxLayout(privacyPanel, BoxLayout.X_AXIS));
        privacyPanel.setBackground(GUIConstants.PINK);
        final JLabel privacyLabel = new JLabel(SettingsViewModel.ACCOUNT_PRIVACY_LABEL);
        privacyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        privacyLabel.setFont(GUIConstants.FONT_TEXT);
        privacyLabel.setForeground(GUIConstants.RED);
        privacyPanel.add(privacyLabel);
        privacyPanel.add(Box.createHorizontalGlue());
        accountPrivacyToggle = new JToggleButton(SettingsViewModel.ACCOUNT_PRIVACY_TOGGLE_ON);
        accountPrivacyToggle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        privacyPanel.add(accountPrivacyToggle);

        settingsPanel.add(privacyPanel);

        final JLabel notificationsHeading = new GeneralJLabel(SettingsViewModel.NOTIFICATIONS_HEADING,
                GUIConstants.HEADER_SIZE, GUIConstants.WHITE);
        settingsPanel.add(notificationsHeading);

        final JPanel notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.X_AXIS));
        notificationsPanel.setBackground(GUIConstants.PINK);
        final JLabel notificationsLabel = new JLabel(SettingsViewModel.NOTIFICATIONS_LABEL);
        notificationsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        notificationsLabel.setFont(GUIConstants.FONT_TEXT);
        notificationsLabel.setForeground(GUIConstants.RED);
        notificationsPanel.add(notificationsLabel);
        notificationsPanel.add(Box.createHorizontalGlue());
        notificationsToggle = new JToggleButton(SettingsViewModel.NOTIFICATIONS_TOGGLE_ON);
        notificationsToggle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        notificationsPanel.add(notificationsToggle);

        settingsPanel.add(notificationsPanel);

        final JLabel logoutHeading = new GeneralJLabel(SettingsViewModel.LOGOUT_HEADING,
                GUIConstants.HEADER_SIZE, GUIConstants.WHITE);
        settingsPanel.add(logoutHeading);

        final JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.X_AXIS));
        logoutPanel.setBackground(GUIConstants.PINK);
        final JLabel logoutLabel = new JLabel(SettingsViewModel.LOGOUT_LABEL);
        logoutLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutLabel.setFont(GUIConstants.FONT_TEXT);
        logoutLabel.setForeground(GUIConstants.RED);
        logoutPanel.add(logoutLabel);
        logoutPanel.add(Box.createHorizontalGlue());
        final JButton logoutButton = new JButton(SettingsViewModel.LOGOUT_BUTTON_LABEL);
        logoutButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        logoutPanel.add(logoutButton);

        settingsPanel.add(logoutPanel);

        settingsPanel.add(Box.createVerticalGlue());

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        accountPrivacyToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (accountPrivacyToggle.isSelected()) {
                    accountPrivacyToggle.setText(SettingsViewModel.ACCOUNT_PRIVACY_TOGGLE_ON);
                } else{
                    accountPrivacyToggle.setText(SettingsViewModel.ACCOUNT_PRIVACY_TOGGLE_OFF);
                }
                final SettingsState settingsState = settingsViewModel.getState();
                settingsController.executePrivacyToggle(settingsState.getUsername(), accountPrivacyToggle.isSelected());
            }
        });

        notificationsToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (notificationsToggle.isSelected()) {
                    notificationsToggle.setText(SettingsViewModel.NOTIFICATIONS_TOGGLE_ON);
                } else{
                    notificationsToggle.setText(SettingsViewModel.NOTIFICATIONS_TOGGLE_OFF);
                }
                final SettingsState settingsState = settingsViewModel.getState();
                settingsController.executeNotificationsToggle(settingsState.getUsername(),
                        notificationsToggle.isSelected());
            }
        });

        logoutButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(logoutButton)) {
                        final SettingsState settingsState = settingsViewModel.getState();
                        this.logoutController.execute(settingsState.getUsername());
                    }
                }
        );

        this.add(title);
        this.add(settingsPanel);
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        this.add(menuBar);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("privacy changed")) {
            SettingsState state = this.settingsViewModel.getState();
            this.accountPrivacyToggle.setSelected(state.isPublic());
            if (state.isPublic()) {
                this.accountPrivacyToggle.setText(SettingsViewModel.ACCOUNT_PRIVACY_TOGGLE_ON);
            }
            else {
                this.accountPrivacyToggle.setText(SettingsViewModel.ACCOUNT_PRIVACY_TOGGLE_OFF);
            }
        }
        else if (evt.getPropertyName().equals("notifications changed")) {
            SettingsState state = this.settingsViewModel.getState();
            this.notificationsToggle.setSelected(state.isNotificationsEnabled());
            if (state.isNotificationsEnabled()) {
                this.notificationsToggle.setText(SettingsViewModel.NOTIFICATIONS_TOGGLE_ON);
            }
            else {
                this.notificationsToggle.setText(SettingsViewModel.NOTIFICATIONS_TOGGLE_OFF);
            }
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSettingsController(SettingsController controller) {
        this.settingsController = controller;
    }
    public void setLogoutController(LogoutController controller) {
        this.logoutController = controller;
    }

}
