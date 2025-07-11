package interface_adapter.note;

import interface_adapter.login.LoginState;
import use_case.note.NoteInputBoundary;
import use_case.note.NoteInputData;

/**
 * Controller for our Note related Use Cases.
 */
public class NoteController {

    private final NoteInputBoundary noteInteractor;

    public NoteController(NoteInputBoundary noteInteractor) {
        this.noteInteractor = noteInteractor;
    }

    /**
     * Executes the Note related Use Cases.
     * @param username the username of the user
     * @param note the note to be recorded
     */
    public void execute(String username, String note) {
        if (note != null) {
            final NoteInputData noteInputData = new NoteInputData(username, note);
            noteInteractor.executeSave(noteInputData);
        }
        else {
            final NoteInputData noteInputData = new NoteInputData(username);
            noteInteractor.executeRefresh(noteInputData);
        }
    }

    /**
     * Executes the "switch to NoteView" Use Case.
     */
    public void switchToNoteView() {
        noteInteractor.switchToNoteView();
    }

    /**
     * Executes the "switch to LoggedInView" Use Case.
     */
    public void switchToLoggedInView() {
        noteInteractor.switchToLoggedInView();
    }
}
