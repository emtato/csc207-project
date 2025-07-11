package interface_adapter.note;

import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.LoggedInState;
import interface_adapter.change_password.LoggedInViewModel;
import use_case.note.NoteOutputBoundary;
import use_case.note.NoteOutputData;

/**
 * The presenter for our Note viewing and editing program.
 */
public class NotePresenter implements NoteOutputBoundary {
    private ViewManagerModel viewManagerModel;
    private final NoteViewModel noteViewModel;
    private final LoggedInViewModel loggedInViewModel;

    public NotePresenter(ViewManagerModel viewManagerModel, NoteViewModel noteViewModel,
                         LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.noteViewModel = noteViewModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    /**
     * Prepares the success view for the Note related Use Cases.
     *
     * @param noteOutputData the output data
     */
    @Override
    public void prepareSuccessView(NoteOutputData noteOutputData) {
        noteViewModel.getState().setNote(noteOutputData.getNote());
        noteViewModel.getState().setError(null);
        noteViewModel.firePropertyChanged();

        this.viewManagerModel.setState(noteViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    /**
     * Prepares the failure view for the Note related Use Cases.
     *
     * @param errorMessage the explanation of the failure
     */
    @Override
    public void prepareFailView(String errorMessage) {
        noteViewModel.getState().setError(errorMessage);
        noteViewModel.firePropertyChanged();
    }

    @Override
    public void switchToNoteView() {
        final NoteState noteState = noteViewModel.getState();
        final LoggedInState loggedInState = loggedInViewModel.getState();
        noteState.setUsername(loggedInState.getUsername());
        noteViewModel.setState(noteState);
        noteViewModel.firePropertyChanged();

        viewManagerModel.setState(noteViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToLoggedInView() {
        viewManagerModel.setState(loggedInViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
