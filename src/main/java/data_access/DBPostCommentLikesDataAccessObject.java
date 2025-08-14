package data_access;

import entity.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    // Simple in-memory cache to mitigate rate limit issues
    private static final long CACHE_TTL_MS = 60_000; // 1 minute TTL
    private static class CacheEntry { Post post; long time; CacheEntry(Post p){this.post=p; this.time=System.currentTimeMillis();} }
    private static final Map<Long, CacheEntry> POST_CACHE = new ConcurrentHashMap<>();
    private static boolean isRateLimitMessage(String msg){
        if (msg == null) return false;
        String lower = msg.toLowerCase();
        return lower.contains("too many requests") || lower.contains("rate limit");
    }
    private static Post getCachedIfFresh(long id){
        CacheEntry ce = POST_CACHE.get(id);
        if (ce == null) return null;
        if (System.currentTimeMillis() - ce.time > CACHE_TTL_MS) { POST_CACHE.remove(id); return null; }
        return ce.post;
    }

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

//    public void deleteReview(long reviewID){
//        JSONObject data = new JSONObject();
//        try {
//            data = getJsonObject();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        JSONObject reviews;
//        if (data.has("reviews")) {
//            reviews = data.getJSONObject("reviews"); //reviews is mapping between id and the remaining info
//            if (reviews.has(String.valueOf(reviewID))) {
//                reviews.remove(String.valueOf(reviewID));
//            }
//        }
//        else {
//            reviews = new JSONObject();
//        }
//
//        data.put("reviews", reviews);
//        try {
//            saveJSONObject(data);
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }

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
            try {
                // Build a Post object for cache (lightweight reconstruction)
                HashMap<String, ArrayList<String>> copyMap = new HashMap<>(contents);
                Post cached = new Post(user, postID, title, description, images, copyMap, postType, time, tags);
                POST_CACHE.put(postID, new CacheEntry(cached));
            } catch (Exception ignore) {}
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }

