package use_case.settings;

/**
 * The output boundary for the Change Settings Use Case.
 */
public interface SettingsOutputBoundary {
    /**
     * Prepares the success view for the Change Settings Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(SettingsOutputData outputData);

    /**
     * Prepares the failure view for the Change Settings Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
