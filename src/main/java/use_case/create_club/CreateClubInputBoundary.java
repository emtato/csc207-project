package use_case.create_club;

public interface CreateClubInputBoundary {
    /**
     * Executes the create club use case with the provided input data.
     */
    void execute(CreateClubInputData inputData);
    // Removed member selection responsibility (moved to SelectClubMembersInputBoundary)
}
