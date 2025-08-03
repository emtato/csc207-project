package use_case.fetch_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import data_access.PostCommentsLikesDataAccessObject;
import entity.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchPostInteractor implements FetchPostInputBoundary {

    private final PostCommentsLikesDataAccessObject postDAO;

    public FetchPostInteractor(PostCommentsLikesDataAccessObject postDAO) {
        this.postDAO = postDAO;
    }

    @Override
    public void execute(FetchPostInputData inputData) {
        Post post = postDAO.getPost(inputData.getPostID());


    }

    public List<Post> getRandomFeedPosts(int count) {
        List<Long> availableIDs = postDAO.getAvailablePosts();
        Collections.shuffle(availableIDs);
        List<Post> result = new ArrayList<>();
        for (int i = 0; i < Math.min(count, availableIDs.size()); i++) {
            result.add(postDAO.getPost(availableIDs.get(i)));
        }
        return result;
    }

    public List<Long> getAvailablePostIDs() {
        return postDAO.getAvailablePosts();
    }
}
