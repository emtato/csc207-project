package use_case.create_post;/**
 * Created by Emilia on 2025-08-11!
 * Description:
 * ^ • ω • ^
 */

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;
import entity.Recipe;
import org.junit.jupiter.api.Test;
import use_case.comment.CommentPostInputData;
import use_case.comment.CommentPostInteractor;
import use_case.comment.CommentPostOutputBoundary;
import use_case.comment.CommentPostOutputData;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CreatePostInteractorTest {

    @Test
    void successTest() {
        PostCommentsLikesDataAccessObject dao = InMemoryPostCommentLikesDataAccessObject.getInstance();
        UserDataAccessObject userDao = InMemoryUserDataAccessObject.getInstance();

        //save dummy user into memory db
        Account user = new Account("username", "password");
        userDao.save(user);

        CreatePostOutputBoundary successPresenter = new CreatePostOutputBoundary() {

            @Override
            public void prepareSuccessView(CreatePostOutputData outputData) {
                long postID = outputData.getPostID();
                Post post = dao.getPost(postID);
                assertEquals("username", post.getUser().getUsername());
                assertEquals("post title", post.getTitle());
                assertEquals("recipe", post.getType());
                assertEquals("description", post.getDescription());
                Recipe recpie = (Recipe) post;
                assertEquals("steps", recpie.getSteps());
                assertEquals(new ArrayList<>(Arrays.asList("ingredients")), recpie.getIngredients());
                assertEquals(new ArrayList<>(Arrays.asList("tags")), post.getTags());
                assertEquals(new ArrayList<>(Arrays.asList("images")), post.getImageURLs());
                dao.deletePost(post.getID());
            }

            @Override
            public void prepareFailView(CreatePostOutputData outputData) {
                System.out.println(":C");
                fail("Use case failure is unexpected.");
            }
        };

        CreatePostInteractor interactor = new CreatePostInteractor(dao, userDao, successPresenter);

        final CreatePostInputData inputData = new CreatePostInputData(user, "post title", "recipe", "description", new ArrayList<>(Arrays.asList("ingredients")), "steps", new ArrayList<>(Arrays.asList("tags")), new ArrayList<>(Arrays.asList("images")), new ArrayList<>());
        interactor.execute(inputData);
    }
}
