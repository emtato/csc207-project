package use_case.note;

import entity.User;
import entity.UserFactory;

/**
 * The "Use Case Interactor" for our two note-related use cases of refreshing
 * the contents of the note and saving the contents of the note. Since they
 * are closely related, we have combined them here for simplicity.
 */
public class NoteInteractor implements NoteInputBoundary {

    private final NoteDataAccessInterface noteDataAccessInterface;
    private final NoteOutputBoundary noteOutputBoundary;
    private final UserFactory userFactory;

    public NoteInteractor(NoteDataAccessInterface noteDataAccessInterface,
                          NoteOutputBoundary noteOutputBoundary, UserFactory userFactory) {
        this.noteDataAccessInterface = noteDataAccessInterface;
        this.noteOutputBoundary = noteOutputBoundary;
        this.userFactory = userFactory;
    }

    /**
     * Executes the refresh note use case.
     *
     * @param noteInputData the input data
     */
    @Override
    public void executeRefresh(NoteInputData noteInputData) {
        try {
            final User user = noteDataAccessInterface.get(noteInputData.getUsername());
            final String note = noteDataAccessInterface.loadNote(user);
            final NoteOutputData noteOutputData = new NoteOutputData(note, false);
            noteOutputBoundary.prepareSuccessView(noteOutputData);
        }
        catch (DataAccessException ex) {
            noteOutputBoundary.prepareFailView(ex.getMessage());
        }
    }

    /**
     * Executes the save note use case.
     *
     * @param noteInputData the input data
     */
    @Override
    public void executeSave(NoteInputData noteInputData) {
        try {
            final User user = noteDataAccessInterface.get(noteInputData.getUsername());
            final String updatedNote = noteDataAccessInterface.saveNote(user, noteInputData.getMessage());
            final NoteOutputData noteOutputData = new NoteOutputData(updatedNote, false);
            noteOutputBoundary.prepareSuccessView(noteOutputData);
        }
        catch (DataAccessException ex) {
            noteOutputBoundary.prepareFailView(ex.getMessage());
        }
    }

    @Override
    public void switchToNoteView() {
        noteOutputBoundary.switchToNoteView();
    }

    @Override
    public void switchToLoggedInView() {
        noteOutputBoundary.switchToLoggedInView();
    }
}
