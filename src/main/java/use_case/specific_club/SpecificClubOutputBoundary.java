package use_case.specific_club;

public interface SpecificClubOutputBoundary {
    /**
     * Prepares the success view with the provided output data.
     *
     * @param outputData The data to be presented in the success view
     */
    void prepareSuccessView(SpecificClubOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message to be displayed
     */
    void prepareFailView(String error);
}
