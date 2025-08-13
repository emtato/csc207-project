package use_case.specific_club;

/**
 * Input boundary interface for specific club operations.
 * This interface defines the methods that the controller can call to interact with the specific club use case.
 */
public interface SpecificClubInputBoundary {
    /**
     * Executes the specific club use case with the given input data.
     *
     * @param inputData The input data containing specific club details
     */
    void execute(SpecificClubInputData inputData);

    /**
     * Loads the specific club's data.
     *
     * @param clubId The ID of the club to load
     */
    void loadClub(long clubId);

    /**
     * Fetches the club's announcements.
     *
     * @param clubId The ID of the club whose announcements to fetch
     */
    void fetchAnnouncements(long clubId);

    /**
     * Fetches the club's regular posts.
     *
     * @param clubId The ID of the club whose posts to fetch
     */
    void fetchPosts(long clubId);

    /**
     * Checks if a user is a member of the club.
     *
     * @param username The username to check
     * @param clubId The ID of the club
     * @return true if the user is a member, false otherwise
     */
    boolean isMember(String username, long clubId);
}
