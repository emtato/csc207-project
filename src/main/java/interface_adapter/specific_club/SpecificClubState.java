package interface_adapter.specific_club;

import entity.Club;

public class SpecificClubState {
    private Club club;
    private String error;

    public SpecificClubState() {}

    public SpecificClubState(Club club) {
        this.club = club;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
