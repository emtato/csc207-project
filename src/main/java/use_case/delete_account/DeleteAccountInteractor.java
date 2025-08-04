package use_case.delete_account;

import data_access.PostCommentsLikesDataAccessObject;
import entity.User;
import use_case.logout.LogoutInputData;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;
import use_case.logout.LogoutUserDataAccessInterface;

/**
 * The Delete Account Interactor.
 */
public class DeleteAccountInteractor implements DeleteAccountInputBoundary{
    private DeleteAccountUserDataAccessInterface userDataAccessObject;
    private PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject;
    private DeleteAccountOutputBoundary deleteAccountPresenter;

    public DeleteAccountInteractor(DeleteAccountUserDataAccessInterface userDataAccessInterface,
                                   PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject,
                                   DeleteAccountOutputBoundary deleteAccountOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.postCommentsLikesDataAccessObject = postCommentsLikesDataAccessObject;
        this.deleteAccountPresenter = deleteAccountOutputBoundary;
    }

    @Override
    public void execute(DeleteAccountInputData deleteAccountInputData) {
        final String username = deleteAccountInputData.getUsername();
        final User user = userDataAccessObject.get(username);
        for (Long postID : user.getUserPosts()){
            postCommentsLikesDataAccessObject.deletePost(postID);
        }
        userDataAccessObject.deleteAccount(username);

        final DeleteAccountOutputData newlogout = new DeleteAccountOutputData(username);
        deleteAccountPresenter.prepareSuccessView(newlogout);
    }
}
