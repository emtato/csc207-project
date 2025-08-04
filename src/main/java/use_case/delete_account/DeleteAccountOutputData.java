package use_case.delete_account;

/**
 * Output Data for the Delete Account Use Case.
 */
public class DeleteAccountOutputData {
    private final String username;

    public DeleteAccountOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
