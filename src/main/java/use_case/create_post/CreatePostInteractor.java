package use_case.create_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import data_access.UserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.DBPostCommentLikesDataAccessObject;
import data_access.DBUserDataAccessObject;
import data_access.FilePostCommentLikesDataAccessObject;
import data_access.FileUserDataAccessObject;
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
            System.out.println("DEBUG[CreatePost]: Using Post DAO class = " + postDAO.getClass().getName());
            System.out.println("DEBUG[CreatePost]: Using User DAO class = " + userDAO.getClass().getName());
            System.out.println("DEBUG[CreatePost]: Incoming type=" + inputData.getType() + ", clubId=" + inputData.getClubId());
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
            System.out.println("DEBUG[CreatePost]: Generated postId=" + postId);

            // Create entity (in-memory)
            Post post = new Post(
                    inputData.getUser(),
                    postId,
                    inputData.getTitle(),
                    inputData.getBody(),
                    inputData.getImages(),
                    map,
                    inputData.getType(),
                    now,
                    inputData.getTags()
            );

            // Persist post
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
            System.out.println("DEBUG[CreatePost]: writePost completed for postId=" + postId);

            // Bridge: if using DB post DAO but file-based views expect file posts, mirror into file storage
            try {
                if (postDAO instanceof DBPostCommentLikesDataAccessObject) {
                    PostCommentsLikesDataAccessObject fileDAO = FilePostCommentLikesDataAccessObject.getInstance();
                    if (fileDAO.getPost(postId) == null) {
                        System.out.println("DEBUG[CreatePost]: Mirroring post " + postId + " into file storage for club view consistency");
                        fileDAO.writePost(
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
                    } else {
                        System.out.println("DEBUG[CreatePost]: File storage already has postId=" + postId);
                    }
                }
            } catch (Exception mirrorEx) {
                System.out.println("DEBUG[CreatePost]: Mirror to file storage failed: " + mirrorEx.getMessage());
            }

            // Associate post with user
            userDAO.addPost(postId, inputData.getUser().getUsername());
            System.out.println("DEBUG[CreatePost]: User association added for postId=" + postId + " user=" + inputData.getUser().getUsername());

            // Bridge user posts if using DB user DAO
            try {
                if (userDAO instanceof DBUserDataAccessObject) {
                    FileUserDataAccessObject fileUserDAO = (FileUserDataAccessObject) FileUserDataAccessObject.getInstance();
                    System.out.println("DEBUG[CreatePost]: Mirroring user post association to file user data for user=" + inputData.getUser().getUsername());
                    fileUserDAO.addPost(postId, inputData.getUser().getUsername());
                }
            } catch (Exception userMirrorEx) {
                System.out.println("DEBUG[CreatePost]: Mirror user post failed: " + userMirrorEx.getMessage());
            }

            // Club association
            if (inputData.getClubId() != null) {
                try {
                    postDAO.addPostToClub(post, inputData.getClubId());
                    System.out.println("DEBUG[CreatePost]: addPostToClub succeeded for clubId=" + inputData.getClubId());
                } catch (Exception ex) {
                    System.out.println("DEBUG[CreatePost]: addPostToClub FAILED: " + ex.getMessage());
                }
                Post persisted = postDAO.getPost(postId);
                if (persisted == null) {
                    System.out.println("DEBUG[CreatePost]: Persisted post retrieval FAILED after club association (post missing) postId=" + postId);
                } else {
                    persisted.setClub(true);
                    System.out.println("DEBUG[CreatePost]: Persisted post retrieved and club flag set.");
                }
            } else {
                Post persisted = postDAO.getPost(postId);
                if (persisted == null) {
                    System.out.println("DEBUG[CreatePost]: Persisted post retrieval FAILED (post missing) postId=" + postId);
                } else {
                    persisted.setClub(false);
                }
            }

            // Sanity check: verify presence in storage (posts section if file-based)
            try {
                Post reload = postDAO.getPost(postId);
                System.out.println("DEBUG[CreatePost]: Post reload after all ops => " + (reload == null ? "NULL" : ("found type=" + reload.getType())));
            } catch (Exception ex) {
                System.out.println("DEBUG[CreatePost]: Exception during reload check: " + ex.getMessage());
            }

            CreatePostOutputData outputData = new CreatePostOutputData(post.getID(), timestamp);
            createPostPresenter.prepareSuccessView(outputData);

        }
        catch (Exception e) {
            System.out.println("DEBUG[CreatePost]: Exception during execute: " + e.getClass().getName() + " - " + e.getMessage());
            // Get current timestamp for error case
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            String errorTimestamp = now.format(formatter);

            CreatePostOutputData outputData = new CreatePostOutputData(0L, errorTimestamp);
            createPostPresenter.prepareFailView(outputData);

        }
    }
}
