package use_case.create_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public class CreatePostOutputData {
    private final long postID;
    private final String time;

    public CreatePostOutputData(long postID, String time) {
        this.postID = postID;
        this.time = time;
    }

    public long getPostID() {
        return postID;
    }

    public String getTime() {
        return time;
    }
}
