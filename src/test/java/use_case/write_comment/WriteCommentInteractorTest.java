package use_case.write_comment;/**
 * Created by Emilia on 2025-08-11!
 * Description:
 * ^ • ω • ^
 */

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Account;
import org.junit.jupiter.api.Test;
import use_case.comment.CommentPostInputData;
import use_case.comment.CommentPostInteractor;
import use_case.comment.CommentPostOutputBoundary;
import use_case.comment.CommentPostOutputData;
import use_case.get_comments.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WriteCommentInteractorTest {
    @Test
    void successTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        //write dummy post
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put("steps", new ArrayList<>(Arrays.asList("hi")));
        dao.writePost(103332, new Account("hi", "hi"), "post title", "recipe", "description", map, new ArrayList<>(Arrays.asList("aa")), new ArrayList<>(Arrays.asList("aa")), "2025-08-07 02:12 AM", new ArrayList<>());

        CommentPostOutputBoundary successPresenter = new CommentPostOutputBoundary() {

            @Override
            public void prepareSuccessView(CommentPostOutputData outputData) {
                assertEquals("HELLOOOO", dao.getComments(103332).get(0).getComment());
                assertEquals("hi", dao.getComments(103332).get(0).getAccount().getUsername());
                assertEquals(LocalDateTime.of(2025, 8, 11, 12, 0), dao.getComments(103332).get(0).getDate());
            }


            @Override
            public void prepareFailView(String error) {
                fail("there is no post with this ID :(");
            }
        };

        CommentPostInteractor interactor = new CommentPostInteractor(dao, successPresenter);
        final CommentPostInputData inputData = new CommentPostInputData(103332, new Account("hi", "hi"), "HELLOOOO", LocalDateTime.of(2025, 8, 11, 12, 0));

        interactor.execute(inputData);

        dao.deletePost(103332);
        dao.getComments(103332).clear();
    }

    @Test
    void failTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();

        CommentPostOutputBoundary successPresenter = new CommentPostOutputBoundary() {

            @Override
            public void prepareSuccessView(CommentPostOutputData outputData) {
                fail("wasnt upposed to succedd!");

            }


            @Override
            public void prepareFailView(String error) {
                assertEquals("there is no post with this ID :(", error);

            }
        };

        CommentPostInteractor interactor = new CommentPostInteractor(dao, successPresenter);
        final CommentPostInputData inputData = new CommentPostInputData(103332, new Account("hi", "hi"), "HELLOOOO", LocalDateTime.of(2025, 8, 11, 12, 0));

        interactor.execute(inputData);
    }
}
