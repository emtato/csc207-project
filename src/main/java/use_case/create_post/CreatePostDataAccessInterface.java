package use_case.create_post;

import use_case.UserDataAccessInterface;

public interface CreatePostDataAccessInterface extends UserDataAccessInterface {
    void addPost(long id, String username);
}
