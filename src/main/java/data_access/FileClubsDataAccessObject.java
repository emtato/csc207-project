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

public class FileClubsDataAccessObject implements ClubsDataAccessObject {
    private String filePath = "src/main/java/data_access/data_storage.json";
    private final PostCommentsLikesDataAccessObject postDAO;

    public FileClubsDataAccessObject(PostCommentsLikesDataAccessObject postDAO) {
        this.postDAO = postDAO;
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

        // Ensure clubs object exists
        if (!data.has("clubs")) {
            data.put("clubs", new JSONObject());
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(data.toString(2));
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize clubs data: " + e.getMessage(), e);
            }
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

    public boolean clubExists(String clubName) {
        JSONObject data = getJsonObject();
        JSONObject clubs = data.getJSONObject("clubs");

        for (String clubId : clubs.keySet()) {
            JSONObject clubData = clubs.getJSONObject(clubId);
            if (clubData.getString("name").equalsIgnoreCase(clubName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Club getClub(long clubID) {
        return getClub(String.valueOf(clubID));
    }

    /**
     * Gets a specific club by its ID
     * @param clubId The ID of the club to get
     * @return The Club object, or null if not found
     */
    public Club getClub(String clubId) {
        JSONObject data = getJsonObject();
        JSONObject clubs = data.getJSONObject("clubs");

        if (!clubs.has(clubId)) {
            return null;
        }

        JSONObject clubData = clubs.getJSONObject(clubId);

        // Get basic club info
        String name = clubData.getString("name");
        String description = clubData.getString("description");
        long id = clubData.getLong("id");

        // Get members
        JSONArray membersJson = clubData.getJSONArray("members");
        ArrayList<Account> members = new ArrayList<>();
        for (int i = 0; i < membersJson.length(); i++) {
            if (!membersJson.isNull(i)) {
                members.add(new Account(membersJson.getString(i), ""));
            }
        }

        // Get food preferences
        ArrayList<String> foodPreferences = new ArrayList<>();

        // Get posts - handle them as numbers (Long)
        ArrayList<Post> posts = new ArrayList<>();
        if (clubData.has("posts")) {
            JSONArray postsJson = clubData.getJSONArray("posts");
            for (int i = 0; i < postsJson.length(); i++) {
                long postId = postsJson.getLong(i);
                Post post = postDAO.getPost(postId);
                if (post != null) {
                    posts.add(post);
                }
            }
        }

        // Get tags
        ArrayList<String> tags = new ArrayList<>();
        if (clubData.has("tags")) {
            JSONArray tagsJson = clubData.getJSONArray("tags");
            for (int i = 0; i < tagsJson.length(); i++) {
                tags.add(tagsJson.getString(i));
            }
        }

        return new Club(name, description, members, foodPreferences, posts, id, tags);
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

    public void removeMemberFromClub(String username, long clubID) {
        // Update clubs.json
        JSONObject data = getJsonObject();
        JSONObject clubs = data.getJSONObject("clubs");
        String clubIdStr = String.valueOf(clubID);

        if (clubs.has(clubIdStr)) {
            JSONObject clubData = clubs.getJSONObject(clubIdStr);
            JSONArray members = clubData.getJSONArray("members");

            // Create a new array without the username
            JSONArray newMembers = new JSONArray();
            for (int i = 0; i < members.length(); i++) {
                if (!members.isNull(i) && !members.getString(i).equals(username)) {
                    newMembers.put(members.getString(i));
                }
            }

            clubData.put("members", newMembers);
            clubs.put(clubIdStr, clubData);

            try (FileWriter writer = new FileWriter(filePath)) {
                data.put("clubs", clubs);
                writer.write(data.toString(2));
            } catch (IOException e) {
                throw new RuntimeException("Failed to update club data: " + e.getMessage(), e);
            }

            // Update user_data.json
            FileUserDataAccessObject userDAO = (FileUserDataAccessObject) FileUserDataAccessObject.getInstance();
            userDAO.removeClubFromUser(username, clubIdStr);
        }
    }
}
