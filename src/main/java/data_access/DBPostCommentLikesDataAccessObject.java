package data_access;/**
 * Created by Emilia on 2025-07-29!
 * Description:
 * ^ • ω • ^
 */

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

public class DBPostCommentLikesDataAccessObject implements PostCommentsLikesDataAccessObject {
    private String filePath = "src/main/java/data_access/data_storage.json";


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
    public void writePost(long postID, Account user, String title, String postType, String description, HashMap<String, ArrayList<String>> contents, ArrayList<String> tags, ArrayList<String> images, String time) {
        JSONObject data = getJsonObject();
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
        if (time.charAt(time.length() - 4) == 'a') {
            String cutTime = time.substring(0, time.length() - 4) + "AM";
            newPost.put("time", cutTime);
        }
        else {
            String cutTime = time.substring(0, time.length() - 4) + "PM";
            newPost.put("time", cutTime);
        }
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
            else if (postObj.get("type").equals("other?")) {

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

    public void writeClub(long clubID, ArrayList<Account> members, String name, String description, ArrayList<Post> posts, ArrayList<String> tags) {
        JSONObject data = getJsonObject();
        JSONObject clubs;
        if (data.has("clubs")) {
            clubs = data.getJSONObject("clubs");
        }
        else {
            clubs = new JSONObject();
        }
        JSONObject newClub = new JSONObject();
        newClub.put("name", name);
        newClub.put("description", description);
        newClub.put("posts", posts);
        clubs.put(String.valueOf(clubID), newClub);
        data.put("clubs", clubs);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        }
        catch (IOException e) {
            throw new RuntimeException("i am sad :(", e);
        }
    }

    public Club getClub(long clubID) {
        JSONObject data = getJsonObject();

        if (!data.has("clubs")) {
            return null;
        }

        JSONObject clubs = data.getJSONObject("clubs");
        String parentClubID = String.valueOf(clubID);

        if (!clubs.has(parentClubID)) {
            return null;
        }

        JSONObject clubObj = clubs.getJSONObject(parentClubID);
        String name = clubObj.getString("name");
        String description = clubObj.getString("description");
        ArrayList<Account> members = new ArrayList<>();
        if (clubObj.has("members")) {
            JSONArray memberArray = clubObj.getJSONArray("members");
            for (int i = 0; i < memberArray.length(); i++) {
                String username = memberArray.getString(i);
                members.add(new Account(username, "password"));
            }
        }
        ArrayList<Post> posts = new ArrayList<>();
        if (clubObj.has("posts")) {
            JSONArray postArray = clubObj.getJSONArray("posts");
            for (int i = 0; i < postArray.length(); i++) {
                JSONObject postObj = postArray.getJSONObject(i);
                String username = postObj.getString("user");
                String title = postObj.getString("title");
                String desc = postObj.getString("description");
                long postID = postObj.getLong("id");
                Account user = new Account(username, "password");
                Post post = new Post(user, postID, title, desc);
                posts.add(post);
            }
        }

        return new Club(name, description, members, new ArrayList<>(), posts) {
            {
                setDisplayName(name);
                setDescription(description);
            }
        };
    }
}
