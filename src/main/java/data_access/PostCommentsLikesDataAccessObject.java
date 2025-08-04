package data_access;

import entity.Account;
import entity.Club;
import entity.Comment;
import entity.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public interface PostCommentsLikesDataAccessObject {
    public void addComment(long parentID, Account user, String contents, LocalDateTime timestamp);

    public ArrayList<Comment> getComments(long parentID);

    /**
     * keep a record of which account has liked which post
     *
     * @param user   current logged in user
     * @param postID post ID currently being liked
     */
    public void addLike(Account user, long postID);

    /**
     * function to determine if the current user has liked post postID to keep track and avoid spamming likes
     *
     * @param user   current logged in user
     * @param postID id of post being accessed
     * @return boolean to indicate if the current user has liked postID
     */
    public boolean postIsLiked(Account user, long postID);

    /**
     * not fully done implementing/ not capable of posting everything but a start. writes given post information to JSON.
     *
     * @param user        user who posted dis
     * @param title       title of dis post
     * @param postType    String, type of dis post (club, event, recipe,..)
     * @param description String description
     * @param contents    HashMap of remaining post information (recipe would have an ingredients, steps key-value pairs)
     * @param tags        tasg
     * @param time
     */
    public void writePost(long postID, Account user, String title, String postType, String description, HashMap<String, ArrayList<String>> contents, ArrayList<String> tags, ArrayList<String> images, String time);

    /**
     * Get post object from postID.
     *
     * @param postID unique post ID
     * @return Post object
     */
    public Post getPost(long postID);

    /**
     * function to update a post's likes in database. The system to update likes should be used when the
     * user likes or unlikes a post, and thus -1 or 1 is passed as likeDifference
     *
     * @param postID         ID of the post we are updating likes on
     * @param likeDifference integer -1 or 1.
     */
    public void updateLikesForPost(long postID, int likeDifference);

    /**
     * get a list of all postID's stored in database.
     *
     * @return ArrayList of long
     */
    public ArrayList<Long> getAvailablePosts();
}
