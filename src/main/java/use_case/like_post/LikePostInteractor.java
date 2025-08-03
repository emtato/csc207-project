package use_case.like_post;

import data_access.PostCommentsLikesDataAccessObject;
import entity.Post;

/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */
public class LikePostInteractor implements LikePostInputBoundary {
    private final PostCommentsLikesDataAccessObject postDAO;
    //private final LikePostOutputBoundary presenter;

    public LikePostInteractor(PostCommentsLikesDataAccessObject postDAO) {
        this.postDAO = postDAO;
    }

    @Override
    public void execute(LikePostInputData inputData) {
        // get post from DAO
        Post post = postDAO.getPost(inputData.getPostId());

        if (inputData.isLiking()) {
            post.setLikes(post.getLikes() + 1);
            postDAO.updateLikesForPost(post.getID(), 1);
        } else {
            post.setLikes(post.getLikes() - 1);
            postDAO.updateLikesForPost(post.getID(), -1);
        }

        //presenter.prepareSuccessView(post);
    }
}
