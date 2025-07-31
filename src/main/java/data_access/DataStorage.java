package data_access;/**
 * Created by Emilia on 2025-07-29!
 * Description:
 * ^ • ω • ^
 */

import entity.Account;
import entity.Comment;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataStorage {
    private String filePath = "src/main/java/data_access/data_storage.json";

    public static void main(String[] args) {
        long postId = 483958292l;
        DataStorage dataStorage = new DataStorage();
        dataStorage.writeCommentToFile(postId, new Account("bil", "bal"), "bol", LocalDateTime.now());

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

    public void writeCommentToFile(long parentID, Account user, String contents, LocalDateTime timestamp) {
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

    public void writeLikeToFile(Account user, long postID) {
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

    public boolean postLikedyesNopls(Account user, long postID) {
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
}
