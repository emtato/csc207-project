package use_case.note;

/**
 * The Input Boundary for our note-related use cases. Since they are closely related,
 * we have included them both in the same interface for simplicity.
 */
public interface NoteInputBoundary {

    /**
     * Executes the refresh note use case.
     * @param noteInputData the input data
     */
    void executeRefresh(NoteInputData noteInputData);

    /**
     * Executes the save note use case.
     * @param noteInputData the input data
     */
    void executeSave(NoteInputData noteInputData);

    /**
     * Switches to the Note View.
     */
    void switchToNoteView();

    /**
     * Switches to the Logged In View.
     */
    void switchToLoggedInView();

}
