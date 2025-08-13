package use_case.create_review;

import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
            // Get timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            String timestamp = now.format(formatter);

            // Generate unique ID for review
            long reviewId = System.currentTimeMillis();

            // Create Review entity
            Review review = new Review(
                    inputData.getUser(),
                    reviewId,
                    inputData.getTitle(),
                    inputData.getBody()
            );
            review.setRating(inputData.getRating());

            // Persist review
            postDAO.writeReview(reviewId,
                    inputData.getUser(),
                    inputData.getTitle(),
                    inputData.getBody(),
                    inputData.getTags(),
                    timestamp
            );

            // Associate review with user
            userDAO.addPost(reviewId, inputData.getUser().getUsername());

            // Success
            CreateReviewOutputData outputData = new CreateReviewOutputData(reviewId, timestamp);
            createReviewPresenter.prepareSuccessView(outputData);

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
