package data_access;

import entity.Account;
import entity.Club;
import entity.Post;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Database-backed implementation of ClubsDataAccessObject.
 * Persists club information (metadata, members, post id list, tags) in the remote JSON store
 * under a dedicated data key.
 */
public class DBClubsDataAccessObject implements ClubsDataAccessObject {
    private static ClubsDataAccessObject instance;
    private final PostCommentsLikesDataAccessObject postDAO;

    private static final String DATABASE_USERNAME = "csc207munchablesusername";
    private static final String DATABASE_PASSWORD = "csc207munchablespassword";
    private static final String DATA_KEY = "clubsdata"; // container for all clubs in remote store

    private static final int CREDENTIAL_ERROR = 401;
    private static final int SUCCESS_CODE = 200;
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";

    private DBClubsDataAccessObject(PostCommentsLikesDataAccessObject postDAO) { this.postDAO = postDAO; }

    public static ClubsDataAccessObject getInstance(PostCommentsLikesDataAccessObject postDAO) {
        if (instance == null) {
            instance = new DBClubsDataAccessObject(postDAO);
        }
        return instance;
    }

    // Retrieve the clubsdata JSONObject (creating it if missing)
    @NotNull
    private JSONObject getJsonObject() throws DataAccessException {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("http://vm003.teach.cs.toronto.edu:20112/user?username=%s", DATABASE_USERNAME))
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try (Response response = client.newCall(request).execute()) {
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                final JSONObject userJSONObject = responseBody.getJSONObject("user");
                final JSONObject info = userJSONObject.getJSONObject("info");
                if (!info.has(DATA_KEY)) {
                    info.put(DATA_KEY, new JSONObject());
                    saveJSONObject(info.getJSONObject(DATA_KEY));
                }
                return info.getJSONObject(DATA_KEY); // structure: { "clubs": { id: {...}, ... } }
            } else {
                throw new DataAccessException(responseBody.getString(MESSAGE));
            }
        } catch (IOException | JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    // Retrieve entire info object
    private JSONObject getInfo() throws DataAccessException {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("http://vm003.teach.cs.toronto.edu:20112/user?username=%s", DATABASE_USERNAME))
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try (Response response = client.newCall(request).execute()) {
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                final JSONObject userJSONObject = responseBody.getJSONObject("user");
                return userJSONObject.getJSONObject("info");
            } else {
                throw new DataAccessException(responseBody.getString(MESSAGE));
            }
        } catch (IOException | JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    // Persist DATA_KEY object back to remote store
    private boolean saveJSONObject(JSONObject clubsData) throws DataAccessException {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, DATABASE_USERNAME);
        requestBody.put(PASSWORD, DATABASE_PASSWORD);
        final JSONObject info = getInfo();
        info.put(DATA_KEY, clubsData);
        requestBody.put("info", info);
        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                .url("http://vm003.teach.cs.toronto.edu:20112/modifyUserInfo")
                .method("PUT", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try (Response response = client.newCall(request).execute()) {
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                return true;
            } else if (responseBody.getInt(STATUS_CODE_LABEL) == CREDENTIAL_ERROR) {
                throw new DataAccessException("message could not be found or password was incorrect");
            } else {
                throw new DataAccessException("database error: " + responseBody.getString(MESSAGE));
            }
        } catch (IOException | JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void writeClub(long clubID, ArrayList<Account> members, String name, String description, String imageUrl, ArrayList<Post> posts, ArrayList<String> tags) {
        JSONObject data;
        try { data = getJsonObject(); } catch (DataAccessException e) { System.out.println(e.getMessage()); return; }
        JSONObject clubs = data.has("clubs") ? data.getJSONObject("clubs") : new JSONObject();
        JSONObject existing = clubs.has(String.valueOf(clubID)) ? clubs.getJSONObject(String.valueOf(clubID)) : new JSONObject();
        LinkedHashSet<Long> postIds = new LinkedHashSet<>();
        if (existing.has("posts")) {
            JSONArray existingPosts = existing.getJSONArray("posts");
            for (int i = 0; i < existingPosts.length(); i++) { try { postIds.add(existingPosts.getLong(i)); } catch (Exception ignored) {} }
        }
        if (posts != null) { for (Post p : posts) if (p != null) postIds.add(p.getID()); }
        LinkedHashSet<String> memberUsernames = new LinkedHashSet<>();
        if (members != null) { for (Account m : members) if (m != null && m.getUsername() != null) memberUsernames.add(m.getUsername()); }
        JSONArray memberArray = new JSONArray();
        for (String uname : memberUsernames) memberArray.put(uname);
        JSONArray postsArray = new JSONArray();
        for (Long id : postIds) postsArray.put(id);
        LinkedHashSet<String> tagSet = new LinkedHashSet<>();
        if (tags != null) tagSet.addAll(tags);
        JSONArray tagArray = new JSONArray();
        for (String t : tagSet) if (t != null) tagArray.put(t);
        JSONObject newClub = new JSONObject();
        newClub.put("id", clubID);
        newClub.put("name", name);
        newClub.put("description", description);
        newClub.put("imageUrl", imageUrl);
        newClub.put("members", memberArray);
        newClub.put("posts", postsArray);
        newClub.put("tags", tagArray);
        clubs.put(String.valueOf(clubID), newClub);
        data.put("clubs", clubs);
        try { saveJSONObject(data); } catch (DataAccessException e) { System.out.println(e.getMessage()); }
    }

    @Override
    public boolean clubExists(String clubName) {
        JSONObject data;
        try { data = getJsonObject(); } catch (DataAccessException e) { return false; }
        if (!data.has("clubs")) return false;
        JSONObject clubs = data.getJSONObject("clubs");
        for (String id : clubs.keySet()) {
            JSONObject club = clubs.getJSONObject(id);
            if (club.optString("name").equalsIgnoreCase(clubName)) return true;
        }
        return false;
    }

    @Override
    public Club getClub(long clubID) {
        JSONObject data;
        try { data = getJsonObject(); } catch (DataAccessException e) { System.out.println(e.getMessage()); return null; }
        if (!data.has("clubs")) return null;
        JSONObject clubs = data.getJSONObject("clubs");
        String key = String.valueOf(clubID);
        if (!clubs.has(key)) return null;
        JSONObject clubData = clubs.getJSONObject(key);
        String name = clubData.optString("name", "");
        String description = clubData.optString("description", "");
        String imageUrl = clubData.has("imageUrl") ? clubData.optString("imageUrl", null) : null;
        long id = clubData.optLong("id", clubID);

        // Members
        ArrayList<Account> members = new ArrayList<>();
        if (clubData.has("members")) {
            JSONArray mem = clubData.getJSONArray("members");
            for (int i = 0; i < mem.length(); i++) {
                try { members.add(new Account(mem.getString(i), "")); } catch (Exception ignored) {}
            }
        }

        // Posts
        ArrayList<Post> posts = new ArrayList<>();
        if (clubData.has("posts")) {
            JSONArray pArr = clubData.getJSONArray("posts");
            JSONArray newPosts = new JSONArray();
            for (int i = 0; i < pArr.length(); i++) {
                try { long pid = pArr.getLong(i); Post p = postDAO.getPost(pid); if (p != null) { posts.add(p); newPosts.put(pid); } } catch (Exception ex) { System.out.println("DEBUG[DBClubsDAO]: Failed loading post: " + ex.getMessage()); }
            }
            // Replace posts array with validated list if changed
            if (newPosts.length() != pArr.length()) {
                clubData.put("posts", newPosts);
                clubs.put(key, clubData);
                data.put("clubs", clubs);
                try { saveJSONObject(data); } catch (DataAccessException ignored) {}
            }
        }

        // Tags
        ArrayList<String> tags = new ArrayList<>();
        if (clubData.has("tags")) {
            JSONArray tArr = clubData.getJSONArray("tags");
            for (int i = 0; i < tArr.length(); i++) try { tags.add(tArr.getString(i)); } catch (Exception ignored) {}
        }

        ArrayList<String> foodPreferences = new ArrayList<>(); // Not persisted separately in current design
        return new Club(name, description, imageUrl, members, foodPreferences, posts, id, tags);
    }

    @Override
    public ArrayList<Club> getAllClubs() {
        ArrayList<Club> result = new ArrayList<>();
        JSONObject data;
        try { data = getJsonObject(); } catch (DataAccessException e) { System.out.println(e.getMessage()); return result; }
        if (!data.has("clubs")) return result;
        JSONObject clubs = data.getJSONObject("clubs");
        for (String key : clubs.keySet()) {
            try {
                Club c = getClub(Long.parseLong(key));
                if (c != null) result.add(c);
            } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    @Override
    public void removeMemberFromClub(String username, long clubID) {
        JSONObject data;
        try { data = getJsonObject(); } catch (DataAccessException e) { System.out.println(e.getMessage()); return; }
        if (!data.has("clubs")) return;
        JSONObject clubs = data.getJSONObject("clubs");
        String key = String.valueOf(clubID);
        if (!clubs.has(key)) return;
        JSONObject clubData = clubs.getJSONObject(key);
        if (!clubData.has("members")) return;
        JSONArray members = clubData.getJSONArray("members");
        JSONArray newMembers = new JSONArray();
        for (int i = 0; i < members.length(); i++) {
            try {
                String uname = members.getString(i);
                if (!uname.equals(username)) newMembers.put(uname);
            } catch (Exception ignored) {}
        }
        clubData.put("members", newMembers);
        clubs.put(key, clubData);
        data.put("clubs", clubs);
        try { saveJSONObject(data); } catch (DataAccessException e) { System.out.println(e.getMessage()); }
        // Update user membership list in DB user store
        try {
            DBUserDataAccessObject userDAO = (DBUserDataAccessObject) DBUserDataAccessObject.getInstance();
            userDAO.removeClubFromUser(username, String.valueOf(clubID));
        } catch (Exception ex) { System.out.println("DEBUG[DBClubsDAO]: Failed to update user club removal: " + ex.getMessage()); }
    }

    @Override
    public void deleteClub(long clubID) {
        JSONObject data;
        try { data = getJsonObject(); } catch (DataAccessException e) { System.out.println(e.getMessage()); return; }
        if (!data.has("clubs")) return;
        JSONObject clubs = data.getJSONObject("clubs");
        String key = String.valueOf(clubID);
        if (!clubs.has(key)) return;
        java.util.List<String> memberUsernames = new java.util.ArrayList<>();
        try {
            JSONObject clubData = clubs.getJSONObject(key);
            if (clubData.has("members")) {
                JSONArray members = clubData.getJSONArray("members");
                for (int i = 0; i < members.length(); i++) {
                    try { memberUsernames.add(members.getString(i)); } catch (Exception ignored) {}
                }
            }
        } catch (Exception ex) { System.out.println("DEBUG[DBClubsDAO]: Error collecting members on delete: " + ex.getMessage()); }
        try {
            DBUserDataAccessObject userDAO = (DBUserDataAccessObject) DBUserDataAccessObject.getInstance();
            userDAO.bulkRemoveClubFromUsers(key, memberUsernames);
        } catch (Exception ex) { System.out.println("DEBUG[DBClubsDAO]: bulk member cleanup failed: " + ex.getMessage()); }
        clubs.remove(key);
        data.put("clubs", clubs);
        int attempts = 0;
        while (attempts < 3) {
            attempts++;
            try { saveJSONObject(data); return; }
            catch (DataAccessException e) {
                System.out.println("DEBUG[DBClubsDAO]: save attempt " + attempts + " failed deleting club " + key + ": " + e.getMessage());
                try { Thread.sleep(150L * attempts); } catch (InterruptedException ignored) {}
            }
        }
        System.out.println("DEBUG[DBClubsDAO]: FAILED to persist deletion of club " + key + " after retries");
    }

    // Add a post ID to a club's post list (DB-only, no file mirroring)
    public void addPostIdToClub(long clubId, long postId) throws DataAccessException {
        JSONObject data = getJsonObject();
        if (!data.has("clubs")) {
            throw new DataAccessException("No clubs data when adding post to club " + clubId);
        }
        JSONObject clubs = data.getJSONObject("clubs");
        String key = String.valueOf(clubId);
        if (!clubs.has(key)) {
            throw new DataAccessException("Club " + clubId + " not found when adding post " + postId);
        }
        JSONObject club = clubs.getJSONObject(key);
        JSONArray postsArray = club.has("posts") ? club.getJSONArray("posts") : new JSONArray();
        boolean exists = false;
        for (int i = 0; i < postsArray.length(); i++) {
            if (postsArray.optLong(i) == postId) { exists = true; break; }
        }
        if (!exists) {
            postsArray.put(postId);
        }
        club.put("posts", postsArray);
        clubs.put(key, club);
        data.put("clubs", clubs);
        saveJSONObject(data);
    }
}
