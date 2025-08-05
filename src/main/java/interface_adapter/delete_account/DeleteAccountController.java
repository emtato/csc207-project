package interface_adapter.delete_account;

import use_case.delete_account.DeleteAccountInputBoundary;
import use_case.delete_account.DeleteAccountInputData;

public class DeleteAccountController {
    private DeleteAccountInputBoundary deleteAccountUseCaseInteractor;

    public DeleteAccountController(DeleteAccountInputBoundary deleteAccountUseCaseInteractor) {
        // Save the interactor in the instance variable.
        this.deleteAccountUseCaseInteractor = deleteAccountUseCaseInteractor;
    }

    /**
     * Executes the Delete Account Use Case.
     * @param username the username of the user being deleted
     */
    public void execute(String username) {
        final DeleteAccountInputData user = new DeleteAccountInputData(username);
        deleteAccountUseCaseInteractor.execute(user);
    }
}
