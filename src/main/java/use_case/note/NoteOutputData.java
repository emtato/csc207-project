package use_case.note;

/**
 * Output Data for the Note Use Case.
 */
public class NoteOutputData {

    private final String note;

    private final boolean useCaseFailed;

    public NoteOutputData(String note, boolean useCaseFailed) {
        this.note = note;
        this.useCaseFailed = useCaseFailed;
    }

    public String getNote() {
        return note;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
