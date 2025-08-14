package view;

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.spoonacular.SpoonacularAPI;
import entity.Account;
import entity.Comment;
import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.analyze_recipe.AnalyzeRecipeController;
import interface_adapter.analyze_recipe.AnalyzeRecipePresenter;
import interface_adapter.analyze_recipe.AnalyzeRecipeViewModel;
import interface_adapter.create_post.CreatePostViewModel;
import interface_adapter.get_comments.GetCommentsController;
import interface_adapter.get_comments.GetCommentsPresenter;
import interface_adapter.get_comments.GetCommentsViewModel;
import interface_adapter.like_post.LikePostController;
import org.junit.jupiter.api.Test;
import use_case.analyze_recipe.AnalyzeRecipeInteractor;
import use_case.get_comments.GetCommentsInteractor;
import use_case.like_post.LikePostInteractor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Emilia on 2025-08-13!
 * Description:
 * ^ • ω • ^
 */


public class PostViewTest {
    @Test
    public void test() throws IOException, InterruptedException {
        String steps = "1. smash 4 glorbles of bean paste into a sock, microwave till it sings\n" + "2.sprinkle in 2 blinks of mystery flakes, scream gently\n" + "3.serve upside-down on a warm tile";
        Recipe trialpost = new Recipe(new Account("meow", "woof"), 483958292, "repice for glunking", "i made it for the tiger but the bird keeps taking it", new ArrayList<>(Arrays.asList("glorbles", "beans", "tile", "dandelion")), steps, new ArrayList<>(Arrays.asList("yeah")));
        trialpost.setTags(new ArrayList<>(Arrays.asList("glorpy", "beany")));
        trialpost.setImageURLs(new ArrayList<>(Arrays.asList("https://i.imgur.com/eA9NeJ1.jpeg", "https://i.imgur.com/wzX83Zc.jpeg")));

        PostView postView = new PostView(new ViewManagerModel(), trialpost, new GetCommentsViewModel(), new AnalyzeRecipeViewModel());
        InMemoryPostCommentLikesDataAccessObject mem = (InMemoryPostCommentLikesDataAccessObject) InMemoryPostCommentLikesDataAccessObject.getInstance();
        mem.addComment(483958292, new Account("hi", "hi"), "hi", LocalDateTime.now());

        postView.setGetCommentsController(new GetCommentsController(new GetCommentsInteractor(InMemoryPostCommentLikesDataAccessObject.getInstance(), new GetCommentsPresenter(new GetCommentsViewModel()))));
        postView.displayPost(trialpost);


//        postView.setLikePostController(new LikePostController(new LikePostInteractor(InMemoryPostCommentLikesDataAccessObject.getInstance())));
//
//        JButton likeButton = new JButton("Like");
//        ActionEvent e = new ActionEvent(likeButton, 0, "Like");
//
//        postView.actionPerformed(e);


//        postView.setAnalyzeRecipeController(new AnalyzeRecipeController(new AnalyzeRecipeInteractor(new SpoonacularAPI(), new AnalyzeRecipePresenter(new AnalyzeRecipeViewModel()))));
//        JButton analyzeButton = new JButton("Analyze");
//        ActionEvent e2 = new ActionEvent(analyzeButton, 0, "analyze");
//        postView.actionPerformed(e2);
//
//        JButton saveButton = new JButton("");
//        ActionEvent e3 = new ActionEvent(saveButton, 0, "save");
//        postView.actionPerformed(e3);
//
//        JButton shareButton = new JButton("");
//        ActionEvent e4 = new ActionEvent(shareButton, 0, "share");
//        postView.actionPerformed(e4);
//
//        JButton commentButton = new JButton("");
//        ActionEvent e5 = new ActionEvent(commentButton, 0, "comment");
//        postView.actionPerformed(e5);


    }

}
