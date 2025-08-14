package use_case.create_club;

public interface CreateClubOutputBoundary {
    /**
     * Prepares the success view after club creation
     */
    void prepareSuccessView(CreateClubOutputData outputData);

    /**
     * Prepares the fail view with error message
     */
    void prepareFailView(String error);
    // Removed member selection method to isolate responsibility.
}
