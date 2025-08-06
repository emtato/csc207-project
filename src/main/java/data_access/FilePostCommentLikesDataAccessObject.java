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
import java.util.Arrays;
import java.util.HashMap;

public class FilePostCommentLikesDataAccessObject implements PostCommentsLikesDataAccessObject {
    private static PostCommentsLikesDataAccessObject instance;
    private final String filePath = "src/main/java/data_access/data_storage.json";

    private FilePostCommentLikesDataAccessObject() {
    }

    public static PostCommentsLikesDataAccessObject getInstance() {
        if (instance == null) {
            instance = new FilePostCommentLikesDataAccessObject();
        }
        return instance;
    }

    @Override
    public void deletePost(long postID){
        final JSONObject data = getJsonObject();
        JSONObject posts;
        if (data.has("posts")) {
            posts = data.getJSONObject("posts"); //posts is mapping between id and the remaining info
            if (posts.has(String.valueOf(postID))) {
                posts.remove(String.valueOf(postID));
            }
        }
        else {
            posts = new JSONObject();
        }

        data.put("posts", posts);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        }
        catch (IOException e) {
            throw new RuntimeException("i am sad :(", e);
        }
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
        }
        catch (IOException e) {
            data = new JSONObject();
        }
        return data;
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
        }
        else {
            commentsMap = new JSONObject();
        }

        if (commentsMap.has(parentPostID)) {
            comments = commentsMap.getJSONArray(parentPostID);
        }
        else {
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
        }
        catch (IOException e) {
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
        }
        else {
            likeMap = new JSONObject();
        }

        String username = user.getUsername();
        JSONArray likedPosts; //use set later?

        if (likeMap.has(username)) {
            likedPosts = likeMap.getJSONArray(username);
        }
        else {
            likedPosts = new JSONArray();
        }

        if (!likedPosts.toList().contains(postID)) {
            likedPosts.put(postID);
        }

        likeMap.put(username, likedPosts);
        data.put("likes", likeMap);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        }
        catch (IOException e) {
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
        }
        catch (IOException e) {
            return false;
        }

        if (data.has("likes")) {
            likeMap = data.getJSONObject("likes");
        }
        else {
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
        }
        else {
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
        }
        else {
            String cutTime = time.substring(0, time.length() - 4) + "PM";
            newPost.put("time", cutTime);
        }

        posts.put(String.valueOf(postID), newPost);
        data.put("posts", posts);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        }
        catch (IOException e) {
            throw new RuntimeException("i am sad :(", e);
        }
    }

    @Override
    public Post getPost(long postID) {
        JSONObject data = getJsonObject();

        if (!data.has("posts")) {
            return null;
        }

        JSONObject posts = data.getJSONObject("posts");
        String parentPostID = String.valueOf(postID);

        if (!posts.has(parentPostID)) {
            return null;
        }

        JSONObject postObj = posts.getJSONObject(parentPostID);

        String username = postObj.getString("user");
        String title = postObj.getString("title");
        String description = postObj.getString("description");
        // Handle case where likes field doesn't exist
        long likeCount = postObj.optLong("likes", 0);  // Default to 0 if field doesn't exist

        Account user = new Account(username, "password");
        Post post = new Post(user, postID, title, description);
        post.setLikes(likeCount);

        if (postObj.has("images")) {
            JSONArray imagesJSONArray = postObj.getJSONArray("images");
            ArrayList<String> imagesArray = new ArrayList<>();
            for (int i = 0; i < imagesJSONArray.length(); i++) {
                imagesArray.add(imagesJSONArray.getString(i));
            }
            post.setImageURLs(imagesArray);
        }
        if (postObj.has("tags")) {
            JSONArray tagArray = postObj.getJSONArray("tags");
            ArrayList<String> tags = new ArrayList<>();
            for (int i = 0; i < tagArray.length(); i++) {
                tags.add(tagArray.getString(i));
            }
            post.setTags(tags);
        }
        String time = postObj.getString("time");
        post.setDateTimeFromString(time);

        if (postObj.has("contents")) {
            JSONObject contents = postObj.getJSONObject("contents");
            if (postObj.get("type").equals("recipe")) {
                JSONArray ingredients = contents.getJSONArray("ingredients");
                ArrayList<String> ingredientList = new ArrayList<>();
                for (int i = 0; i < ingredients.length(); i++) {
                    ingredientList.add(ingredients.getString(i));
                }

                JSONArray stepsArray = contents.getJSONArray("steps");
                StringBuilder steps = new StringBuilder();
                for (int i = 0; i < stepsArray.length(); i++) {
                    steps.append(stepsArray.getString(i)).append("<br>");
                }

                String cuisines = contents.get("cuisines").toString();
                if (cuisines.equals("Enter cuisine separated by commas if u want")) {
                    cuisines = "";
                }
                return new Recipe(post, ingredientList, steps.toString(), new ArrayList<>(Arrays.asList(cuisines.split(","))));
            }
            else if (postObj.get("type").equals("other")) {
                return new Post(user, postID, title, description);
            }
        }
        return post;
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
                }
                catch (IOException e) {
                    throw new RuntimeException("i am sad :(", e);
                }
            }
            else {
                throw new RuntimeException("bwahhh D: post not found");

            }
        }
        else {
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
