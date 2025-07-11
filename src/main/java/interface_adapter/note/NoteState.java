package interface_adapter.note;

/**
 * The State for a note.
 * <p>For this example, a note is simplay a string.</p>
 */
public class NoteState {
    private String note = "";
    private String username = "";
    private String error;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setError(String errorMessage) {
        this.error = errorMessage;
    }

    public String getError() {
        return error;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
