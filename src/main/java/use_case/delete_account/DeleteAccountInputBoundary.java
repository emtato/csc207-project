package use_case.delete_account;

/**
 * Input Boundary for actions which are related to deleting account.
 */
public interface DeleteAccountInputBoundary {
    /**
     * Executes the Delete Account use case.
     * @param deleteAccountInputData the input data
     */
    void execute(DeleteAccountInputData deleteAccountInputData);
}
