package data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import entity.Account;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * The DAO for user data.
 */
public class DBUserDataAccessObject implements UserDataAccessObject{
    private static UserDataAccessObject instance;

    private final String DATABASE_USERNAME = "csc207munchablesusername";
    private final String DATABASE_PASSWORD = "csc207munchablespassword";
    private final String DATA_KEY = "usersinformation";
    private static final int CREDENTIAL_ERROR = 401;
    private static final int SUCCESS_CODE = 200;
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";

    private String currentUsername;

    private DBUserDataAccessObject() {
    }

    public static UserDataAccessObject getInstance() {
        if (instance == null) {
            instance = new DBUserDataAccessObject();
        }
        return instance;
    }

    /**
     * method to reduce duplicate code, retrieves JSONObject from "database"
     *
     * @return JSONObject .
     */
    @NotNull
    private JSONObject getJsonObject() throws Exception {
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
                return data.getJSONObject(DATA_KEY);
            }
            else {
                throw new Exception(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * helper method for saveJSONObject, retrieves JSONObject of all info from database
     *
     * @return JSONObject .
     */
    private JSONObject getInfo() throws Exception {
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
                throw new Exception(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes the JSON object to the "database"
     */
    private boolean saveJSONObject(JSONObject data) throws Exception {
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
                throw new Exception("message could not be found or password was incorrect");
            }
            else {
                throw new Exception("database error: " + responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public boolean existsByName(String identifier) {
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (Exception ex) {}
        return data.has("users") && data.getJSONObject("users").has(identifier);
    }

    @Override
    public void save(User user) {
        // Cast User to Account since we're working with Account implementation
        Account account = (Account) user;
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (Exception ex) {}

        if (!data.has("users")) {
            data.put("users", new JSONObject());
        }

        JSONObject users = data.getJSONObject("users");
        JSONObject userJson = new JSONObject();

        // Store account data
        userJson.put("username", account.getUsername());
        userJson.put("password", account.getPassword());
        userJson.put("email", account.getEmail());
        userJson.put("bio", account.getBio());
        userJson.put("displayName", account.getDisplayName());
        userJson.put("profilePictureUrl", account.getProfilePictureUrl());

        // Store booleans
        userJson.put("isPublic", account.isPublic());
        userJson.put("notificationsEnabled", account.isNotificationsEnabled());

        // Store lists and maps
        userJson.put("likesUsernames", new JSONArray(account.getLikesUsernames()));
        userJson.put("blockedTerms", new JSONArray(account.getBlockedTerms()));
        userJson.put("foodPreferences", new JSONArray(account.getFoodPreferences()));

        // Store maps (only store username, display name, profile picture url reconstruct objects on load)
        JSONObject followerAccountsJson = new JSONObject();
        for (String key : account.getFollowerAccounts().keySet()) {
            JSONObject followerJson = new JSONObject();
            followerJson.put("username", account.getFollowerAccounts().get(key).getUsername());
            followerJson.put("displayName", account.getFollowerAccounts().get(key).getDisplayName());
            followerJson.put("profilePictureUrl", account.getFollowerAccounts().get(key).getProfilePictureUrl());
            followerAccountsJson.put(key, followerJson);
        }
        userJson.put("followerAccounts", followerAccountsJson);

        JSONObject followingAccountsJson = new JSONObject();
        for (String key : account.getFollowingAccounts().keySet()) {
            JSONObject followingJson = new JSONObject();
            followingJson.put("username", account.getFollowingAccounts().get(key).getUsername());
            followingJson.put("displayName", account.getFollowingAccounts().get(key).getDisplayName());
            followingJson.put("profilePictureUrl", account.getFollowingAccounts().get(key).getProfilePictureUrl());
            followingAccountsJson.put(key, followingJson);
        }
        userJson.put("followingAccounts", followingAccountsJson);

        JSONObject blockedAccountsJson = new JSONObject();
        for (String key : account.getBlockedAccounts().keySet()) {
            JSONObject blockedJson = new JSONObject();
            blockedJson.put("username", account.getBlockedAccounts().get(key).getUsername());
            blockedJson.put("displayName", account.getBlockedAccounts().get(key).getDisplayName());
            blockedJson.put("profilePictureUrl", account.getBlockedAccounts().get(key).getProfilePictureUrl());
            blockedAccountsJson.put(key, blockedJson);
        }
        userJson.put("blockedAccounts", blockedAccountsJson);

        // Store muted accounts (usernames only)
        JSONArray mutedAccountsJson = new JSONArray();
        for (User mutedAccount : account.getMutedAccounts()) {
            JSONObject mutedJSON = new JSONObject();
            mutedJSON.put("username", account.getUsername());
            mutedJSON.put("displayName", account.getDisplayName());
            mutedJSON.put("profilePictureUrl", account.getProfilePictureUrl());
            mutedAccountsJson.put(mutedJSON);
        }
        userJson.put("mutedAccounts", mutedAccountsJson);

        // Store user posts
        if (account.getUserPosts() != null) {
            JSONArray userPostsJson = new JSONArray();

            for (Long postId : account.getUserPosts()) {
                userPostsJson.put(postId);
            }
            userJson.put("userPosts", userPostsJson);
        }

        users.put(account.getUsername(), userJson);
        try {
            saveJSONObject(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User get(String username) {
        JSONObject data = new JSONObject();
        try {
            data = getJsonObject();
        }
        catch (Exception ex) {}

        if (!data.has("users")) {
            return null;
        }

        JSONObject users = data.getJSONObject("users");
        if (!users.has(username)) {
            return null;
        }

        JSONObject userJson = users.getJSONObject(username);
        Account account = new Account(userJson.getString("username"), userJson.getString("password"));

        // Set basic properties
        account.setDisplayName(userJson.optString("displayName", ""));
        account.setEmail(userJson.optString("email", ""));
        account.setBio(userJson.optString("bio", ""));
        account.setProfilePictureUrl(userJson.optString("profilePictureUrl",
                "https://i.imgur.com/eA9NeJ1.jpeg"));
        account.setPublic(userJson.optBoolean("isPublic", true));
        account.setNotificationsEnabled(userJson.optBoolean("notificationsEnabled", true));

        // Load lists
        if (userJson.has("likesUsernames")) {
            ArrayList<Long> likes = new ArrayList<>();
            JSONArray likesArray = userJson.getJSONArray("likesUsernames");
            for (int i = 0; i < likesArray.length(); i++) {
                likes.add(likesArray.getLong(i));
            }
            account.setLikesUsernames(likes);
        }

        // Load blocked terms and food preferences
        if (userJson.has("blockedTerms")) {
            ArrayList<String> blockedTerms = new ArrayList<>();
            JSONArray termsArray = userJson.getJSONArray("blockedTerms");
            for (int i = 0; i < termsArray.length(); i++) {
                blockedTerms.add(termsArray.getString(i));
            }
            account.setBlockedTerms(blockedTerms);
        }

        if (userJson.has("foodPreferences")) {
            ArrayList<String> foodPrefs = new ArrayList<>();
            JSONArray prefsArray = userJson.getJSONArray("foodPreferences");
            for (int i = 0; i < prefsArray.length(); i++) {
                foodPrefs.add(prefsArray.getString(i));
            }
            account.setFoodPreferences(foodPrefs);
        }

        // Load maps and complex objects
        // Note: For follower, following, blocked, and muted accounts, we only store usernames, display names, and
        // profile picture urls, and create stub Account objects.
        // Full account data would need to be loaded separately if needed.

        if (userJson.has("followerAccounts")) {
            HashMap<String, User> followers = new HashMap<>();
            JSONObject followersJson = userJson.getJSONObject("followerAccounts");
            for (String key : followersJson.keySet()) {
                String followerUsername = followersJson.getJSONObject(key).getString("username");
                String followerDisplayName = followersJson.getJSONObject(key).getString("displayName");
                String followerPictureUrl = followersJson.getJSONObject(key).getString("profilePictureUrl");
                User follower = new Account(followerUsername, "");
                follower.setDisplayName(followerDisplayName);
                follower.setProfilePictureUrl(followerPictureUrl);
                followers.put(key, follower);
            }
            account.setFollowerAccounts(followers);
        }

        if (userJson.has("followingAccounts")) {
            HashMap<String, User> following = new HashMap<>();
            JSONObject followingJson = userJson.getJSONObject("followingAccounts");
            for (String key : followingJson.keySet()) {
                String followingUsername = followingJson.getJSONObject(key).getString("username");
                String followingDisplayName = followingJson.getJSONObject(key).getString("displayName");
                String followingPictureUrl = followingJson.getJSONObject(key).getString("profilePictureUrl");
                User accountToFollow = new Account(followingUsername, "");
                accountToFollow.setDisplayName(followingDisplayName);
                accountToFollow.setProfilePictureUrl(followingPictureUrl);
                following.put(key, accountToFollow);
            }
            account.setFollowingAccounts(following);
        }

        if (userJson.has("blockedAccounts")) {
            HashMap<String, User> blocked = new HashMap<>();
            JSONObject blockedJson = userJson.getJSONObject("blockedAccounts");
            for (String key : blockedJson.keySet()) {
                String blockedUsername = blockedJson.getJSONObject(key).getString("username");
                String blockedDisplayName = blockedJson.getJSONObject(key).getString("displayName");
                String blockedPictureUrl = blockedJson.getJSONObject(key).getString("profilePictureUrl");
                User accountToBlock = new Account(blockedUsername, "");
                accountToBlock.setDisplayName(blockedDisplayName);
                accountToBlock.setProfilePictureUrl(blockedPictureUrl);
                blocked.put(key, accountToBlock);
            }
            account.setBlockedAccounts(blocked);
        }

        if (userJson.has("mutedAccounts")) {
            ArrayList<User> muted = new ArrayList<>();
            JSONArray mutedArray = userJson.getJSONArray("mutedAccounts");
            for (int i = 0; i < mutedArray.length(); i++) {
                String mutedUsername = mutedArray.getJSONObject(i).getString("username");
                String mutedDisplayName = mutedArray.getJSONObject(i).getString("displayName");
                String mutedPictureUrl = mutedArray.getJSONObject(i).getString("profilePictureUrl");
                User accountToMute = new Account(mutedUsername, "");
                accountToMute.setDisplayName(mutedDisplayName);
                accountToMute.setProfilePictureUrl(mutedPictureUrl);
                muted.add(accountToMute);
            }
            account.setMutedAccounts(muted);
        }

        // Load user posts
        if (userJson.has("userPosts")) {
            ArrayList<Long> posts = new ArrayList<>();
            JSONArray postsJsonArray = userJson.getJSONArray("userPosts");
            for (int i = 0; i < postsJsonArray.length(); i++) {
                posts.add(postsJsonArray.getLong(i));
            }
            account.setUserPosts(posts);
        }

        return account;
    }

    @Override
    public void addPost(long id, String username) {
        User user = get(username);
        user.getUserPosts().add(id);
        save(user);
    }

    @Override
    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    @Override
    public void changePassword(User user) {
        save(user); // Since save() updates all user data
    }

    @Override
    public void updateDisplayName(User user, String newDisplayName) {
        user.setDisplayName(newDisplayName);
        save(user);
    }

    @Override
    public void updateBio(User user, String newBio) {
        user.setBio(newBio);
        save(user);
    }

    @Override
    public void updateProfilePictureUrl(User user, String newProfilePictureUrl) {
        user.setProfilePictureUrl(newProfilePictureUrl);
        save(user);
    }

    @Override
    public void updatePreferences(User user, ArrayList<String> newPreferences) {
        user.setFoodPreferences(newPreferences);
        save(user);
    }

    @Override
    public void removeFollower(String username, String removedUsername) {
        User user = get(username);
        User removedUser = get(removedUsername);
        user.getFollowerAccounts().remove(removedUsername);
        removedUser.getFollowingAccounts().remove(username);
        save(user);
        save(removedUser);
    }

    @Override
    public void removeFollowing(String username, String removedUsername) {
        User user = get(username);
        User removedUser = get(removedUsername);
        user.getFollowingAccounts().remove(removedUsername);
        removedUser.getFollowerAccounts().remove(username);
        save(user);
        save(removedUser);
    }

    @Override
    public void setPrivacy(User user, boolean isPublic) {
        user.setPublic(isPublic);
        save(user);
    }

    @Override
    public void setNotificationStatus(User user, boolean enabled) {
        user.setNotificationsEnabled(enabled);
        save(user);
    }

    @Override
    public boolean canFollow(String username, String otherUsername){
        if (get(username) != null && get(otherUsername) != null) {
            User user = get(username);
            return !user.getFollowingAccounts().containsKey(otherUsername) && !username.equals(otherUsername);
        }
        else{
            return false;
        }
    }

    @Override
    public void addFollowing(String username, String otherUsername){
        User user = get(username);
        User followedUser = get(otherUsername);
        user.getFollowingAccounts().put(otherUsername, followedUser);
        followedUser.getFollowerAccounts().put(username, user);
        save(user);
        save(followedUser);
    }

    @Override
    public void deleteAccount(String username) {
        try {
            JSONObject data = getJsonObject();
            if (!data.has("users")) {
                data.put("users", new JSONObject());
            }

            JSONObject users = data.getJSONObject("users");
            if (users.has(username)) {
                JSONObject user = users.getJSONObject(username);
                JSONObject followingAccounts = user.getJSONObject("followingAccounts");
                for (String followedAccountUsername : followingAccounts.keySet()) {
                    JSONObject fullFollowedAccount = users.getJSONObject(followedAccountUsername);
                    JSONObject followedAccountFollowerMap = fullFollowedAccount.getJSONObject("followerAccounts");
                    followedAccountFollowerMap.remove(username);
                    fullFollowedAccount.put("followerAccounts", followedAccountFollowerMap);
                    users.put(followedAccountUsername, fullFollowedAccount);
                }

                JSONObject followerAccounts = user.getJSONObject("followerAccounts");
                for (String followerAccountUsername : followerAccounts.keySet()) {
                    JSONObject fullFollowerAccount = users.getJSONObject(followerAccountUsername);
                    JSONObject followerAccountFollowingMap = fullFollowerAccount.getJSONObject("followingAccounts");
                    followerAccountFollowingMap.remove(username);
                    fullFollowerAccount.put("followingAccounts", followerAccountFollowingMap);
                    users.put(followerAccountUsername, fullFollowerAccount);
                }

                users.remove(username);
                data.put("users", users);
                saveJSONObject(data);
            }
            else {
                System.out.println("User not found, delete unsuccessful");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
