package data_access;

import entity.Account;
import entity.Club;
import entity.Comment;
import entity.Post;
import entity.Recipe;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * In-memory implementation of the DAO for storing Post, Comment, Likes data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryPostCommentLikesDataAccessObject implements PostCommentsLikesDataAccessObject{
    HashMap<Long, ArrayList<Comment>> commentsMap = new HashMap<>();
    HashMap<Long, Club> clubsMap = new HashMap<>();
    HashMap<Long, Post> postsMap = new HashMap<>();
    HashMap<Account, ArrayList<Long>> likesMap = new HashMap<>();

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
    public void writePost(long postID, Account user, String title, String postType, String description, HashMap<String,
            ArrayList<String>> contents, ArrayList<String> tags, ArrayList<String> images) {
        Post newPost = new Post(user, postID, title, description);
        newPost.setTitle(title);
        newPost.setType(postType);
        newPost.setLikes(0);
        newPost.setTags(tags);
        newPost.setImageURLs(images);

        if (contents != null) {
            if(postType.equals("recipie")) {
                final ArrayList<String> ingredients = contents.get("ingredients");
                final ArrayList<String> stepsArray = contents.get("steps");
                final StringBuilder steps = new StringBuilder();
                for(String step : stepsArray) {
                    steps.append(step).append("<br>");
                }
                final ArrayList<String> cuisines = contents.get("cuisines");

                Post recipe = new Recipe(newPost, ingredients, steps.toString(), cuisines);
                postsMap.put(postID, recipe);
            }
            else if (postType.equals("other")) {
                // TODO
            }
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

    @Override
    public void writeClub(long clubID, ArrayList<Account> members, String name, String description,
                          ArrayList<Post> posts, ArrayList<String> tags) {
        Club club = new Club(name, description, members, tags, posts);
        clubsMap.put(clubID, club);
    }

    @Override
    public Club getClub(long clubID) {
        if(!clubsMap.containsKey(clubID)) {
            return null;
        }
        else{
            return clubsMap.get(clubID);
        }
    }
}
