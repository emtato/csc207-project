package interface_adapter.write_comment;

import entity.Account;
import use_case.comment.CommentPostInputBoundary;
import use_case.comment.CommentPostInputData;
import use_case.comment.CommentPostInteractor;
import use_case.get_comments.GetCommentsInputBoundary;
import use_case.get_comments.GetCommentsInputData;

import java.time.LocalDateTime;

/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public class WriteCommentController {
    private final CommentPostInputBoundary interactor;

    public WriteCommentController(CommentPostInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void writeComment(long postID, Account user, String comment, LocalDateTime timestamp) {
        CommentPostInputData inputData = new CommentPostInputData(postID, user, comment, timestamp);
        interactor.execute(inputData);
    }
}
