package use_case.create_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public interface CreatePostOutputBoundary {
    void prepareSuccessView(CreatePostOutputData data);
    void prepareFailView(String errorMessage);
}


