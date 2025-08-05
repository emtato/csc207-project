package data_access;

import entity.*;
import java.time.LocalDate;
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
            ArrayList<String>> contents, ArrayList<String> tags, ArrayList<String> images, String time) {
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

        JSONObject postObj = posts.getJSONObject(parentPostID);

        String username = postObj.getString("user");
        String title = postObj.getString("title");
        String description = postObj.getString("description");
        long likeCount = postObj.getLong("likes");

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
                String steps = "";
                for (int i = 0; i < stepsArray.length(); i++) {
                    steps += stepsArray.getString(i) + "<br>";
                }

                String cuisines = contents.get("cuisines").toString();
                if (cuisines.equals("Enter cuisine separated by commas if u want")) {
                    cuisines = "";
                }
                Recipe rep = new Recipe(post, ingredientList, steps, new ArrayList<>(Arrays.asList(cuisines.split(","))));
                return rep;
                //early return since its a recipe we dont wanna return the post one, eventually probably all should be early returns
            }
            else if (postObj.get("type").equals("event")) {
                JSONArray locationArray = contents.getJSONArray("location");
                String location = locationArray.getString(0);
                JSONArray dateArray = contents.getJSONArray("date");
                String dateString = dateArray.getString(0);
                LocalDate date = LocalDate.parse(dateString);
                JSONArray participantsArray = contents.getJSONArray("participants");
                ArrayList<String> participantsList = new ArrayList<>();
                for (int i = 0; i < participantsArray.length(); i++) {
                    participantsList.add(participantsArray.getString(i));
                }
                JSONArray foodPreferences = contents.getJSONArray("foodPreferences");
                ArrayList<String> foodPreferencesList = new ArrayList<>();
                for (int i = 0; i < foodPreferences.length(); i++) {
                    foodPreferencesList.add(foodPreferences.getString(i));
                }

                Event event = new Event(post, location, date, participantsList, foodPreferencesList);
            }
        }
        return post;
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
}
