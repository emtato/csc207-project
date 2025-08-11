package use_case.clubs_home;

public interface ClubOutputBoundary {
    void prepareSuccessView(ClubOutputData clubOutputData);
    void prepareFailView(String error);
}
