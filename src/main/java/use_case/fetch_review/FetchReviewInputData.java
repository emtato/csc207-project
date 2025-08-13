package use_case.fetch_review;

public class FetchReviewInputData {
    // private final long postID;
    private final int numberOfReviews;
    //private final List<Long> postIDs;

    public FetchReviewInputData(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }
}
//    public FetchPostInputData(List<Long> postIDs) {
//        this.postID = -1;
//        this.postIDs = postIDs;
//    }
//
//    public long getPostID() {
//        return postID;
//    }
//
//    public List<Long> getPostIDs() {
//        return postIDs;
//    }
//}
