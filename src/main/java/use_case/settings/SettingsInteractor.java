package use_case.settings;

public class SettingsInteractor implements SettingsInputBoundary{
    private final SettingsUserDataAccessInterface userDataAccessObject;
    private final SettingsOutputBoundary presenter;

    //TODO: factories
    public SettingsInteractor(SettingsUserDataAccessInterface userDataAccessInterface,
                             SettingsOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(SettingsInputData settingsInputData) {
        //presenter.prepareFailView("error message");
        //final SettingsOutputData settingsOutputData = new SettingsOutputData(data);
        //presenter.prepareSuccessView(settingsOutputData);
    }

}
