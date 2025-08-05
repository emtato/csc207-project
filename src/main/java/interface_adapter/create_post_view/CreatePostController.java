package interface_adapter.create_post_view;

import entity.Account;
import use_case.create_post.CreatePostInputBoundary;
import use_case.create_post.CreatePostInputData;
import use_case.fetch_post.FetchPostInputBoundary;
import use_case.fetch_post.FetchPostInputData;
import use_case.fetch_post.FetchPostInteractor;
import use_case.like_post.LikePostInputData;

import java.util.ArrayList;

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
     public void createPost(Account user, String title, String type,
                            String body, ArrayList<String> ingredients, String steps,
                            ArrayList<String> tags, ArrayList<String> images, ArrayList<String> clubs) {
        CreatePostInputData inputData = new CreatePostInputData(user, title, type, body, ingredients, steps, tags, images, clubs);
        interactor.execute(inputData);
    }
}
