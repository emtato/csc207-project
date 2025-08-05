package interface_adapter.fetch_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import use_case.fetch_post.FetchPostInputBoundary;
import entity.Post;
import use_case.fetch_post.FetchPostInputData;
import use_case.fetch_post.FetchPostInteractor;

import java.util.ArrayList;
import java.util.List;

public class FetchPostController {
    private final FetchPostInputBoundary interactor;

    public FetchPostController(FetchPostInputBoundary interactor) {
        this.interactor = interactor;
    }

//    public void fetch(long postID) {
//        interactor.execute(new FetchPostInputData(postID));
//    }

    public void getRandomFeedPosts(int count) {
        final FetchPostInputData data = new FetchPostInputData(count);
        interactor.getRandomFeedPosts(data); //using one implementation only
    }


    public void getAvailablePostIDs() {
         ((FetchPostInteractor) interactor).getAvailablePostIDs();
    }
}

