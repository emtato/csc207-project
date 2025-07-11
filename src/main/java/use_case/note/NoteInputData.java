package use_case.note;

/**
 * The Input Data for the Note Use Case.
 */
public class NoteInputData {

    private final String username;
    private final String message;

    public NoteInputData(String username) {
        this.username = username;
        this.message = "";
    }

    public NoteInputData(String username, String message) {
        this.username = username;
        this.message = message;
    }

    String getUsername() {
        return username;
    }

    String getMessage() {
        return message;
    }

}
