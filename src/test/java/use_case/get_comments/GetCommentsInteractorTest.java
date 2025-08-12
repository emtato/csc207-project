package use_case.get_comments;/**
 * Created by Emilia on 2025-08-11!
 * Description:
 * ^ • ω • ^
 */

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Account;
import org.junit.jupiter.api.Test;
import use_case.fetch_post.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GetCommentsInteractorTest {

    @Test
    void successTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        //write dummy post
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put("steps", new ArrayList<>(Arrays.asList("hi")));
        dao.writePost(103332, new Account("hi", "hi"), "post title", "recipe", "description", map, new ArrayList<>(Arrays.asList("aa")), new ArrayList<>(Arrays.asList("aa")), "2025-08-07 02:12 AM", new ArrayList<>());

        //write dummy comment
        dao.addComment(103332, new Account("ih", "ih"), "HELLOOOO", LocalDateTime.of(2025, 8, 11, 12, 0));

        GetCommentsOutputBoundary successPresenter = new GetCommentsOutputBoundary() {
            @Override
            public void prepareSuccessView(GetCommentsOutputData outputData) {
                assertEquals("HELLOOOO", outputData.getComments().get(0).getComment());
                assertEquals("ih",outputData.getComments().get(0).getAccount().getUsername());
                assertEquals(LocalDateTime.of(2025, 8, 11, 12, 0), outputData.getComments().get(0).getDate());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        GetCommentsInputBoundary interactor = new GetCommentsInteractor(dao, successPresenter);
        final GetCommentsInputData inputData = new GetCommentsInputData(103332);

        interactor.execute(inputData);

        dao.deletePost(103332);
        dao.getComments(103332).clear();
    }

    @Test
    void failTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        GetCommentsOutputBoundary successPresenter = new GetCommentsOutputBoundary() {
            @Override
            public void prepareSuccessView(GetCommentsOutputData outputData) {
                fail("Use case is supposed to fail but didnt.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("no comments found", error);
            }
        };

        GetCommentsInputBoundary interactor = new GetCommentsInteractor(dao, successPresenter);
        final GetCommentsInputData inputData = new GetCommentsInputData(103332);

        interactor.execute(inputData);
    }
}



