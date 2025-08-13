package interface_adapter.create_post;

import entity.Account;
import entity.Club;
import use_case.create_post.CreatePostInputBoundary;
import use_case.create_post.CreatePostInputData;
import use_case.create_post.CreatePostOutputData;

import java.lang.reflect.Array;
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

    public void createPost(Account user, String title, String recipe, String body, ArrayList<String> ingredients, String steps, ArrayList<String> tags, ArrayList<String> imageslist, ArrayList<Club> clubs) {
        CreatePostInputData inputData = new CreatePostInputData(user, title, recipe, body, ingredients, steps, tags, imageslist, clubs);
        if (!clubs.isEmpty()) {
            inputData.setClubId(clubs.get(0).getId());
        }
        interactor.execute(inputData);
    }
}
