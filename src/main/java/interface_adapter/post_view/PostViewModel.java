package interface_adapter.post_view;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the meow
 */
public class PostViewModel extends ViewModel<PostState> {




    public PostViewModel() {
        super("analyze");
        setState(new PostState());
    }
}
