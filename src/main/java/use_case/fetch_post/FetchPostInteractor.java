package use_case.fetch_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import data_access.PostCommentsLikesDataAccessObject;
import entity.Post;
import use_case.get_comments.GetCommentsOutputBoundary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchPostInteractor implements FetchPostInputBoundary {

    private final PostCommentsLikesDataAccessObject postDAO;
    private final FetchPostOutputBoundary presenter;

    public FetchPostInteractor(PostCommentsLikesDataAccessObject postDAO, FetchPostOutputBoundary presenter) {
        this.postDAO = postDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(FetchPostInputData inputData) {
        //Post post = postDAO.getPost(inputData.getPostID());


    }

    @Override
    public void getRandomFeedPosts(FetchPostInputData inputData) {
        List<Long> availableIDs = postDAO.getAvailablePosts();
        if (availableIDs.isEmpty()) {
            presenter.prepareFailView("No posts found");
        }
        else {
            Collections.shuffle(availableIDs);
            List<Post> result = new ArrayList<>();
            for (int i = 0; i < Math.min(inputData.getNumberOfPosts(), availableIDs.size()); i++) {
                result.add(postDAO.getPost(availableIDs.get(i)));
            }
            FetchPostOutputData data = new FetchPostOutputData((ArrayList<Post>) result);
            presenter.prepareSuccessView(data);
        }
    }

    public void getAvailablePostIDs() {
        //presenter.present(postDAO.getAvailablePosts());
    }
}
