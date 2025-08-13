package use_case.delete_club;

public class DeleteClubInputData {
    private final String requesterUsername;
    private final long clubId;

    public DeleteClubInputData(String requesterUsername, long clubId) {
        this.requesterUsername = requesterUsername;
        this.clubId = clubId;
    }

    public String getRequesterUsername() { return requesterUsername; }
    public long getClubId() { return clubId; }
}

