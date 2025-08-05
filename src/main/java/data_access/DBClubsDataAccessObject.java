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

public class DBClubsDataAccessObject implements ClubsDataAccessObject {
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
        newClub.put("id", clubID);

        // Handle null members list
        JSONArray memberArray = new JSONArray();
        if (members != null) {
            for (Account member : members) {
                if (member != null) {
                    memberArray.put(member.getUsername());
                }
            }
        }
        newClub.put("members", memberArray);
        newClub.put("name", name);
        newClub.put("description", description);

        // Handle null posts list
        JSONArray postArray = new JSONArray();
        if (posts != null) {
            for (Post post : posts) {
                postArray.put(post.getID());
            }
        }
        newClub.put("posts", postArray);

        // Handle null tags list
        newClub.put("tags", new JSONArray(tags != null ? tags : new ArrayList<>()));

        clubs.put(String.valueOf(clubID), newClub);
        data.put("clubs", clubs);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to write club data to file: " + e.getMessage(), e);
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
                // Skip null members
                if (!memberArray.isNull(i)) {
                    String username = memberArray.getString(i);
                    if (username != null && !username.isEmpty()) {
                        members.add(new Account(username, "password"));
                    }
                }
            }
        }

        ArrayList<Post> posts = new ArrayList<>();
        if (clubObj.has("posts")) {
            JSONArray postArray = clubObj.getJSONArray("posts");
            for (int i = 0; i < postArray.length(); i++) {
                if (!postArray.isNull(i)) {
                    String postId = postArray.getString(i);
                    // For now, we're not loading the actual posts since they're not displayed
                    // You would need to implement post loading if they're needed
                }
            }
        }

        ArrayList<String> tags = new ArrayList<>();
        if (clubObj.has("tags")) {
            JSONArray tagsArray = clubObj.getJSONArray("tags");
            for (int i = 0; i < tagsArray.length(); i++) {
                if (!tagsArray.isNull(i)) {
                    String tag = tagsArray.getString(i);
                    if (tag != null && !tag.isEmpty()) {
                        tags.add(tag);
                    }
                }
            }
        }

        return new Club(name, description, members, new ArrayList<>(), posts, clubID, tags);
    }

    @Override
    public ArrayList<Club> getAllClubs() {
        JSONObject data = getJsonObject();
        ArrayList<Club> allClubs = new ArrayList<>();

        if (!data.has("clubs")) {
            return allClubs;
        }

        JSONObject clubs = data.getJSONObject("clubs");
        for (String clubId : clubs.keySet()) {
            Club club = getClub(Long.parseLong(clubId));
            if (club != null) {
                allClubs.add(club);
            }
        }

        return allClubs;
    }
}
