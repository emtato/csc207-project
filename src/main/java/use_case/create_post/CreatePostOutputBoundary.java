package use_case.create_post;

public interface CreatePostOutputBoundary {
    void prepareSuccessView();
    void prepareFailView(String error);
}
