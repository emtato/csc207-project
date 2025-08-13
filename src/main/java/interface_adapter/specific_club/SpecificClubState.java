package interface_adapter.specific_club;

import entity.Club;

public class SpecificClubState {
    private Club club;
    private String error;
    private boolean deleted = false; // new flag

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

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
