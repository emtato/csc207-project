package use_case.delete_club;

public interface DeleteClubOutputBoundary {
    void prepareSuccessView(DeleteClubOutputData data);
    void prepareFailView(String error);
}

