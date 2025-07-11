package use_case.note;

import use_case.signup.SignupOutputData;

/**
 * The output boundary for the Login Use Case.
 */
public interface NoteOutputBoundary {
    /**
     * Prepares the success view for the Note related Use Cases.
     * @param noteOutputData the output data
     */
    void prepareSuccessView(NoteOutputData noteOutputData);

    /**
     * Prepares the failure view for the Note related Use Cases.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Note View.
     */
    void switchToNoteView();

    /**
     * Switches to the Logged In View.
     */
    void switchToLoggedInView();
}
