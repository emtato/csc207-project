package use_case.comment;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import data_access.PostCommentsLikesDataAccessObject;

public class CommentPostInteractor implements CommentPostInputBoundary {

    private final PostCommentsLikesDataAccessObject postDAO;

    public CommentPostInteractor(PostCommentsLikesDataAccessObject postDAO) {
        this.postDAO = postDAO;
    }

    @Override
    public void execute(CommentPostInputData inputData) {
        postDAO.addComment(
                inputData.getPostID(),
                inputData.getUser(),
                inputData.getCommentText(),
                inputData.getTimestamp()
        );
    }
}
