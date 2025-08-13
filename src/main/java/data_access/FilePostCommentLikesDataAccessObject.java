/**
 * Created by Emilia on 2025-07-29!
 * Description: File-based implementation of PostCommentsLikesDataAccessObject
 */
package data_access;

import entity.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class FilePostCommentLikesDataAccessObject implements PostCommentsLikesDataAccessObject {
    private static PostCommentsLikesDataAccessObject instance;
    private final String filePath = "src/main/java/data_access/data_storage.json";
    private static final Logger LOGGER = Logger.getLogger(FilePostCommentLikesDataAccessObject.class.getName());

    private FilePostCommentLikesDataAccessObject() {
    }

    public static PostCommentsLikesDataAccessObject getInstance() {
        if (instance == null) {
            instance = new FilePostCommentLikesDataAccessObject();
        }
        return instance;
    }

    /**
     * method to reduce duplicate code, retrieves JSONObject from file
     *
     * @return JSONObject of data_storage.json.
     */
    @NotNull
    private JSONObject getJsonObject() {
        JSONObject data;
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            data = new JSONObject(content);
        } catch (IOException e) {
            data = new JSONObject();
        }
        return data;
    }

    @Override
    public void deletePost(long postID) {
        final JSONObject data = getJsonObject();
        JSONObject posts;
        if (data.has("posts")) {
            posts = data.getJSONObject("posts");
            if (posts.has(String.valueOf(postID))) {
                posts.remove(String.valueOf(postID));
            }
        } else {
            posts = new JSONObject();
        }

        data.put("posts", posts);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete post: " + e.getMessage(), e);
        }
    }

    public void deleteReview(long reviewID) {
        final JSONObject data = getJsonObject();
        JSONObject reviews;
        if (data.has("reviews")) {
            reviews = data.getJSONObject("reviews");
            if (reviews.has(String.valueOf(reviewID))) {
                reviews.remove(String.valueOf(reviewID));
            }
        } else {
            reviews = new JSONObject();
        }

        data.put("reviews", reviews);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete review: " + e.getMessage(), e);
        }
    }
    /**
     * reduce duplicate code for get comments in data_storage.JSON
     *
     * @param data     JSONObject data from entire file
     * @param parentID post ID of the post the comment is posted on
     * @return both the list of comments of the given post and a map of all the posts:comments array
     */
    private ArrayList<Object> getCommentArray(JSONObject data, long parentID) {
        String parentPostID = String.valueOf(parentID);
        JSONObject commentsMap;
        JSONArray comments;
        ArrayList<Object> both = new ArrayList<>();
        if (data.has("comments")) {
            commentsMap = data.getJSONObject("comments");
        } else {
            commentsMap = new JSONObject();
        }

        if (commentsMap.has(parentPostID)) {
            comments = commentsMap.getJSONArray(parentPostID);
        } else {
            comments = new JSONArray();
        }
        both.add(comments);
        both.add(commentsMap);
        return both;
    }

    @Override
    public void addComment(long parentID, Account user, String contents, LocalDateTime timestamp) {
        // read existing data
        JSONObject data = getJsonObject();

        ArrayList<Object> list = getCommentArray(data, parentID);
        JSONArray comments = (JSONArray) list.get(0);
        JSONObject commentsMap = (JSONObject) list.get(1);

        //add new comment
        JSONObject comment = new JSONObject();
        comment.put("user", user.getUsername());
        comment.put("content", contents);
        comment.put("time", timestamp.toString());
        comments.put(comment);

        commentsMap.put(String.valueOf(parentID), comments);
        data.put("comments", commentsMap);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("i am sad :(", e);
        }
    }

    @Override
    public ArrayList<Comment> getComments(long parentID) {
        // read existing data
        JSONObject data = getJsonObject();

        ArrayList<Object> list = getCommentArray(data, parentID);
        JSONArray comments = (JSONArray) list.get(0);

        ArrayList<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < comments.length(); i++) {
            JSONObject obj = comments.getJSONObject(i);
            Account user = new Account((String) obj.get("user"), "passwod");
            String content = obj.getString("content");
            LocalDateTime time = LocalDateTime.parse(obj.getString("time"));
            commentList.add(new Comment(user, content, time, 0));
        }
        return commentList;
    }

    @Override
    public void addLike(Account user, long postID) {
        JSONObject data;
        JSONObject likeMap;

        data = getJsonObject();

        if (data.has("likes")) {
            likeMap = data.getJSONObject("likes");
        } else {
            likeMap = new JSONObject();
        }

        String username = user.getUsername();
        JSONArray likedPosts; //use set later?

        if (likeMap.has(username)) {
            likedPosts = likeMap.getJSONArray(username);
        } else {
            likedPosts = new JSONArray();
        }

        if (!likedPosts.toList().contains(postID)) {
            likedPosts.put(postID);
        }

        likeMap.put(username, likedPosts);
        data.put("likes", likeMap);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("i am sad :(", e);
        }
    }

    @Override
    public boolean postIsLiked(Account user, long postID) {
        JSONObject data;
        JSONObject likeMap;

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            data = new JSONObject(content);
        } catch (IOException e) {
            return false;
        }

        if (data.has("likes")) {
            likeMap = data.getJSONObject("likes");
        } else {
            return false;
        }

        String username = user.getUsername();
        if (!likeMap.has(username)) {
            return false;
        }

        JSONArray likedPosts = likeMap.getJSONArray(username);
        return likedPosts.toList().contains(postID);
    }

    /**
     * Not fully done implementing/ not capable of posting everything but a start. writes given post information to JSON.
     *
     * @param postID      unique identifier for the post
     * @param user        user who posted dis
     * @param title       title of dis post
     * @param postType    String, type of dis post (club, event, recipe,..)
     * @param description String description
     * @param contents    HashMap of remaining post information (recipe would have ingredients, steps key-value pairs)
     * @param tags        tags associated with the post
     * @param images      list of image paths
     * @param time        timestamp of when the post was created
     * @param clubs       ArrayList of clubs this post is associated with
     */
    @Override
    public void writePost(long postID, Account user, String title, String postType, String description,
                         HashMap<String, ArrayList<String>> contents, ArrayList<String> tags,
                         ArrayList<String> images, String time, ArrayList<Club> clubs) {
        JSONObject data = getJsonObject();
        JSONObject posts;
        if (data.has("posts")) {
            posts = data.getJSONObject("posts");
        } else {
            posts = new JSONObject();
        }
        JSONObject newPost = new JSONObject();
        newPost.put("user", user.getUsername());
        newPost.put("title", title);
        newPost.put("type", postType);
        newPost.put("description", description);
        newPost.put("contents", contents);
        newPost.put("tags", tags);
        newPost.put("images", images);

        // Ensure a likes field exists so later updates don't fail
        if (!newPost.has("likes")) {
            newPost.put("likes", 0);
        }

        // Convert clubs to JSON array of club objects
        JSONArray clubsArray = new JSONArray();
        for (Club club : clubs) {
            JSONObject clubObj = new JSONObject();
            clubObj.put("name", club.getName());
            clubObj.put("description", club.getDescription());
            clubObj.put("members", new JSONArray(club.getMembers()));
            clubObj.put("foodPreferences", new JSONArray(club.getFoodPreferences()));
            clubObj.put("posts", new JSONArray());  // Empty posts array as these are stored separately
            clubObj.put("id", club.getId());
            clubObj.put("tags", new JSONArray(club.getTags()));
            clubsArray.put(clubObj);
        }
        newPost.put("clubs", clubsArray);

        //idk why this is necessary but it doesnt read a.m. it can only recognize AM
        if (time.charAt(time.length() - 4) == 'a') {
            String cutTime = time.substring(0, time.length() - 4) + "AM";
            newPost.put("time", cutTime);
        } else {
            String cutTime = time.substring(0, time.length() - 4) + "PM";
            newPost.put("time", cutTime);
        }

        if ("review".equals(postType) && contents.containsKey("rating")) {
            ArrayList<String> ratingList = contents.get("rating");
            if (!ratingList.isEmpty()) {
                try {
                    double rating = Double.parseDouble(ratingList.get(0));
                    newPost.put("rating", rating);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        posts.put(String.valueOf(postID), newPost);
        data.put("posts", posts);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("i am sad :(", e);
        }
    }

    /**
     * Writes a review to the JSON file.
     *
     * @param reviewID unique identifier for the review
     * @param user     account posting the review
     * @param title    review title
     * @param body     review body/content
     * @param tags     tags associated with the review
     * @param time     timestamp of review creation
     */
    public void writeReview(long reviewID, Account user, String title, String body,
                            ArrayList<String> tags, String time) {
        JSONObject data = getJsonObject();
        JSONObject posts = data.has("posts") ? data.getJSONObject("posts") : new JSONObject();

        JSONObject newReview = new JSONObject();
        newReview.put("user", user.getUsername());
        newReview.put("title", title);
        newReview.put("description", body);
        newReview.put("type", "review");
        newReview.put("likes", 0);
        newReview.put("tags", tags);
        newReview.put("images", new JSONArray()); // optional
        newReview.put("rating", -1); // default rating

        // Adjust AM/PM
        if (time.charAt(time.length() - 4) == 'a') {
            newReview.put("time", time.substring(0, time.length() - 4) + "AM");
        } else {
            newReview.put("time", time.substring(0, time.length() - 4) + "PM");
        }

        posts.put(String.valueOf(reviewID), newReview);
        data.put("posts", posts);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write review: " + e.getMessage(), e);
        }
    }

    @Override
    public Post getPost(long id) {
        System.out.println("DEBUG: Attempting to load post with ID: " + id);
        JSONObject data = getJsonObject();

        // Create posts section if it doesn't exist
        if (!data.has("posts")) {
            System.out.println("DEBUG: Creating posts section in data storage");
            data.put("posts", new JSONObject());
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(data.toString(2));
            } catch (IOException e) {
                System.out.println("DEBUG: Error creating posts section: " + e.getMessage());
            }
            return null;
        }

        JSONObject posts = data.getJSONObject("posts");
        String postId = String.valueOf(id);

        if (!posts.has(postId)) {
            System.out.println("DEBUG: Post ID " + id + " not found in posts data. This could mean the post was deleted or not properly saved.");
            // Remove the post ID from any clubs that reference it
            cleanupDeletedPost(id);
            return null;
        }

        try {
            JSONObject postData = posts.getJSONObject(postId);
            System.out.println("DEBUG: Found post data for ID: " + id + ", keys: " + postData.keySet());

            // Get basic post info
            String title = postData.optString("title", "Untitled Post");
            String description = postData.optString("description", "");
            String username = postData.optString("user", "unknown");

            // Create Account object for the user
            Account postUser = new Account(username, "");

            // Get image URLs
            ArrayList<String> imageURLs = new ArrayList<>();
            if (postData.has("images")) {
                JSONArray imagesJson = postData.getJSONArray("images");
                for (int i = 0; i < imagesJson.length(); i++) {
                    imageURLs.add(imagesJson.getString(i));
                }
            }

            // Handle contents map
            HashMap<String, ArrayList<String>> contents = new HashMap<>();
            if (postData.has("contents")) {
                JSONObject contentsJson = postData.getJSONObject("contents");
                for (String key : contentsJson.keySet()) {
                    JSONArray valueArray = contentsJson.getJSONArray(key);
                    ArrayList<String> values = new ArrayList<>();
                    for (int i = 0; i < valueArray.length(); i++) {
                        values.add(valueArray.getString(i));
                    }
                    contents.put(key, values);
                }
            }

            // Get tags
            ArrayList<String> tags = new ArrayList<>();
            if (postData.has("tags")) {
                JSONArray tagsJson = postData.getJSONArray("tags");
                for (int i = 0; i < tagsJson.length(); i++) {
                    tags.add(tagsJson.getString(i));
                }
            }

            // Get timestamp
            String timestamp = postData.optString("time", null);

            // Create post with all required parameters
            Post post = new Post(postUser, id, title, description, imageURLs, contents,
                               postData.optString("type", ""), timestamp, tags);

            System.out.println("DEBUG: Successfully created Post object for ID: " + id);
            return post;

        } catch (Exception e) {
            System.out.println("DEBUG: Error loading post " + id + ": " + e.getMessage());
            return null;
        }
    }

    @Override
    public Review getReview(long id) {
        JSONObject data = getJsonObject();

        if (!data.has("posts")) {
            return null;
        }

        JSONObject posts = data.getJSONObject("posts");
        String postId = String.valueOf(id);
        if (!posts.has(postId)) {
            return null;
        }

        try {
            JSONObject postData = posts.getJSONObject(postId);

            if (!"review".equals(postData.optString("type", ""))) {
                return null;
            }

            String title = postData.optString("title", "Untitled Review");
            String description = postData.optString("description", "");
            String username = postData.optString("user", "unknown");

            Account postUser = new Account(username, "");

            Review review = new Review(postUser, id, title, description);

            if (postData.has("rating")) {
                double rating = postData.optDouble("rating", -1);
                if (rating >= 0 && rating <= 5) {
                    review.setRating(rating);
                }
            } else {
                if (postData.has("contents")) {
                    try {
                        JSONObject contents = postData.getJSONObject("contents");
                        if (contents.has("rating")) {
                            JSONArray arr = contents.getJSONArray("rating");
                            if (!arr.isEmpty()) {
                                double rating = Double.parseDouble(arr.getString(0));
                                review.setRating(rating);
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }

            if (postData.has("likes")) {
                review.setLikes(postData.optInt("likes", 0));
            }
            return review;
        } catch (Exception e) {
            System.err.println("Error reading review " + id + ": " + e.getMessage());
            return null;
        }
    }

    private void cleanupDeletedPost(long postId) {
        JSONObject data = getJsonObject();
        if (!data.has("clubs")) {
            return;
        }

        boolean madeChanges = false;
        JSONObject clubs = data.getJSONObject("clubs");
        for (String clubId : clubs.keySet()) {
            JSONObject club = clubs.getJSONObject(clubId);
            if (club.has("posts")) {
                JSONArray posts = club.getJSONArray("posts");
                JSONArray newPosts = new JSONArray();
                for (int i = 0; i < posts.length(); i++) {
                    if (posts.getLong(i) != postId) {
                        newPosts.put(posts.getLong(i));
                    } else {
                        madeChanges = true;
                        System.out.println("DEBUG: Removing deleted post " + postId + " from club " + clubId);
                    }
                }
                club.put("posts", newPosts);
            }
        }

        if (madeChanges) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(data.toString(2));
                System.out.println("DEBUG: Updated clubs to remove references to deleted post " + postId);
            } catch (IOException e) {
                System.out.println("DEBUG: Error updating clubs data: " + e.getMessage());
            }
        }
    }

    @Override
    public void updateLikesForPost(long postID, int likeDifference) {
        JSONObject data = getJsonObject();
        if (data.has("posts")) {
            JSONObject posts = data.getJSONObject("posts");
            if (posts.has(String.valueOf(postID))) {
                JSONObject post = posts.getJSONObject(String.valueOf(postID));
                int currentLikes = post.getInt("likes");
                post.put("likes", currentLikes + likeDifference);
                posts.put(String.valueOf(postID), post);
                data.put("posts", posts);
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(data.toString(2));
                } catch (IOException e) {
                    throw new RuntimeException("i am sad :(", e);
                }
            } else {
                throw new RuntimeException("bwahhh D: post not found");

            }
        } else {
            throw new RuntimeException("bwahhh D: post not found");

        }
    }

    @Override
    public ArrayList<Long> getAvailablePosts() {
        JSONObject data = getJsonObject();

        if (!data.has("posts")) {
            return null;
        }

        JSONObject posts = data.getJSONObject("posts");
        ArrayList<Long> postIDs = new ArrayList<>();
        for (String key : posts.keySet()) {
            long postID = Long.parseLong(key);
            postIDs.add(postID);
        }
        return postIDs;
    }

    public ArrayList<Long> getAvailableReviews() {
        JSONObject data = getJsonObject();

        if (!data.has("reviews")) {
            return new ArrayList<>();
        }

        JSONObject reviews = data.getJSONObject("reviews");
        ArrayList<Long> reviewIDs = new ArrayList<>();
        for (String key : reviews.keySet()) {
            long reviewID = Long.parseLong(key);
            reviewIDs.add(reviewID);
        }
        return reviewIDs;
    }

    @Override
    public void addPostToClub(Post post, long clubId) throws DataAccessException {
        final JSONObject data = getJsonObject();

        // Get the clubs section
        JSONObject clubs = data.getJSONObject("clubs");

        // Find the specific club
        JSONObject club = clubs.getJSONObject(String.valueOf(clubId));

        // Get the club's posts array
        JSONArray posts = club.getJSONArray("posts");

        // Add the post ID to the club's posts array if not already present
        boolean exists = false;
        for (int i = 0; i < posts.length(); i++) {
            if (posts.getLong(i) == post.getID()) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            posts.put(post.getID());
        }

        // Save back to file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new DataAccessException("Error writing to file: " + e.getMessage());
        }
    }
}
