package use_case.create_review;

import use_case.UserDataAccessInterface;

public interface CreateReviewDataAccessInterface extends UserDataAccessInterface {
    void addReview(long id, String username);
}
