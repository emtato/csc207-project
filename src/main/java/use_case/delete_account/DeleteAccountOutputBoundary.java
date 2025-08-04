package use_case.delete_account;

/**
 * The output boundary for the Delete Account Use Case.
 */
public interface DeleteAccountOutputBoundary {
    /**
     * Prepares the success view for the Delete Account Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(DeleteAccountOutputData outputData);

    /**
     * Prepares the failure view for the Delete Account Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
