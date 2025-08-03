package interface_adapter.settings;

import interface_adapter.ViewModel;

/**
 * The View Model for the Settings View.
 */
public class SettingsViewModel extends ViewModel<SettingsState> {

    public static final String TITLE_LABEL = "Settings Menu";
    public static final String PRIVACY_HEADING = "Privacy";
    public static final String ACCOUNT_PRIVACY_LABEL = "Account Privacy";
    public static final String ACCOUNT_PRIVACY_TOGGLE_ON = "PUBLIC";
    public static final String ACCOUNT_PRIVACY_TOGGLE_OFF = "PRIVATE";
    public static final String NOTIFICATIONS_HEADING = "Notifications";
    public static final String NOTIFICATIONS_LABEL = "Notification Enabled";
    public static final String NOTIFICATIONS_TOGGLE_ON = "ON";
    public static final String NOTIFICATIONS_TOGGLE_OFF = "OFF";
    public static final int SETTINGS_PANEL_WIDTH = 1200;
    public static final int SETTINGS_PANEL_HEIGHT = 600;
    public static final String LOGOUT_LABEL = "Logout";
    public static final String LOGOUT_BUTTON_LABEL = "Logout";
    public static final String LOGOUT_HEADING = "Logout";

    public SettingsViewModel() {
        super("settings");
        setState(new SettingsState());
    }
}
