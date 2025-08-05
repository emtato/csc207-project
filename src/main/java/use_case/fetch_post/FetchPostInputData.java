package use_case.fetch_post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public class FetchPostInputData {
    // private final long postID;
    private final int numberOfPosts;
    //private final List<Long> postIDs;

    public FetchPostInputData(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
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
