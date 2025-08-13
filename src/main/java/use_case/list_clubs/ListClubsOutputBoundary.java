package use_case.list_clubs;

public interface ListClubsOutputBoundary {
    void prepareSuccessView(ListClubsOutputData outputData);
    void prepareFailView(String error);
}

