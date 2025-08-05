package data_access;

import entity.Account;
import entity.Club;
import entity.Comment;
import entity.Post;
import entity.Recipe;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * In-memory implementation of the DAO for storing Post, Comment, Likes data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryPostCommentLikesDataAccessObject implements PostCommentsLikesDataAccessObject{
    private static PostCommentsLikesDataAccessObject instance;

    HashMap<Long, ArrayList<Comment>> commentsMap = new HashMap<>();
    HashMap<Long, Club> clubsMap = new HashMap<>();
    HashMap<Long, Post> postsMap = new HashMap<>();
    HashMap<Account, ArrayList<Long>> likesMap = new HashMap<>();

    private InMemoryPostCommentLikesDataAccessObject() {
    }

    public static PostCommentsLikesDataAccessObject getInstance() {
        if (instance == null) {
            instance = new InMemoryPostCommentLikesDataAccessObject();
        }
        return instance;
    }

    @Override
    public void deletePost(long postID){
        postsMap.remove(postID);
    }

    @Override
    public void addComment(long parentID, Account user, String contents, LocalDateTime timestamp) {
        Comment comment = new Comment(user, contents, timestamp, 0);
        if (commentsMap.containsKey(parentID)) {
            commentsMap.get(parentID).add(comment);
        }
        else {
            final ArrayList<Comment> comments = new ArrayList<>();
            comments.add(comment);
            commentsMap.put(parentID, comments);
        }
    }

    @Override
    public ArrayList<Comment> getComments(long parentID) {
        return commentsMap.get(parentID);
    }

    @Override
    public void addLike(Account user, long postID) {
        if (likesMap.containsKey(user)) {
            likesMap.get(user).add(postID);
        }
        else {
            final ArrayList<Long> postIDs = new ArrayList<>();
            postIDs.add(postID);
            likesMap.put(user, postIDs);
        }
    }

    @Override
    public boolean postIsLiked(Account user, long postID) {
        if (likesMap.containsKey(user)) {
            return likesMap.get(user).contains(postID);
        }
        else {
            return false;
        }
    }

    @Override
    public void writePost(long postID, Account user, String title, String postType,
                        String description, HashMap<String, ArrayList<String>> contents,
                        ArrayList<String> tags, ArrayList<String> images, String time,
                        ArrayList<Club> clubs) {
        Post post;
        if (postType.equals("recipe")) {
            ArrayList<String> ingredients = contents.get("ingredients");
            String steps = String.join("<br>", contents.get("steps"));
            String cuisines = String.join(",", contents.getOrDefault("cuisines", new ArrayList<>()));
            post = new Recipe(new Post(user, postID, title, description), ingredients, steps,
                            new ArrayList<>(Arrays.asList(cuisines.split(","))));
        } else {
            post = new Post(user, postID, title, description);
        }

        post.setTags(tags);
        post.setImageURLs(images);
        post.setDateTimeFromString(time);

        postsMap.put(postID, post);

        // Store clubs associated with the post
        for (Club club : clubs) {
            clubsMap.put(postID, club);
        }
    }

    @Override
    public Post getPost(long postID) {
        if (!postsMap.containsKey(postID)) {
            return null;
        }
        else{
            return postsMap.get(postID);
        }
    }

    @Override
    public void updateLikesForPost(long postID, int likeDifference) {
       if (postsMap.containsKey(postID)) {
           Post post = postsMap.get(postID);
           post.setLikes(post.getLikes() + likeDifference);
       }
        else {
            throw new RuntimeException("post not found");
        }
    }

    @Override
    public ArrayList<Long> getAvailablePosts() {
        return new ArrayList<>(postsMap.keySet());
    }

}
