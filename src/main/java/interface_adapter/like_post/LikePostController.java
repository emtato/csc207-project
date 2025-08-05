package interface_adapter.like_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import use_case.like_post.LikePostInputBoundary;
import use_case.like_post.LikePostInputData;

public class LikePostController {

    private final LikePostInputBoundary interactor;

    public LikePostController(LikePostInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void likePost(long postID, boolean isLiking) {
        LikePostInputData inputData = new LikePostInputData(postID, isLiking);
        interactor.execute(inputData);
    }
}
