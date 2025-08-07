package use_case.login;

import java.util.HashMap;
import java.util.Map;

import data_access.PostCommentsLikesDataAccessObject;
import entity.Post;
import entity.User;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.postCommentsLikesDataAccessObject = postCommentsLikesDataAccessObject;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();
        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final User user = userDataAccessObject.get(loginInputData.getUsername());

                final Map<Long, Post> posts = new HashMap<>();
                for (Long postId : user.getUserPosts()) {
                    final Post post = postCommentsLikesDataAccessObject.getPost(postId);
                    posts.put(postId, post);
                }

                userDataAccessObject.setCurrentUsername(user.getUsername());
                final LoginOutputData loginOutputData = new LoginOutputData(user.getUsername(), user.getDisplayName(),
                        user.getProfilePictureUrl(), user.getBio(), user.getNumFollowers(), user.getNumFollowing(),
                        posts, user.isPublic(), user.isNotificationsEnabled(), false, user.getPassword());
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}
