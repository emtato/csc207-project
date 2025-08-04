package use_case.get_comments;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public class GetCommentsInputData {
    private final long postID;

    public GetCommentsInputData(long postID) {
        this.postID = postID;
    }

    public long getPostID() {
        return postID;
    }
}
