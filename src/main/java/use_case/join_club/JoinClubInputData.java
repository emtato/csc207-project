package use_case.join_club;

public class JoinClubInputData {
    private final String username;
    private final long clubId;

    public JoinClubInputData(String username, long clubId) {
        this.username = username;
        this.clubId = clubId;
    }

    public String getUsername() { return username; }
    public long getClubId() { return clubId; }
}

