package data_access;

import entity.Account;
import entity.Club;
import entity.Post;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DBClubsDataAccessObject {
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
