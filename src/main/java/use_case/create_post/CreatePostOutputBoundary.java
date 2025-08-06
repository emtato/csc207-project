package use_case.create_post;

public interface CreatePostOutputBoundary {
    void prepareSuccessView(CreatePostOutputData outputData);
    void prepareFailView(CreatePostOutputData outputData);
}
