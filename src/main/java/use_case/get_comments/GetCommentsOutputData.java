package use_case.get_comments;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import entity.Comment;
import java.util.List;

public class GetCommentsOutputData {
    private final List<Comment> comments;

    public GetCommentsOutputData(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
