package data_access;

import entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface PostCommentsLikesDataAccessObject {
    public void addComment(long parentID, Account user, String contents, LocalDateTime timestamp);

    public ArrayList<Comment> getComments(long parentID);

    /**
     * deletes a post given a post ID
     *
     * @param postID post ID to delete
     */
    public void deletePost(long postID);

    /**
     * Deletes a review given a review ID.
     *
     * @param reviewID review ID to delete
     */
    public void deleteReview(long reviewID);

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
     * @param user        user who posted this
     * @param title       title of this post
     * @param postType    String, type of this post (club, event, recipe,..)
     * @param description String description
     * @param contents    HashMap of remaining post information (recipe would have ingredients, steps key-value pairs)
     * @param tags        tags for the post
     * @param images      List of image URLs
     * @param time        timestamp of the post
     * @param clubs      ArrayList of clubs this post is associated with
     */
    public void writePost(long postID, Account user, String title, String postType, String description,
                         HashMap<String, ArrayList<String>> contents, ArrayList<String> tags,
                         ArrayList<String> images, String time, ArrayList<Club> clubs);

    void writeReview(long reviewId, Account user, String title, String body, ArrayList<String> tags, String timestamp);

    /**
     * Get post object from postID.
     *
     * @param postID unique post ID
     * @return Post object
     */
    public Post getPost(long postID);

    /**
     * Get review object from reviewID.
     *
     * @param reviewID unique review ID
     * @return Review object
     */
    Review getReview(long reviewID);

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
    public ArrayList<Long> getAvailableReviews();

    /**
     * Add a post to a specific club
     * @param post The post to add to the club
     * @param clubId The ID of the club to add the post to
     */
    public void addPostToClub(Post post, long clubId) throws DataAccessException;

    /**
     * Delete all posts by a specific user.
     *
     * @param username the username of the user whose posts are to be deleted
     * @param clubsDAO the ClubsDataAccessObject instance for club-related data access
     * @param userDAO  the UserDataAccessObject instance for user-related data access
     */
    public void deleteAllPostsByUser(String username, ClubsDataAccessObject clubsDAO, UserDataAccessObject userDAO);
}
