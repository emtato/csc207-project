package use_case.create_review;

import data_access.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CreateReviewInteractor implements CreateReviewInputBoundary {

    private final PostCommentsLikesDataAccessObject postDAO;
    private final UserDataAccessObject userDAO;
    private final CreateReviewOutputBoundary createReviewPresenter;

    public CreateReviewInteractor(PostCommentsLikesDataAccessObject postDAO,
                                  UserDataAccessObject userDAO,
                                  CreateReviewOutputBoundary createReviewPresenter) {
        this.postDAO = postDAO;
        this.userDAO = userDAO;
        this.createReviewPresenter = createReviewPresenter;
    }

    @Override
    public void execute(CreateReviewInputData inputData) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            String timestamp = now.format(formatter);

            long reviewId = System.currentTimeMillis();

            HashMap<String, ArrayList<String>> contents = new HashMap<>();
            contents.put("rating", new ArrayList<>(Collections.singletonList(Double.toString(inputData.getRating()))));

            ArrayList<String> tags = inputData.getTags() != null ? inputData.getTags() : new ArrayList<>();
            ArrayList<String> images = new ArrayList<>(); // no images

            // Persist using writePost with type "review"
            postDAO.writePost(
                    reviewId,
                    inputData.getUser(),
                    inputData.getTitle(),
                    "review",
                    inputData.getBody(),
                    contents,
                    tags,
                    images,
                    timestamp,
                    new ArrayList<>() // no clubs by default
            );

            // Mirror to file storage (if necessary, so file-based UI code sees it)
            try {
                if (postDAO instanceof DBPostCommentLikesDataAccessObject) {
                    PostCommentsLikesDataAccessObject fileDAO = FilePostCommentLikesDataAccessObject.getInstance();
                    if (fileDAO.getPost(reviewId) == null) {
                        fileDAO.writePost(
                                reviewId,
                                inputData.getUser(),
                                inputData.getTitle(),
                                "review",
                                inputData.getBody(),
                                contents,
                                tags,
                                images,
                                timestamp,
                                new ArrayList<>()
                        );
                    }
                }
            } catch (Exception mirrorEx) {
                System.out.println("DEBUG[CreateReview]: Mirror to file storage failed: " + mirrorEx.getMessage());
            }

            // associate with user
            userDAO.addPost(reviewId, inputData.getUser().getUsername());

            // respond success
            CreateReviewOutputData out = new CreateReviewOutputData(reviewId, timestamp);
            createReviewPresenter.prepareSuccessView(out);

        } catch (Exception e) {
            System.err.println("CreateReviewInteractor failed: " + e.getMessage());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            String timestamp = now.format(formatter);
            CreateReviewOutputData outputData = new CreateReviewOutputData(0L, timestamp);
            createReviewPresenter.prepareFailView(outputData);
        }
    }
}
