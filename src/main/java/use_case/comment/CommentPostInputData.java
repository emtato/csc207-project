package use_case.comment;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import entity.Account;

import java.time.LocalDateTime;

public class CommentPostInputData {
    private final long postID;
    private final Account user;
    private final String commentText;
    private final LocalDateTime timestamp;

    public CommentPostInputData(long postID, Account user, String commentText, LocalDateTime timestamp) {
        this.postID = postID;
        this.user = user;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public long getPostID() {
        return postID;
    }

    public Account getUser() {
        return user;
    }

    public String getCommentText() {
        return commentText;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
