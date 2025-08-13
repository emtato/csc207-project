package data_access;

import entity.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DBPostCommentLikesDataAccessObject implements PostCommentsLikesDataAccessObject{
    private static PostCommentsLikesDataAccessObject instance;

    private final String DATABASE_USERNAME = "csc207munchablesusername";
    private final String DATABASE_PASSWORD = "csc207munchablespassword";
    private final String DATA_KEY = "postcommentlikes";
    private static final int CREDENTIAL_ERROR = 401;
    private static final int SUCCESS_CODE = 200;
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";

    private DBPostCommentLikesDataAccessObject() {
    }

    public static PostCommentsLikesDataAccessObject getInstance() {
        if (instance == null) {
            instance = new DBPostCommentLikesDataAccessObject();
        }
        return instance;
    }

    @Override
    public void deletePost(long postID){
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
        try {
            saveJSONObject(data);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * method to reduce duplicate code, retrieves JSONObject of post, comment, like data from database
     *
     * @return JSONObject .
     */
    @NotNull
    private JSONObject getJsonObject() throws DataAccessException {
        // Make an API call to get the user object.
        final String username = DATABASE_USERNAME;
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("http://vm003.teach.cs.toronto.edu:20112/user?username=%s", username))
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                final JSONObject userJSONObject = responseBody.getJSONObject("user");
                final JSONObject data = userJSONObject.getJSONObject("info");
                if (data.has(DATA_KEY)) {
                    return data.getJSONObject(DATA_KEY);
                }
                else{
                    data.put(DATA_KEY, new JSONObject());
                    saveJSONObject(data.getJSONObject(DATA_KEY));
                    return data.getJSONObject(DATA_KEY);
                }
            }
            else {
                throw new DataAccessException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * reduce duplicate code for geting comments
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

    /**
     * helper method for saveJSONObject, retrieves JSONObject of all info from database
     *
     * @return JSONObject .
     */
    private JSONObject getInfo() throws DataAccessException {
        // Make an API call to get the user object.
        final String username = DATABASE_USERNAME;
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("http://vm003.teach.cs.toronto.edu:20112/user?username=%s", username))
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                final JSONObject userJSONObject = responseBody.getJSONObject("user");
                final JSONObject data = userJSONObject.getJSONObject("info");
                return data;
            }
            else {
                throw new DataAccessException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean saveJSONObject(JSONObject data) throws DataAccessException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        // POST METHOD
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, DATABASE_USERNAME);
        requestBody.put(PASSWORD, DATABASE_PASSWORD);
        final JSONObject info = getInfo();
        info.put(DATA_KEY, data);
        requestBody.put("info", info);
        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                .url("http://vm003.teach.cs.toronto.edu:20112/modifyUserInfo")
                .method("PUT", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                return true;
            }
            else if (responseBody.getInt(STATUS_CODE_LABEL) == CREDENTIAL_ERROR) {
                throw new DataAccessException("message could not be found or password was incorrect");
            }
            else {
                throw new DataAccessException("database error: " + responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void addComment(long parentID, Account user, String contents, LocalDateTime timestamp)  {
        // read existing data
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

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

        try {
            saveJSONObject(data);
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public ArrayList<Comment> getComments(long parentID) {
        // read existing data
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

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
        JSONObject data = new JSONObject();
        JSONObject likeMap;

        try {
            data = getJsonObject();
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

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

        try {
            saveJSONObject(data);
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public boolean postIsLiked(Account user, long postID) {
        JSONObject data = new JSONObject();
        JSONObject likeMap;

        try {
            data = new JSONObject();
        }
        catch (JSONException ex) {
            System.out.println(ex.getMessage());
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
    @Override
    public void writePost(long postID, Account user, String title, String postType, String description, HashMap<String,
            ArrayList<String>> contents, ArrayList<String> tags, ArrayList<String> images, String time, ArrayList<Club> clubs) {
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        JSONObject posts;
        if (data.has("posts")) {
            posts = data.getJSONObject("posts"); //posts is mapping between id and the remaining info
        }
        else {
            posts = new JSONObject();
        }
        JSONObject newPost = new JSONObject();
        newPost.put("user", user.getUsername());
        newPost.put("title", title);
        newPost.put("description", description);
        newPost.put("type", postType);
        newPost.put("likes", 0);
        JSONObject contentsJSONObject = new JSONObject(contents);
        newPost.put("contents", contentsJSONObject);
        newPost.put("tags", tags);
        posts.put(String.valueOf(postID), newPost);
        newPost.put("images", images);

        //idk why this is necessary but it doesnt read a.m. it can only recognize AM
        if (time.charAt(time.length() - 4) == 'a') {
            String cutTime = time.substring(0, time.length() - 4) + "AM";
            newPost.put("time", cutTime);
        }
        else {
            String cutTime = time.substring(0, time.length() - 4) + "PM";
            newPost.put("time", cutTime);
        }
        if (postType.equals("review") && contents.containsKey("rating")) {
            ArrayList<String> ratingList = contents.get("rating");
            if (!ratingList.isEmpty()) {
                try {
                    double rating = Double.parseDouble(ratingList.get(0));
                    newPost.put("rating", rating);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        data.put("posts", posts);
        try {
            saveJSONObject(data);
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Post getPost(long postID) {
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        if (!data.has("posts")) {
            return null;
        }

        JSONObject posts = data.getJSONObject("posts");
        String parentPostID = String.valueOf(postID);

        if (!posts.has(parentPostID)) {
            return null;
        }

        try {
            JSONObject postObj = posts.getJSONObject(parentPostID);
            String username = postObj.optString("user", "unknown");
            String title = postObj.optString("title", "Untitled Post");
            String description = postObj.optString("description", "");
            String type = postObj.optString("type", "general");
            String timestamp = postObj.optString("time", null);
            long likeCount = postObj.optLong("likes", 0);

            Account user = new Account(username, "");

            // Images
            ArrayList<String> images = new ArrayList<>();
            if (postObj.has("images")) {
                JSONArray imagesJSONArray = postObj.getJSONArray("images");
                for (int i = 0; i < imagesJSONArray.length(); i++) {
                    images.add(imagesJSONArray.getString(i));
                }
            }

            // Tags
            ArrayList<String> tags = new ArrayList<>();
            if (postObj.has("tags")) {
                JSONArray tagArray = postObj.getJSONArray("tags");
                for (int i = 0; i < tagArray.length(); i++) {
                    tags.add(tagArray.getString(i));
                }
            }

            // Contents map
            HashMap<String, ArrayList<String>> contentsMap = new HashMap<>();
            if (postObj.has("contents")) {
                JSONObject contents = postObj.getJSONObject("contents");
                for (String key : contents.keySet()) {
                    JSONArray arr = contents.getJSONArray(key);
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        list.add(arr.getString(i));
                    }
                    contentsMap.put(key, list);
                }
            }

            // Specialized handling (keep prior behavior for recipe/review)
            if ("recipe".equalsIgnoreCase(type)) {
                ArrayList<String> ingredients = contentsMap.getOrDefault("ingredients", new ArrayList<>());
                String steps = String.join("<br>", contentsMap.getOrDefault("steps", new ArrayList<>()));
                ArrayList<String> cuisines = contentsMap.getOrDefault("cuisines", new ArrayList<>());
                Post base = new Post(user, postID, title, description, images, contentsMap, type, timestamp, tags);
                base.setLikes(likeCount);
                return new Recipe(base, ingredients, steps, cuisines);
            } else if ("review".equalsIgnoreCase(type)) {
                Post base = new Post(user, postID, title, description, images, contentsMap, type, timestamp, tags);
                base.setLikes(likeCount);
                Review review = new Review(user, postID, title, description);
                if (postObj.has("rating")) {
                    try { review.setRating(postObj.getDouble("rating")); } catch (Exception ignored) {}
                }
                return review;
            }

            // Generic / announcement / general post
            Post post = new Post(user, postID, title, description, images, contentsMap, type, timestamp, tags);
            post.setLikes(likeCount);
            return post;
        } catch (Exception e) {
            System.out.println("DEBUG: Error reconstructing post " + postID + ": " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateLikesForPost(long postID, int likeDifference) {
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        if (data.has("posts")) {
            JSONObject posts = data.getJSONObject("posts");
            if (posts.has(String.valueOf(postID))) {
                JSONObject post = posts.getJSONObject(String.valueOf(postID));
                int currentLikes = post.getInt("likes");
                post.put("likes", currentLikes + likeDifference);
                posts.put(String.valueOf(postID), post);
                data.put("posts", posts);
                try {
                    saveJSONObject(data);
                }
                catch (DataAccessException ex) {
                    System.out.println(ex.getMessage());
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
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        if (!data.has("posts")) {
            return new ArrayList<>();
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
        // Append post ID to local file-based clubs (data_storage.json) so club views can load it.
        final String filePath = "src/main/java/data_access/data_storage.json";
        JSONObject dataFile;
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
            dataFile = new JSONObject(content);
        } catch (IOException e) {
            dataFile = new JSONObject();
        }
        if (!dataFile.has("clubs")) {
            throw new DataAccessException("No clubs section found when adding post to club");
        }
        JSONObject clubs = dataFile.getJSONObject("clubs");
        String clubKey = String.valueOf(clubId);
        if (!clubs.has(clubKey)) {
            throw new DataAccessException("Club ID " + clubId + " not found when adding post");
        }
        JSONObject club = clubs.getJSONObject(clubKey);
        JSONArray postsArray = club.has("posts") ? club.getJSONArray("posts") : new JSONArray();
        boolean exists = false;
        for (int i = 0; i < postsArray.length(); i++) {
            if (postsArray.optLong(i) == post.getID()) { exists = true; break; }
        }
        if (!exists) {
            postsArray.put(post.getID());
            System.out.println("DEBUG: (DB) Added post ID " + post.getID() + " to club " + clubId);
        } else {
            System.out.println("DEBUG: (DB) Post ID " + post.getID() + " already present in club " + clubId);
        }
        club.put("posts", postsArray);
        clubs.put(clubKey, club);
        dataFile.put("clubs", clubs);
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath)) {
            writer.write(dataFile.toString(2));
            System.out.println("DEBUG: (DB) Persisted updated club posts for club " + clubId + ": " + postsArray);
        } catch (IOException e) {
            throw new DataAccessException("Error writing club update: " + e.getMessage());
        }
    }
}
