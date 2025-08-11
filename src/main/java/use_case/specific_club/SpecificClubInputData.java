package use_case.specific_club;

public class SpecificClubInputData {
    private final long clubId;
    private final String username;
    private final String action;

    public SpecificClubInputData(long clubId, String username, String action) {
        this.clubId = clubId;
        this.username = username;
        this.action = action;
    }

    public long getClubId() {
        return clubId;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }
}