//    public void writeReview(long reviewID, Account user, String title, String body,
//                            ArrayList<String> tags, String time) {
//        JSONObject data;
//        try {
//            data = getJsonObject();
//        } catch (DataAccessException ex) {
//            System.out.println(ex.getMessage());
//            data = new JSONObject();
//        }
//
//        JSONObject posts = data.has("posts") ? data.getJSONObject("posts") : new JSONObject();
//
//        JSONObject newReview = new JSONObject();
//        newReview.put("user", user.getUsername());
//        newReview.put("title", title);
//        newReview.put("description", body);
//        newReview.put("type", "review");
//        newReview.put("likes", 0);
//        newReview.put("tags", tags);
//        newReview.put("images", new ArrayList<>()); // optional images
//        newReview.put("rating", -1); // default, can be updated later
//
//        // Adjust AM/PM formatting
//        if (time.charAt(time.length() - 4) == 'a') {
//            newReview.put("time", time.substring(0, time.length() - 4) + "AM");
//        } else {
//            newReview.put("time", time.substring(0, time.length() - 4) + "PM");
//        }
//
//        posts.put(String.valueOf(reviewID), newReview);
//        data.put("posts", posts);
//
//        try {
//            saveJSONObject(data);
//            // Optionally cache the review
//            Review cached = new Review(user, reviewID, title, body);
//            POST_CACHE.put(reviewID, new CacheEntry(cached));
//        } catch (DataAccessException ex) {
//            System.out.println(ex.getMessage());
//        }
//    }


    @Override
    public Post getPost(long postID) {
        // First attempt cache
        Post cached = getCachedIfFresh(postID);
        if (cached != null) {
            return cached;
        }
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (RuntimeException rte) {
            // On rate limit / transient failure, return stale cache (even if expired) or null WITHOUT implying deletion
            if (isRateLimitMessage(rte.getMessage())) {
                Post stale = POST_CACHE.containsKey(postID) ? POST_CACHE.get(postID).post : null;
                if (stale != null) {
                    System.out.println("DEBUG[DBPosts]: Rate limit hit; returning stale cached post " + postID);
                    return stale;
                }
                System.out.println("DEBUG[DBPosts]: Rate limit; no cache for post " + postID);
                return null; // signal transient miss
            }
            System.out.println(rte.getMessage());
        }
        catch (DataAccessException ex) {
            if (isRateLimitMessage(ex.getMessage())) {
                Post stale = POST_CACHE.containsKey(postID) ? POST_CACHE.get(postID).post : null;
                if (stale != null) {
                    System.out.println("DEBUG[DBPosts]: Rate limit (DAE) returning stale cached post " + postID);
                    return stale;
                }
                return null;
            }
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
            ArrayList<String> images = new ArrayList<>();
            if (postObj.has("images")) {
                JSONArray imagesJSONArray = postObj.getJSONArray("images");
                for (int i = 0; i < imagesJSONArray.length(); i++) {
                    images.add(imagesJSONArray.getString(i));
                }
            }
            ArrayList<String> tags = new ArrayList<>();
            if (postObj.has("tags")) {
                JSONArray tagArray = postObj.getJSONArray("tags");
                for (int i = 0; i < tagArray.length(); i++) {
                    tags.add(tagArray.getString(i));
                }
            }
            HashMap<String, ArrayList<String>> contentsMap = new HashMap<>();
            if (postObj.has("contents")) {
                JSONObject contents = postObj.getJSONObject("contents");
                for (String key : contents.keySet()) {
                    JSONArray arr = contents.getJSONArray(key);
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) list.add(arr.getString(i));
                    contentsMap.put(key, list);
                }
            }
            Post post;
            if ("recipe".equalsIgnoreCase(type)) {
                ArrayList<String> ingredients = contentsMap.getOrDefault("ingredients", new ArrayList<>());
                String steps = String.join("<br>", contentsMap.getOrDefault("steps", new ArrayList<>()));
                ArrayList<String> cuisines = contentsMap.getOrDefault("cuisines", new ArrayList<>());
                Post base = new Post(user, postID, title, description, images, contentsMap, type, timestamp, tags);
                base.setLikes(likeCount);
                post = new Recipe(base, ingredients, steps, cuisines);
            } else if ("review".equalsIgnoreCase(type)) {
                Post base = new Post(user, postID, title, description, images, contentsMap, type, timestamp, tags);
                base.setLikes(likeCount);
                Review review = new Review(user, postID, title, description);
                if (postObj.has("rating")) {
                    try { review.setRating(postObj.getDouble("rating")); } catch (Exception ignored) {}
                }
                post = review;
            } else {
                post = new Post(user, postID, title, description, images, contentsMap, type, timestamp, tags);
                post.setLikes(likeCount);
            }
            POST_CACHE.put(postID, new CacheEntry(post));
            return post;
        } catch (Exception e) {
            System.out.println("DEBUG: Error reconstructing post " + postID + ": " + e.getMessage());
            return null;
        }
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


    public ArrayList<Long> getAvailableReviews() {

        ArrayList<Long> reviewIDs = new ArrayList<>();
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        JSONObject posts = data.getJSONObject("posts");
        for (String key : posts.keySet()) {
            JSONObject post = (JSONObject)posts.get(key);
            Object oType = post.get("type");
            if (oType instanceof String && ((String)oType).equals("review")) {
                long postID = Long.parseLong(key);
                reviewIDs.add(postID);
            }


        }
//
//
//
//        JSONObject data = new JSONObject();
//        try {
//            data = getJsonObject();
//        }
//        catch (DataAccessException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        if (!data.has("reviews")) {
//            return new ArrayList<>();
//        }
//
//        JSONObject reviews = data.getJSONObject("reviews");
//        ArrayList<Long> reviewIDs = new ArrayList<>();
//        for (String key : reviews.keySet()) {
//            long reviewID = Long.parseLong(key);
//            reviewIDs.add(reviewID);
//        }
        return reviewIDs;
    }

    @Override
    public void addPostToClub(Post post, long clubId) throws DataAccessException {
        // DB-only: directly append post ID to the club's posts list in remote storage.
        try {
            ClubsDataAccessObject clubsDAO = DBClubsDataAccessObject.getInstance(this);
            if (clubsDAO instanceof DBClubsDataAccessObject) {
                DBClubsDataAccessObject dbClubs = (DBClubsDataAccessObject) clubsDAO;
                dbClubs.addPostIdToClub(clubId, post.getID());
                System.out.println("DEBUG[DBPostDAO]: Added post ID " + post.getID() + " to club " + clubId + " (DB only)");
            } else {
                throw new DataAccessException("Clubs DAO instance is not DBClubsDataAccessObject");
            }
        } catch (Exception ex) {
            if (ex instanceof DataAccessException) throw (DataAccessException) ex;
            throw new DataAccessException("Failed to add post to club in DB: " + ex.getMessage());
        }
    }

    @Override
    public void deleteAllPostsByUser(String username, ClubsDataAccessObject clubsDAO, UserDataAccessObject userDAO) {
        // Collect post IDs to delete by scanning all posts (avoid inconsistent user userPosts list)
        ArrayList<Long> allIds = getAvailablePosts();
        ArrayList<Long> toDelete = new ArrayList<>();
        if (allIds != null) {
            for (Long id : allIds) {
                Post p = getPost(id);
                if (p != null && p.getUser() != null && username.equals(p.getUser().getUsername())) {
                    toDelete.add(id);
                }
            }
        }
        // Remove references from all clubs (DB implementation knows how to persist itself)
        if (clubsDAO != null) {
            try {
                ArrayList<Club> clubs = clubsDAO.getAllClubs();
                for (Club club : clubs) {
                    boolean changed = false;
                    ArrayList<Post> kept = new ArrayList<>();
                    for (Post p : club.getPosts()) {
                        if (!toDelete.contains(p.getID())) kept.add(p); else changed = true;
                    }
                    if (changed) {
                        clubsDAO.writeClub(club.getId(), club.getMembers(), club.getName(), club.getDescription(), club.getImageUrl(), kept, club.getTags());
                    }
                }
            } catch (Exception e) {
                System.out.println("DEBUG[DBPostDAO]: Error cleaning club references: " + e.getMessage());
            }
        }
        // Delete posts from posts map
        for (Long id : toDelete) {
            deletePost(id);
            POST_CACHE.remove(id);
        }
        // Update user's post list if present
        if (userDAO != null) {
            try {
                User u = userDAO.get(username);
                if (u instanceof Account) {
                    Account acc = (Account) u;
                    if (acc.getUserPosts() != null) {
                        acc.getUserPosts().removeAll(toDelete);
                        userDAO.save(acc);
                    }
                }
            } catch (Exception e) {
                System.out.println("DEBUG[DBPostDAO]: Error updating user post list: " + e.getMessage());
            }
        }
    }
}
