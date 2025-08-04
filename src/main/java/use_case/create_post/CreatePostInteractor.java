package use_case.create_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import data_access.UserDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CreatePostInteractor implements CreatePostInputBoundary {
    private final PostCommentsLikesDataAccessObject postDAO;
    private final UserDataAccessObject userDAO;

    public CreatePostInteractor(PostCommentsLikesDataAccessObject postDAO,
                                UserDataAccessObject userDAO) {
        this.postDAO = postDAO;
        this.userDAO = userDAO;

    }

    @Override
    public void execute(CreatePostInputData inputData) {
// format data

        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        map.put("ingredients", inputData.getIngredients());
        map.put("steps", new ArrayList(Arrays.asList(inputData.getSteps())));
        map.put("cuisines", inputData.getTags());
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
                inputData.getTags(),
                inputData.getImages(),
                time
        );

        // associate with user
        userDAO.addPost(
                postID,
                inputData.getUser().getUsername()
        );

    }
}
