package use_case.get_comments;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public interface GetCommentsOutputBoundary {
    void prepareSuccessView(GetCommentsOutputData outputData);
    void prepareFailView(String errorMessage);
}

