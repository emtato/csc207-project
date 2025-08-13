package use_case.leave_club;

public interface LeaveClubOutputBoundary {
    void prepareSuccessView(LeaveClubOutputData outputData);
    void prepareFailView(String error);
}

