package interface_adapter.create_post_view;

import use_case.create_post.CreatePostOutputBoundary;

import java.util.ArrayList;

public class CreatePostPresenter implements CreatePostOutputBoundary {
    private final CreatePostViewModel createPostViewModel;

    public CreatePostPresenter(CreatePostViewModel createPostViewModel) {
        this.createPostViewModel = createPostViewModel;
    }

    @Override
    public void prepareSuccessView() {
        CreatePostState state = createPostViewModel.getState();
        state.setTitle("");
        state.setBody("");
        state.setType("");
        state.setSteps("");
        state.setIngredients(new ArrayList<>());
        state.setTags(new ArrayList<>());
        state.setClubs(new ArrayList<>());
        state.setImages(new ArrayList<>());
        createPostViewModel.setState(state);
        createPostViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        CreatePostState state = createPostViewModel.getState();
        // Handle the error case - could show an error message
        state.setError(error);
        createPostViewModel.setState(state);
        createPostViewModel.firePropertyChanged();
    }
}
