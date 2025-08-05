package use_case.toggle_settings;

/**
 * The output boundary for the Change Settings Use Case.
 */
public interface SettingsOutputBoundary {
    /**
     * Prepares the success view for the Change Privacy Use Case.
     * @param outputData the output data
     */
    void preparePrivacySuccessView(SettingsOutputData outputData);

    /**
     * Prepares the success view for the Enable/disable Notifications Use Case.
     * @param outputData the output data
     */
    void prepareNotificationsSuccessView(SettingsOutputData outputData);

    /**
     * Prepares the failure view for the Change Settings Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
