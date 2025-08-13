package interface_adapter.create_post;

import use_case.create_post.CreatePostInputBoundary;
import use_case.create_post.CreatePostInputData;

/**
 * Created by Emilia on 2025-08-05!
 * Description:
 * ^ • ω • ^
 */

public class CreatePostController {
    private final CreatePostInputBoundary interactor;

    public CreatePostController(CreatePostInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(CreatePostInputData inputData) {
        interactor.execute(inputData);
    }
}
