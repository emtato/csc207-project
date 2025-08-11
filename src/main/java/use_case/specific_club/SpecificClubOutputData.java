package use_case.specific_club;

import entity.Club;

public class SpecificClubOutputData {
    private final Club club;
    private final boolean useCaseSuccess;
    private String errorMessage;

    public SpecificClubOutputData(Club club, boolean useCaseSuccess, String errorMessage) {
        this.club = club;
        this.useCaseSuccess = useCaseSuccess;
        this.errorMessage = errorMessage;
    }

    public Club getClub() {
        return club;
    }

    public boolean isSuccess() {
        return useCaseSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
