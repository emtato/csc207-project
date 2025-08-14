/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */
package use_case.create_post;
import data_access.UserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
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

    public CreatePostInteractor(PostCommentsLikesDataAccessObject postDAO, UserDataAccessObject
            userDAO, CreatePostOutputBoundary createPostPresenter) {
        this.postDAO = postDAO;
        this.userDAO = userDAO;
        this.createPostPresenter = createPostPresenter;
    }

    @Override
    public void execute(CreatePostInputData inputData) {
        // Get current timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        String timestamp = now.format(formatter);
        try {
            // format data
            HashMap<String, ArrayList<String>> map = new HashMap<>();
            map.put("ingredients", inputData.getIngredients());
            map.put("steps", new ArrayList<>(Arrays.asList(inputData.getSteps())));
            map.put("cuisines", inputData.getTags());

            // Generate unique ID for the post
            long postId = System.currentTimeMillis();

            // Create entity (in-memory)
            Post post = new Post(inputData.getUser(), postId, inputData.getTitle(), inputData.getBody(),
                    inputData.getImages(), map, inputData.getType(), now, inputData.getTags());

            // Persist post
            postDAO.writePost(postId, inputData.getUser(), inputData.getTitle(), inputData.getType(),
                    inputData.getBody(), map, inputData.getTags(), inputData.getImages(), timestamp, inputData.getClubs());

            // Associate post with user
            userDAO.addPost(postId, inputData.getUser().getUsername());

            // Club association
            if (inputData.getClubId() != null) {
                try {
                    postDAO.addPostToClub(post, inputData.getClubId());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            CreatePostOutputData outputData = new CreatePostOutputData(post.getID(), timestamp);
            createPostPresenter.prepareSuccessView(outputData);
        }
        catch (Exception e) {
            // Get current timestamp for error case
            CreatePostOutputData outputData = new CreatePostOutputData(0L, timestamp);
            createPostPresenter.prepareFailView(outputData);

        }
    }
}
