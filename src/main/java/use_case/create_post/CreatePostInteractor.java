package use_case.create_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import data_access.UserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CreatePostInteractor implements CreatePostInputBoundary {
    private final PostCommentsLikesDataAccessObject postDAO;
    private final UserDataAccessObject userDAO;
    private final CreatePostOutputBoundary createPostPresenter;

    public CreatePostInteractor(PostCommentsLikesDataAccessObject postDAO,
                              UserDataAccessObject userDAO,
                              CreatePostOutputBoundary createPostPresenter) {
        this.postDAO = postDAO;
        this.userDAO = userDAO;
        this.createPostPresenter = createPostPresenter;
    }

    @Override
    public void execute(CreatePostInputData inputData) {
        try {
            // format data
            HashMap<String, ArrayList<String>> map = new HashMap<>();
            map.put("ingredients", inputData.getIngredients());
            map.put("steps", new ArrayList<>(Arrays.asList(inputData.getSteps())));
            map.put("cuisines", inputData.getTags());

            // Get current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            String timestamp = now.format(formatter);

            // Generate unique ID for the post
            long postId = System.currentTimeMillis();

            // Create the post with type parameter
            Post post = new Post(
                inputData.getUser(),
                postId,
                inputData.getTitle(),
                inputData.getBody(),
                inputData.getImages(),
                inputData.getType()
            );
            post.setTags(inputData.getTags());

            // Save the post using writePost
            postDAO.writePost(
                postId,
                inputData.getUser(),
                inputData.getTitle(),
                inputData.getType(),
                inputData.getBody(),
                map,
                inputData.getTags(),
                inputData.getImages(),
                timestamp,
                inputData.getClubs()
            );

            // If this is a club post, add it to the club's posts
            if (inputData.getClubId() != null) {
                postDAO.addPostToClub(post, inputData.getClubId());
            }

            CreatePostOutputData outputData = new CreatePostOutputData(post.getID(), timestamp);
            createPostPresenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            // Get current timestamp for error case
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            String errorTimestamp = now.format(formatter);

            CreatePostOutputData outputData = new CreatePostOutputData(0L, errorTimestamp);
            createPostPresenter.prepareFailView(outputData);
        }
    }
}
