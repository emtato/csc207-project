package use_case.leave_club;

public class LeaveClubInputData {
    private final String username;
    private final long clubId;

    public LeaveClubInputData(String username, long clubId) {
        this.username = username;
        this.clubId = clubId;
    }

    public String getUsername() { return username; }
    public long getClubId() { return clubId; }
}

