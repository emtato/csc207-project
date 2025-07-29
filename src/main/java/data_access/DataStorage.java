package data_access;/**
 * Created by Emilia on 2025-07-29!
 * Description:
 * ^ • ω • ^
 */

import entity.Account;
import entity.Comment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataStorage {
    private String filePath = "src/main/java/data_access/data_storage.json";

     public static void main(String[] args) {
        String postId = "483958292";
        DataStorage dataStorage = new DataStorage();
        dataStorage.writeDataToFile(postId, new Account("bil", "bal"), "bol", LocalDateTime.now());



    }

    public void writeDataToFile(String parentPostID, Account user, String contents, LocalDateTime timestamp) {
        JSONObject data;
        JSONArray comments;

        // read existing data
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            data = new JSONObject(content);
        } catch (IOException e) {
            data = new JSONObject();
        }


        if (data.has(parentPostID)) {
            comments = data.getJSONArray(parentPostID);
        } else {
            comments = new JSONArray();
        }

        //add new comment
        JSONObject comment = new JSONObject();
        comment.put("user", user);
        comment.put("content", contents);
        comment.put("time", timestamp.toString());
        comments.put(comment);

        data.put(parentPostID, comments);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("i am sad :(", e);
        }
    }

    public ArrayList<Comment> getComments(String parentPostID) {
        File file = new File(filePath);
        JSONObject data;
        JSONArray comments;
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            data = new JSONObject(content);
            if (data.has(parentPostID)) {
                comments = data.getJSONArray(parentPostID);
            }
            else {
                comments = new JSONArray();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(":C", e);
        }
        ArrayList<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < comments.length(); i++) {
            JSONObject obj = comments.getJSONObject(i);
            Account user = (Account) obj.get("user");
            String content = obj.getString("content");
            LocalDateTime time = LocalDateTime.parse(obj.getString("time"));
            commentList.add(new Comment(user, content, time, 0));
        }
        return commentList;
    }


}
