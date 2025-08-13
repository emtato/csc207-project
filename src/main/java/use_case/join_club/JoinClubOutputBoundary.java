package use_case.join_club;

public interface JoinClubOutputBoundary {
    void prepareSuccessView(JoinClubOutputData outputData);
    void prepareFailView(String error);
}

