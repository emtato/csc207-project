package use_case.delete_account;

/**
 * The Input Data for the Delete Account Use Case.
 */
public class DeleteAccountInputData {
    private String username;

    public DeleteAccountInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
