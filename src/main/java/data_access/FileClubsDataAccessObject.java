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

    @Override
    public void writeClub(long clubID, ArrayList<Account> members, String name, String description, String imageUrl, ArrayList<Post> posts, ArrayList<String> tags) {
        JSONObject data = getJsonObject();
        JSONObject clubs = data.getJSONObject("clubs");

        // Existing club (if present) to preserve posts
        JSONObject existingClub = clubs.has(String.valueOf(clubID)) ? clubs.getJSONObject(String.valueOf(clubID)) : new JSONObject();

        // Collect existing post IDs
        java.util.LinkedHashSet<Long> postIds = new java.util.LinkedHashSet<>();
        if (existingClub.has("posts")) {
            JSONArray existingPosts = existingClub.getJSONArray("posts");
            for (int i = 0; i < existingPosts.length(); i++) {
                try { postIds.add(existingPosts.getLong(i)); } catch (Exception ignored) {}
            }
        }
        // Merge provided posts list (typically empty for membership updates)
        if (posts != null) {
            for (Post p : posts) {
                if (p != null) postIds.add(p.getID());
            }
        }

        // Build new club JSON
        JSONObject newClub = new JSONObject();
        newClub.put("id", clubID);
        newClub.put("name", name);
        newClub.put("description", description);
        newClub.put("imageUrl", imageUrl);

        // Members: dedupe + skip nulls
        java.util.LinkedHashSet<String> memberUsernames = new java.util.LinkedHashSet<>();
        if (members != null) {
            for (Account m : members) {
                if (m != null && m.getUsername() != null) {
                    memberUsernames.add(m.getUsername());
                }
            }
        }
        JSONArray memberArray = new JSONArray();
        for (String uname : memberUsernames) memberArray.put(uname);
        newClub.put("members", memberArray);

        // Posts
        JSONArray postsArray = new JSONArray();
        for (Long id : postIds) postsArray.put(id);
        newClub.put("posts", postsArray);

        // Tags: dedupe, skip null
        JSONArray tagArray = new JSONArray();
        if (tags != null) {
            java.util.LinkedHashSet<String> tagSet = new java.util.LinkedHashSet<>(tags);
            for (String t : tagSet) if (t != null) tagArray.put(t);
        }
        newClub.put("tags", tagArray);

        clubs.put(String.valueOf(clubID), newClub);
        data.put("clubs", clubs);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write club data to file: " + e.getMessage(), e);
        }
    }

    @Override
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
            System.out.println("DEBUG: Club not found with ID: " + clubId);
            return null;
        }

        JSONObject clubData = clubs.getJSONObject(clubId);
        System.out.println("DEBUG: Loading club data for ID: " + clubId);

        // Get basic club info
        String name = clubData.getString("name");
        String description = clubData.getString("description");
        String imageUrl = clubData.has("imageUrl") ? clubData.optString("imageUrl", null) : null;
        long id = clubData.getLong("id");
        System.out.println("DEBUG: Club basic info - Name: " + name + ", ID: " + id);

        // Get members
        JSONArray membersJson = clubData.getJSONArray("members");
        ArrayList<Account> members = new ArrayList<>();
        for (int i = 0; i < membersJson.length(); i++) {
            if (!membersJson.isNull(i)) {
                members.add(new Account(membersJson.getString(i), ""));
            }
        }
        System.out.println("DEBUG: Club members count: " + members.size());

        // Get food preferences
        ArrayList<String> foodPreferences = new ArrayList<>();

        // Get posts
        ArrayList<Post> posts = new ArrayList<>();
        if (clubData.has("posts")) {
            JSONArray postsJson = clubData.getJSONArray("posts");
            System.out.println("DEBUG: Found " + postsJson.length() + " post IDs in club data");
            for (int i = 0; i < postsJson.length(); i++) {
                try {
                    long postId = postsJson.getLong(i);
                    System.out.println("DEBUG: Loading post with ID: " + postId);
                    Post post = postDAO.getPost(postId);
                    if (post != null) {
                        posts.add(post);
                        System.out.println("DEBUG: Successfully loaded post: " + postId);
                    } else {
                        System.out.println("DEBUG: Failed to load post: " + postId + " - Post does not exist");
                        // Remove this post ID from the club's posts array since it doesn't exist
                        postsJson.remove(i);
                        i--; // Adjust index since we removed an element
                        clubData.put("posts", postsJson);
                        clubs.put(clubId, clubData);
                        try (FileWriter writer = new FileWriter(filePath)) {
                            data.put("clubs", clubs);
                            writer.write(data.toString(2));
                            System.out.println("DEBUG: Removed invalid post ID from club data");
                        } catch (IOException e) {
                            System.out.println("DEBUG: Failed to update club data: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG: Error loading post at index " + i + ": " + e.getMessage());
                }
            }
        } else {
            System.out.println("DEBUG: No posts array found in club data");
        }
        System.out.println("DEBUG: Successfully loaded " + posts.size() + " posts for club");

        // Get tags
        ArrayList<String> tags = new ArrayList<>();
        if (clubData.has("tags")) {
            JSONArray tagsJson = clubData.getJSONArray("tags");
            for (int i = 0; i < tagsJson.length(); i++) {
                tags.add(tagsJson.getString(i));
            }
        }

        return new Club(name, description, imageUrl, members, foodPreferences, posts, id, tags);
    }

    @Override
    public ArrayList<Club> getAllClubs() {
        JSONObject data = getJsonObject();
        ArrayList<Club> allClubs = new ArrayList<>();

        System.out.println("DEBUG: Loading all clubs from storage");
        System.out.println("DEBUG: Data has 'clubs' key: " + data.has("clubs"));

        if (!data.has("clubs")) {
            System.out.println("DEBUG: No clubs found in storage");
            return allClubs;
        }

        JSONObject clubs = data.getJSONObject("clubs");
        System.out.println("DEBUG: Found clubs in storage. Club IDs: " + clubs.keySet());

        for (String clubId : clubs.keySet()) {
            System.out.println("\nDEBUG: Loading club with ID: " + clubId);
            Club club = getClub(Long.parseLong(clubId));
            if (club != null) {
                System.out.println("DEBUG: Successfully loaded club: " + club.getName());
                allClubs.add(club);
            } else {
                System.out.println("DEBUG: Failed to load club with ID: " + clubId);
            }
        }

        System.out.println("DEBUG: Finished loading clubs. Total loaded: " + allClubs.size());
        return allClubs;
    }

    @Override
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

    @Override
    public void deleteClub(long clubID) {
        System.out.println("[DEBUG] Attempting to delete club " + clubID);
        JSONObject data = getJsonObject();
        if (data.has("clubs")) {
            JSONObject clubs = data.getJSONObject("clubs");
            String key = String.valueOf(clubID);
            if (clubs.has(key)) {
                try {
                    JSONObject clubData = clubs.getJSONObject(key);
                    if (clubData.has("members")) {
                        JSONArray members = clubData.getJSONArray("members");
                        FileUserDataAccessObject userDAO = (FileUserDataAccessObject) FileUserDataAccessObject.getInstance();
                        for (int i = 0; i < members.length(); i++) {
                            if (!members.isNull(i)) {
                                String uname = members.getString(i);
                                System.out.println("[DEBUG] Removing club " + key + " from user " + uname);
                                userDAO.removeClubFromUser(uname, key);
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("[DEBUG] Error while removing club from members: " + ex.getMessage());
                }
                clubs.remove(key);
                data.put("clubs", clubs);
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(data.toString(2));
                    System.out.println("[DEBUG] Club " + key + " deleted and file updated.");
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete club: " + e.getMessage(), e);
                }
            } else {
                System.out.println("[DEBUG] Club id " + key + " not found in JSON.");
            }
        } else {
            System.out.println("[DEBUG] No 'clubs' section present when attempting delete.");
        }
    }
}
