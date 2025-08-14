package use_case.get_comments;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import entity.Comment;
import data_access.PostCommentsLikesDataAccessObject;

import java.util.List;

public class GetCommentsInteractor implements GetCommentsInputBoundary {
    private final PostCommentsLikesDataAccessObject postDAO;
    private final GetCommentsOutputBoundary presenter;

    public GetCommentsInteractor(PostCommentsLikesDataAccessObject postDAO, GetCommentsOutputBoundary presenter) {
        this.postDAO = postDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetCommentsInputData inputData) {
        List<Comment> comments = postDAO.getComments(inputData.getPostID());
        if (comments == null || comments.isEmpty()) {
            presenter.prepareFailView("no comments found");
        }
        else {
            GetCommentsOutputData outputData = new GetCommentsOutputData(comments);
            presenter.prepareSuccessView(outputData);
        }
    }
}
