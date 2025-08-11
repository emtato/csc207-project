package use_case.clubs_home;

public class ClubInputData {
    private final String username;
    private final String clubIdToJoin;

    public ClubInputData(String username, String clubIdToJoin) {
        this.username = username;
        this.clubIdToJoin = clubIdToJoin;
    }

    public String getUsername() {
        return username;
    }

    public String getClubIdToJoin() {
        return clubIdToJoin;
    }
}
