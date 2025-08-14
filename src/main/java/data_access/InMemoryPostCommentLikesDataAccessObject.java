package data_access;

import entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * In-memory implementation of the DAO for storing Post, Comment, Likes data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryPostCommentLikesDataAccessObject implements PostCommentsLikesDataAccessObject {
    private static PostCommentsLikesDataAccessObject instance;

    HashMap<Long, ArrayList<Comment>> commentsMap = new HashMap<>();
    HashMap<Long, Club> clubsMap = new HashMap<>();
    HashMap<Long, Post> postsMap = new HashMap<>();
    HashMap<Long, Review> reviewsMap = new HashMap<>();
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
    public void deletePost(long postID) { postsMap.remove(postID); }

//    public void deleteReview(long reviewID) { reviewsMap.remove(reviewID); }

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

        }
        else if (postType.equals("review")) {
            double rating = -1;
            if (contents.containsKey("rating") && !contents.get("rating").isEmpty()) {
                try {
                    rating = Double.parseDouble(contents.get("rating").get(0));
                }
                catch (NumberFormatException ignored) {
                }
            }
            Review review = new Review(user, postID, title, description);
            review.setRating(rating);

            if (contents.containsKey("restaurant") && !contents.get("restaurant").isEmpty()) {
                review.setRestaurantReview(true);
            }
            else {
                review.setRecipeReview(true);
            }
            post = review;
        }
        else {
            post = new Post(user, postID, title, description);
        }
        post.setType(postType);
        post.setTags(tags);
        post.setImageURLs(images);
        post.setDateTimeFromString(time);


        postsMap.put(postID, post);

        // Store clubs associated with the post
        for (Club club : clubs) {
            clubsMap.put(postID, club);
        }
    }

//    public void writeReview(long reviewID, Account user, String title, String body,
//                            ArrayList<String> tags, String timestamp) {
//        Review review = new Review(user, reviewID, title, body);
//        review.setDateTimeFromString(timestamp);
//        review.setTags(tags);
//
//
//        // Store in in-memory map
//        postsMap.put(reviewID, review);
//    }

    @Override
    public Post getPost(long postID) {
        return postsMap.getOrDefault(postID, null);
    }

//    @Override
//    public Review getReview(long id) {
//        Post p = getPost(id);
//        if (p instanceof Review) {
//            return (Review) p;
//        }
//        return null;
//    }

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

    public ArrayList<Long> getAvailableReviews() {
        return new ArrayList<>(reviewsMap.keySet());
    }

    @Override
    public void addPostToClub(Post post, long clubId) throws DataAccessException {

    }

    // New method to satisfy updated interface: bulk delete of a user's posts
    @Override
    public void deleteAllPostsByUser(String username, ClubsDataAccessObject clubsDAO, UserDataAccessObject userDAO) {
        ArrayList<Long> toRemove = new ArrayList<>();
        for (Long id : postsMap.keySet()) {
            Post p = postsMap.get(id);
            if (p != null && p.getUser() != null && username.equals(p.getUser().getUsername())) {
                toRemove.add(id);
            }
        }
        // Remove posts and associated comments / likes
        for (Long id : toRemove) {
            postsMap.remove(id);
            commentsMap.remove(id);
            for (ArrayList<Long> liked : likesMap.values()) {
                liked.remove(id);
            }
            clubsMap.remove(id);
        }
        // Update user record if provided
        if (userDAO != null) {
            try {
                User u = userDAO.get(username);
                if (u instanceof Account && u.getUserPosts() != null) {
                    u.getUserPosts().removeAll(toRemove);
                    userDAO.save(u);
                }
            } catch (Exception ignored) {}
        }
        // Update clubs via clubsDAO (rebuild without removed posts)
        if (clubsDAO != null) {
            try {
                for (Club club : clubsDAO.getAllClubs()) {
                    boolean changed = false;
                    ArrayList<Post> kept = new ArrayList<>();
                    for (Post p : club.getPosts()) {
                        if (!toRemove.contains(p.getID())) kept.add(p); else changed = true;
                    }
                    if (changed) {
                        clubsDAO.writeClub(club.getId(), club.getMembers(), club.getName(), club.getDescription(), club.getImageUrl(), kept, club.getTags());
                    }
                }
            } catch (Exception ignored) {}
        }
    }
}
