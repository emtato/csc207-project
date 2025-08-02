package interface_adapter.settings;

/**
 * The state for the Settings View Model.
 */
public class SettingsState {
    private String username;
    private boolean isPublic;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
