package interface_adapter.post_view;

import use_case.like_post.LikePostInputBoundary;

/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */
//public class PostController {
//    private final LikePostInputBoundary likeInteractor;
//    private final AddCommentInputBoundary commentInteractor;
//
//    public PostController(LikePostInputBoundary likeInteractor, AddCommentInputBoundary commentInteractor) {
//        this.likeInteractor = likeInteractor;
//        this.commentInteractor = commentInteractor;
//    }
//
//    public void toggleLike(long postId, String username, boolean isLiking) {
//        LikePostInputData inputData = new LikePostInputData(postId, username, isLiking);
//        likeInteractor.execute(inputData);
//    }
//
//    public void addComment(long postId, String username, String commentText) {
//        AddCommentInputData inputData = new AddCommentInputData(postId, username, commentText);
//        commentInteractor.execute(inputData);
//    }
//}
