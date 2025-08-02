package use_case.settings;

import entity.User;

public class SettingsInteractor implements SettingsInputBoundary{
    private final SettingsUserDataAccessInterface userDataAccessObject;
    private final SettingsOutputBoundary presenter;

    public SettingsInteractor(SettingsUserDataAccessInterface userDataAccessInterface,
                             SettingsOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void executePrivacyToggle(SettingsInputData settingsInputData) {
        final User user = userDataAccessObject.get(settingsInputData.getUsername());
        userDataAccessObject.setPrivacy(user, settingsInputData.isPublic());
        final SettingsOutputData settingsOutputData = new SettingsOutputData(settingsInputData.isPublic());
    }

}
