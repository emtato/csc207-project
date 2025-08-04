package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.edit_profile.EditProfileState;
import org.jetbrains.annotations.NotNull;

import interface_adapter.ViewManagerModel;
import interface_adapter.delete_account.DeleteAccountController;
import interface_adapter.logout.LogoutController;
import interface_adapter.toggle_settings.SettingsController;
import interface_adapter.toggle_settings.SettingsState;
import interface_adapter.toggle_settings.SettingsViewModel;
import view.ui_components.GeneralJLabel;
import view.ui_components.MenuBarPanel;

public class SettingsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "settings";
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    private SettingsController settingsController;
    private LogoutController logoutController;
    private DeleteAccountController deleteAccountController;
    private ChangePasswordController changePasswordController;

    private final JToggleButton accountPrivacyToggle;
    private final JToggleButton notificationsToggle;
    private final JButton logoutButton;
    private final JButton deleteAccountButton;
    private final JButton changePasswordButton;
    private final JPasswordField passwordField;

    public SettingsView(SettingsViewModel settingsViewModel, ViewManagerModel viewManagerModel) {
        this.settingsViewModel = settingsViewModel;
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel.addPropertyChangeListener(this);

        final JLabel title = new GeneralJLabel(SettingsViewModel.TITLE_LABEL,
                GUIConstants.TITLE_SIZE, GUIConstants.RED);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel settingsPanel = getSettingsPanel();

        accountPrivacyToggle = new JToggleButton(SettingsViewModel.ACCOUNT_PRIVACY_TOGGLE_ON);
        createNewPanel(settingsPanel, SettingsViewModel.PRIVACY_HEADING, SettingsViewModel.ACCOUNT_PRIVACY_LABEL,
                accountPrivacyToggle);

        notificationsToggle = new JToggleButton(SettingsViewModel.NOTIFICATIONS_TOGGLE_ON);
        createNewPanel(settingsPanel, SettingsViewModel.NOTIFICATIONS_HEADING,SettingsViewModel.NOTIFICATIONS_LABEL,
                notificationsToggle);

        logoutButton = new JButton(SettingsViewModel.LOGOUT_BUTTON_LABEL);
        createNewPanel(settingsPanel, SettingsViewModel.LOGOUT_HEADING, SettingsViewModel.LOGOUT_LABEL, logoutButton);

        deleteAccountButton = new JButton(SettingsViewModel.DELETE_BUTTON_LABEL);
        createNewPanel(settingsPanel, SettingsViewModel.DELETE_HEADING, SettingsViewModel.DELETE_LABEL,
                deleteAccountButton);

        passwordField = new JPasswordField();
        createNewPanel(settingsPanel, SettingsViewModel.PASSWORD_HEADING, SettingsViewModel.PASSWORD_LABEL,
                passwordField);

        changePasswordButton = new JButton(SettingsViewModel.PASSWORD_BUTTON_LABEL);
        settingsPanel.add(changePasswordButton);

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

        deleteAccountButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(deleteAccountButton)) {
                        final SettingsState settingsState = settingsViewModel.getState();
                        this.deleteAccountController.execute(settingsState.getUsername());
                    }
                }
        );

        changePasswordButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(changePasswordButton)) {
                        final SettingsState settingsState = settingsViewModel.getState();
                        this.changePasswordController.execute(settingsState.getUsername(),
                                settingsState.getNewPassword());
                    }
                }
        );

        passwordField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SettingsState currentState = settingsViewModel.getState();
                currentState.setNewPassword(new String(passwordField.getPassword()));
                settingsViewModel.setState(currentState);
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

        this.add(title);
        this.add(settingsPanel);
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        this.add(menuBar);
    }

    private void createNewPanel(JPanel parentPanel, String header, String label, JComponent component) {
        final JLabel newHeading = new GeneralJLabel(header, GUIConstants.HEADER_SIZE, GUIConstants.WHITE);
        parentPanel.add(newHeading);

        final JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
        subPanel.setBackground(GUIConstants.PINK);
        final JLabel newLabel = new JLabel(label);
        newLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        newLabel.setFont(GUIConstants.FONT_TEXT);
        newLabel.setForeground(GUIConstants.RED);
        subPanel.add(newLabel);
        subPanel.add(Box.createHorizontalGlue());
        component.setAlignmentX(Component.RIGHT_ALIGNMENT);
        subPanel.add(component);

        parentPanel.add(subPanel);
    }

    @NotNull
    private static JPanel getSettingsPanel() {
        final JPanel settingsPanel = new JPanel();
        final Dimension settingsPanelDimension = new Dimension(SettingsViewModel.SETTINGS_PANEL_WIDTH,
                SettingsViewModel.SETTINGS_PANEL_HEIGHT);
        settingsPanel.setMaximumSize(settingsPanelDimension);
        settingsPanel.setMinimumSize(settingsPanelDimension);
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPanel.setBackground(GUIConstants.RED);
        return settingsPanel;
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
        else if (evt.getPropertyName().equals("password changed")) {
            SettingsState state = this.settingsViewModel.getState();
            this.passwordField.setText(state.getNewPassword());
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
    public void setDeleteAccountController(DeleteAccountController controller) {
        this.deleteAccountController = controller;
    }
    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }
}
