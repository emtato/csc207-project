package use_case.comment;/**
 * Created by Emilia on 2025-08-11!
 * Description:
 * ^ • ω • ^
 */

public interface CommentPostOutputBoundary {

    void prepareSuccessView(CommentPostOutputData outputData);
    void prepareFailView(String errorMessage);

}
