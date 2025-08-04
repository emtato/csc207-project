package use_case.like_post;

public class LikePostInputData {
    private final long postId;

    private final boolean isLiking;

    public LikePostInputData(long postId, boolean isLiking) {
        this.postId = postId;
       // this.username = username;
        this.isLiking = isLiking;
    }

    public long getPostId() {
        return postId;
    }

//    public String getUsername() {
//        return username;
//    }

    public boolean isLiking() {
        return isLiking;
    }
}
