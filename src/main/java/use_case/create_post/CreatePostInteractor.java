package use_case.create_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import data_access.UserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Club;

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

            // Convert club names/IDs to Club objects
            ArrayList<Club> clubs = new ArrayList<>();
            for (String clubName : inputData.getClubs()) {
                clubs.add(new Club(clubName, "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
            }

            long postID = (long) (Math.random() * 1_000_000_000_000L);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            LocalDateTime now = LocalDateTime.now();
            String time = now.format(formatter);

            // write to posts storage
            postDAO.writePost(
                    postID,
                    inputData.getUser(),
                    inputData.getTitle(),
                    inputData.getType(),
                    inputData.getBody(),
                    map,
                    new ArrayList<>(),  // tags
                    inputData.getImages(),
                    time,
                    clubs
            );

            // associate with user
            userDAO.addPost(
                    postID,
                    inputData.getUser().getUsername()
            );

            // Call presenter's success method
            createPostPresenter.prepareSuccessView();
        } catch (Exception e) {
            // If any error occurs during post creation, call presenter's fail method
            createPostPresenter.prepareFailView("Failed to create post: " + e.getMessage());
        }
    }
}
