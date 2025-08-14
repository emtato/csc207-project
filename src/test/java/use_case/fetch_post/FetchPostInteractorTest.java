package use_case.fetch_post;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Account;
import entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FetchPostInteractorTest {

    @BeforeEach
    void resetDao() {
        ((InMemoryPostCommentLikesDataAccessObject)
                InMemoryPostCommentLikesDataAccessObject.getInstance())
                .clearAll();
    }

    @Test
    void successTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        //write dummy post
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put("steps", new ArrayList<>(Arrays.asList("hi")));
        dao.writePost(103332, new Account("hi", "hi"), "post title", "recipe", "description", map, new ArrayList<>(Arrays.asList("aa")), new ArrayList<>(Arrays.asList("aba")), "2025-08-07 02:12 a.m.", new ArrayList<>());


        FetchPostOutputBoundary successPresenter = new FetchPostOutputBoundary() {
            @Override
            public void prepareSuccessView(FetchPostOutputData data) {
                assertEquals(1, data.getPosts().size());
                assertEquals("hi", data.getPosts().get(0).getUser().getUsername());
                assertEquals("post title", data.getPosts().get(0).getTitle());
                assertEquals("recipe", data.getPosts().get(0).getType());
                assertEquals("description", data.getPosts().get(0).getDescription());
                Recipe post = (Recipe) data.getPosts().get(0);
                assertEquals(map.get("steps").get(0), post.getSteps());
                assertEquals(new ArrayList<>(Arrays.asList("aa")), post.getTags());
                assertEquals(new ArrayList<>(Arrays.asList("aba")), post.getImageURLs());
                assertEquals("2025-08-07 02:12 AM", data.getPosts().get(0).getDateTimeToString());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        FetchPostInputBoundary interactor = new FetchPostInteractor(dao, successPresenter);
        final FetchPostInputData inputData = new FetchPostInputData(1);

        interactor.getRandomFeedPosts(inputData);

        dao.deletePost(103332);
    }

    @Test
    void failTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        FetchPostOutputBoundary successPresenter = new FetchPostOutputBoundary() {
            @Override
            public void prepareSuccessView(FetchPostOutputData data) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No posts found", error);
            }
        };

        FetchPostInputBoundary interactor = new FetchPostInteractor(dao, successPresenter);
        final FetchPostInputData inputData = new FetchPostInputData(1);

        interactor.getRandomFeedPosts(inputData);
    }
}
