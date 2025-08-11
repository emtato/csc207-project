package use_case.create_club;

import entity.Club;

public class CreateClubOutputData {
    private final Club club;
    private final boolean useCaseSuccess;
    private final String errorMessage;

    public CreateClubOutputData(Club club, boolean useCaseSuccess, String errorMessage) {
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
