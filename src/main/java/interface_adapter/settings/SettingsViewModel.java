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

    public SettingsViewModel() {
        super("settings");
        setState(new SettingsState());
    }
}
