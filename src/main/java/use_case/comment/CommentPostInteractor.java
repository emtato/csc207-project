package use_case.comment;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import data_access.PostCommentsLikesDataAccessObject;
import use_case.create_post.CreatePostOutputBoundary;

public class CommentPostInteractor implements CommentPostInputBoundary {

    private final PostCommentsLikesDataAccessObject postDAO;
    private final CommentPostOutputBoundary presenter;

    public CommentPostInteractor(PostCommentsLikesDataAccessObject postDAO, CommentPostOutputBoundary presenter) {
        this.postDAO = postDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(CommentPostInputData inputData) {
        if (postDAO.getPost(inputData.getPostID()) != null) {
            postDAO.addComment(
                    inputData.getPostID(),
                    inputData.getUser(),
                    inputData.getCommentText(),
                    inputData.getTimestamp()
            );
            presenter.prepareSuccessView(new CommentPostOutputData(inputData.getUser(), inputData.getCommentText()));
        }
        else {
            presenter.prepareFailView("there is no post with this ID :(");
        }
    }
}
