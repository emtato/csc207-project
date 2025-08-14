package data_access;

import java.io.IOException;
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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The DAO for user data.
 */
public class DBUserDataAccessObject implements UserDataAccessObject {
    private static UserDataAccessObject instance;
    private JSONObject cachedUsersRoot; // cache of full users JSON object (the 'users' JSONObject)
    private long cacheTimestampMs = 0L;
    private static final long CACHE_TTL_MS = 5_000; // 5 seconds; adjust if needed
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
                return data.getJSONObject(DATA_KEY);
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

    /**
     * Writes the JSON object to the "database"
     */
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

    private synchronized JSONObject getUsersRootCached() throws DataAccessException {
        long now = System.currentTimeMillis();
        if (cachedUsersRoot != null && (now - cacheTimestampMs) < CACHE_TTL_MS) {
            return cachedUsersRoot;
        }
        int attempts = 0;
        DataAccessException last = null;
        while (attempts < 3) {
            attempts++;
            try {
                JSONObject data = getJsonObject();
                cachedUsersRoot = data.has("users") ? data.getJSONObject("users") : new JSONObject();
                cacheTimestampMs = System.currentTimeMillis();
                return cachedUsersRoot;
            } catch (DataAccessException e) {
                last = e;
                // Simple backoff
                try { Thread.sleep(150L * attempts); } catch (InterruptedException ignored) {}
            }
        }
        throw last != null ? last : new DataAccessException("Unknown error loading users");
    }

    // Helper to build an Account from its JSON (subset needed for club creation/member selection)
    private Account buildAccountFromJson(String username, JSONObject userJson) {
        Account account = new Account(userJson.optString("username", username), userJson.optString("password", ""));
        account.setDisplayName(userJson.optString("displayName", ""));
        account.setEmail(userJson.optString("email", ""));
        account.setBio(userJson.optString("bio", ""));
        account.setProfilePictureUrl(userJson.optString("profilePictureUrl", "https://i.imgur.com/eA9NeJ1.jpeg"));
        account.setPublic(userJson.optBoolean("isPublic", true));
        account.setNotificationsEnabled(userJson.optBoolean("notificationsEnabled", true));
        if (userJson.has("clubs")) {
            ArrayList<String> clubs = new ArrayList<>();
            JSONArray arr = userJson.getJSONArray("clubs");
            for (int i = 0; i < arr.length(); i++) clubs.add(arr.optString(i));
            account.setClubs(clubs);
        }

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

        if (userJson.has("requestedAccounts")) {
            HashMap<String, User> requested = new HashMap<>();
            JSONObject requestedJson = userJson.getJSONObject("requestedAccounts");
            for (String key : requestedJson.keySet()) {
                String requestedUsername = requestedJson.getJSONObject(key).getString("username");
                String requestedDisplayName = requestedJson.getJSONObject(key).getString("displayName");
                String requestedPictureUrl = requestedJson.getJSONObject(key).getString("profilePictureUrl");
                User accountToRequest = new Account(requestedUsername, "");
                accountToRequest.setDisplayName(requestedDisplayName);
                accountToRequest.setProfilePictureUrl(requestedPictureUrl);
                requested.put(key, accountToRequest);
            }
            account.setRequestedAccounts(requested);
        }

        if (userJson.has("requesterAccounts")) {
            HashMap<String, User> requesters = new HashMap<>();
            JSONObject requesterJson = userJson.getJSONObject("requesterAccounts");
            for (String key : requesterJson.keySet()) {
                String requesterUsername = requesterJson.getJSONObject(key).getString("username");
                String requesterDisplayName = requesterJson.getJSONObject(key).getString("displayName");
                String requesterPictureUrl = requesterJson.getJSONObject(key).getString("profilePictureUrl");
                User requester = new Account(requesterUsername, "");
                requester.setDisplayName(requesterDisplayName);
                requester.setProfilePictureUrl(requesterPictureUrl);
                requesters.put(key, requester);
            }
            account.setRequesterAccounts(requesters);
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
    public boolean existsByName(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) return false;
        try {
            JSONObject usersRoot = getUsersRootCached();
            if (usersRoot.has(identifier)) return true;
            // Fallback: force refresh once if not found
            cachedUsersRoot = null; cacheTimestampMs = 0L;
            try {
                JSONObject refreshed = getUsersRootCached();
                if (refreshed.has(identifier)) return true;
            } catch (DataAccessException ignored) {}
            // Last resort: direct raw fetch bypassing cache
            try {
                JSONObject raw = getJsonObject();
                if (raw.has("users") && raw.getJSONObject("users").has(identifier)) return true;
            } catch (DataAccessException ignored) {}
            return false;
        } catch (DataAccessException e) {
            // Direct attempt if cache retrieval failed
            try {
                JSONObject raw = getJsonObject();
                return raw.has("users") && raw.getJSONObject("users").has(identifier);
            } catch (DataAccessException ignored) { return false; }
        }
    }

    @Override
    public ArrayList<Account> getAllUsers() {
        ArrayList<Account> allUsers = new ArrayList<>();
        try {
            JSONObject usersRoot = getUsersRootCached();
            System.out.println("[DBUserDAO][DEBUG] getAllUsers(): cached/root key count=" + usersRoot.keySet().size());
            if (usersRoot.keySet().size() <= 10) {
                System.out.println("[DBUserDAO][DEBUG] Keys (cached): " + usersRoot.keySet());
            }
            for (String username : usersRoot.keySet()) {
                try {
                    JSONObject uJson = usersRoot.getJSONObject(username);
                    allUsers.add(buildAccountFromJson(username, uJson));
                } catch (Exception ex) {
                    System.out.println("[DBUserDAO][WARN] Skipping user '" + username + "' due to error: " + ex.getMessage());
                }
            }
            // If suspiciously few users (<3) force one cache refresh attempt (might have been rate-limited)
            if (allUsers.size() < 3) {
                System.out.println("[DBUserDAO][DEBUG] User count " + allUsers.size() + " < 3, forcing fresh fetch bypassing cache");
                cachedUsersRoot = null; // invalidate
                try {
                    JSONObject usersRoot2 = getUsersRootCached();
                    System.out.println("[DBUserDAO][DEBUG] After refresh key count=" + usersRoot2.keySet().size());
                    if (usersRoot2.keySet().size() <= 10) {
                        System.out.println("[DBUserDAO][DEBUG] Keys (refreshed): " + usersRoot2.keySet());
                    }
                    allUsers.clear();
                    for (String username : usersRoot2.keySet()) {
                        try { allUsers.add(buildAccountFromJson(username, usersRoot2.getJSONObject(username))); } catch (Exception ignored) {}
                    }
                } catch (Exception e2) {
                    System.out.println("[DBUserDAO][WARN] Fresh fetch failed: " + e2.getMessage());
                }
            }
            // Second heuristic: If still small (<5) attempt a direct raw fetch ignoring cache method
            if (allUsers.size() < 5) {
                try {
                    JSONObject raw = getJsonObject();
                    if (raw.has("users")) {
                        JSONObject rawUsers = raw.getJSONObject("users");
                        System.out.println("[DBUserDAO][DEBUG] Direct raw fetch user key count=" + rawUsers.keySet().size());
                        if (rawUsers.keySet().size() <= 10) {
                            System.out.println("[DBUserDAO][DEBUG] Keys (raw): " + rawUsers.keySet());
                        }
                        // If raw has more entries than we loaded, rebuild list
                        if (rawUsers.keySet().size() > allUsers.size()) {
                            allUsers.clear();
                            for (String username : rawUsers.keySet()) {
                                try { allUsers.add(buildAccountFromJson(username, rawUsers.getJSONObject(username))); } catch (Exception ignored) {}
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("[DBUserDAO][WARN] Direct raw fetch failed: " + ex.getMessage());
                }
            }
        } catch (DataAccessException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        System.out.println("[DBUserDAO][DEBUG] getAllUsers() final count=" + allUsers.size());
        return allUsers;
    }

    @Override
    public void save(User user) {
        // Invalidate cache so subsequent reads reflect latest state
        cachedUsersRoot = null;
        cacheTimestampMs = 0L;

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
        userJson.put("location", account.getLocation());

        // Persist clubs list (missing before)
        userJson.put("clubs", new JSONArray(account.getClubs() == null ? new ArrayList<>() : account.getClubs()));

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

        JSONObject requestedAccountsJson = new JSONObject();
        for (String key : account.getRequestedAccounts().keySet()) {
            JSONObject requestedJson = new JSONObject();
            requestedJson.put("username", account.getRequestedAccounts().get(key).getUsername());
            requestedJson.put("displayName", account.getRequestedAccounts().get(key).getDisplayName());
            requestedJson.put("profilePictureUrl", account.getRequestedAccounts().get(key).getProfilePictureUrl());
            requestedAccountsJson.put(key, requestedJson);
        }
        userJson.put("requestedAccounts", requestedAccountsJson);

        JSONObject requesterAccountsJson = new JSONObject();
        for (String key : account.getRequesterAccounts().keySet()) {
            JSONObject requesterJson = new JSONObject();
            requesterJson.put("username", account.getRequesterAccounts().get(key).getUsername());
            requesterJson.put("displayName", account.getRequesterAccounts().get(key).getDisplayName());
            requesterJson.put("profilePictureUrl", account.getRequesterAccounts().get(key).getProfilePictureUrl());
            requesterAccountsJson.put(key, requesterJson);
        }
        userJson.put("requesterAccounts", requesterAccountsJson);

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
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private Account ensureUserExists(String username) {
        User existing = null;
        try { existing = get(username); } catch (Exception ignored) {}
        if (existing instanceof Account) {
            return (Account) existing;
        }
        // Pure DB-only stub creation (no file/session bridging)
        System.out.println("DEBUG[DBUserDAO]: Creating stub DB user for missing username: " + username);
        Account stub = new Account(username, "");
        stub.setClubs(new ArrayList<>());
        stub.setUserPosts(new ArrayList<>());
        save(stub);
        return stub;
    }

    @Override
    public User get(String username) {
        // Try cache first to avoid extra HTTP call
        try {
            JSONObject usersRoot = getUsersRootCached();
            if (usersRoot.has(username)) {
                return buildAccountFromJson(username, usersRoot.getJSONObject(username));
            }
        } catch (DataAccessException ignored) {}
        // Fallback to original logic if not in cache or cache retrieval failed
        JSONObject data = new JSONObject();
        try { data = getJsonObject(); } catch (DataAccessException ex) {}
        if (!data.has("users")) { return null; }
        JSONObject users = data.getJSONObject("users");
        if (!users.has(username)) { return null; }
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

        // Load clubs list
        if (userJson.has("clubs")) {
            ArrayList<String> clubs = new ArrayList<>();
            JSONArray clubsArray = userJson.getJSONArray("clubs");
            for (int i = 0; i < clubsArray.length(); i++) {
                clubs.add(clubsArray.getString(i));
            }
            account.setClubs(clubs);
        }

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

        if (userJson.has("requestedAccounts")) {
            HashMap<String, User> requested = new HashMap<>();
            JSONObject requestedJson = userJson.getJSONObject("requestedAccounts");
            for (String key : requestedJson.keySet()) {
                String requestedUsername = requestedJson.getJSONObject(key).getString("username");
                String requestedDisplayName = requestedJson.getJSONObject(key).getString("displayName");
                String requestedPictureUrl = requestedJson.getJSONObject(key).getString("profilePictureUrl");
                User accountToRequest = new Account(requestedUsername, "");
                accountToRequest.setDisplayName(requestedDisplayName);
                accountToRequest.setProfilePictureUrl(requestedPictureUrl);
                requested.put(key, accountToRequest);
            }
            account.setRequestedAccounts(requested);
        }

        if (userJson.has("requesterAccounts")) {
            HashMap<String, User> requesters = new HashMap<>();
            JSONObject requesterJson = userJson.getJSONObject("requesterAccounts");
            for (String key : requesterJson.keySet()) {
                String requesterUsername = requesterJson.getJSONObject(key).getString("username");
                String requesterDisplayName = requesterJson.getJSONObject(key).getString("displayName");
                String requesterPictureUrl = requesterJson.getJSONObject(key).getString("profilePictureUrl");
                User requester = new Account(requesterUsername, "");
                requester.setDisplayName(requesterDisplayName);
                requester.setProfilePictureUrl(requesterPictureUrl);
                requesters.put(key, requester);
            }
            account.setRequesterAccounts(requesters);
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
        Account user = ensureUserExists(username);
        if (user.getUserPosts() == null) user.setUserPosts(new ArrayList<>());
        if (!user.getUserPosts().contains(id)) {
            user.getUserPosts().add(id);
        }
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
    public void updateLocation(User user, String newLocation) {
        user.setLocation(newLocation);
        save(user);
    }

    @Override
    public void removeFollower(String username, String removedUsername) {
        final User userObj = get(username);
        final User removedUserObj = get(removedUsername);
        if (userObj == null || removedUserObj == null) return;
        final Account user = (Account) userObj;
        final Account removedUser = (Account) removedUserObj;
        user.getFollowerAccounts().remove(removedUsername);
        removedUser.getFollowingAccounts().remove(username);
        save(user);
        save(removedUser);
    }

    @Override
    public void removeFollowing(String username, String removedUsername) {
        User userObj = get(username);
        User removedUserObj = get(removedUsername);
        if (userObj == null || removedUserObj == null) return;
        Account user = (Account) userObj;
        Account removedUser = (Account) removedUserObj;
        user.getFollowingAccounts().remove(removedUsername);
        removedUser.getFollowerAccounts().remove(username);
        save(user);
        save(removedUser);
    }

    @Override
    public void removeFollowRequest(String username, String removedUsername) {
        final User userObj = get(username);
        final User removedUserObj = get(removedUsername);
        if (userObj == null || removedUserObj == null) return;
        final Account user = (Account) userObj;
        final Account removedUser = (Account) removedUserObj;
        removedUser.getRequesterAccounts().remove(username);
        user.getRequestedAccounts().remove(removedUsername);
        save(user);
        save(removedUser);
    }

    @Override
    public void removeFollowRequester(String username, String removedUsername) {
        final User userObj = get(username);
        final User removedUserObj = get(removedUsername);
        if (userObj == null || removedUserObj == null) return;
        final Account user = (Account) userObj;
        final Account removedUser = (Account) removedUserObj;
        removedUser.getRequestedAccounts().remove(username);
        user.getRequesterAccounts().remove(removedUsername);
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
    public boolean canRequestFollow(String username, String otherUsername) {
        final User user = get(username);
        final User otherUser = get(otherUsername);
        if (user == null || otherUser == null) return false;
        return otherUser != null && !((Account) user).getRequestedAccounts().containsKey(otherUsername)
                && !username.equals(otherUsername) && !((Account) user).getFollowingAccounts().containsKey(otherUsername)
                && !((Account) otherUser).isPublic();
    }

    @Override
    public boolean canFollow(String username, String otherUsername) {
        final User user = get(username);
        final User otherUser = get(otherUsername);
        if (user == null || otherUser == null) return false;
        return otherUser != null && !((Account) user).getFollowingAccounts().containsKey(otherUsername)
                && !username.equals(otherUsername) && ((Account) otherUser).isPublic();
    }

    @Override
    public void addFollowRequest(String username, String otherUsername) {
        final Account user = ensureUserExists(username);
        final Account requestedUser = ensureUserExists(otherUsername);
        user.getRequestedAccounts().put(otherUsername, requestedUser);
        requestedUser.getRequesterAccounts().put(username, user);
        save(requestedUser);
        save(user);
    }

    @Override
    public void addFollowing(String username, String otherUsername) {
        final Account user = ensureUserExists(username);
        final Account followedUser = ensureUserExists(otherUsername);
        user.getFollowingAccounts().put(otherUsername, followedUser);
        followedUser.getFollowerAccounts().put(username, user);
        if (user.getRequestedAccounts().containsKey(otherUsername)) {
            user.getRequestedAccounts().remove(otherUsername);
        }
        if (followedUser.getRequesterAccounts().containsKey(username)) {
            followedUser.getRequesterAccounts().remove(username);
        }
        save(user);
        save(followedUser);
    }

    @Override
    public void deleteAccount(String username) {
        try {
            final JSONObject data = getJsonObject();
            if (!data.has("users")) {
                data.put("users", new JSONObject());
            }

            final JSONObject users = data.getJSONObject("users");
            if (users.has(username)) {
                final JSONObject user = users.getJSONObject(username);
                final JSONObject followingAccounts = user.getJSONObject("followingAccounts");
                for (String followedAccountUsername : followingAccounts.keySet()) {
                    final JSONObject fullFollowedAccount = users.getJSONObject(followedAccountUsername);
                    final JSONObject followedAccountFollowerMap = fullFollowedAccount.getJSONObject("followerAccounts");
                    followedAccountFollowerMap.remove(username);
                    fullFollowedAccount.put("followerAccounts", followedAccountFollowerMap);
                    users.put(followedAccountUsername, fullFollowedAccount);
                }

                final JSONObject followerAccounts = user.getJSONObject("followerAccounts");
                for (String followerAccountUsername : followerAccounts.keySet()) {
                    final JSONObject fullFollowerAccount = users.getJSONObject(followerAccountUsername);
                    final JSONObject followerAccountFollowingMap = fullFollowerAccount.getJSONObject("followingAccounts");
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
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeClubFromUser(String username, String clubId) {
        try {
            User user = get(username);
            if (user instanceof Account) {
                Account account = (Account) user;
                ArrayList<String> clubs = account.getClubs();
                if (clubs != null && clubs.remove(clubId)) {
                    account.setClubs(clubs);
                    save(account);
                }
            }
        } catch (Exception e) {
            System.out.println("Error removing club from user: " + e.getMessage());
        }
    }

    @Override
    public void bulkRemoveClubFromUsers(String clubId, java.util.List<String> usernames) {
        if (clubId == null || usernames == null || usernames.isEmpty()) return;
        try {
            // Work directly on cached root or fresh fetch to minimize network calls
            cachedUsersRoot = null; cacheTimestampMs = 0L; // force refresh for consistency
            JSONObject usersRoot = getUsersRootCached();
            boolean changed = false;
            for (String uname : usernames) {
                if (uname == null) continue;
                if (!usersRoot.has(uname)) continue;
                try {
                    JSONObject uJson = usersRoot.getJSONObject(uname);
                    if (uJson.has("clubs")) {
                        JSONArray clubsArr = uJson.getJSONArray("clubs");
                        JSONArray newArr = new JSONArray();
                        boolean removed = false;
                        for (int i = 0; i < clubsArr.length(); i++) {
                            String cid = clubsArr.optString(i, null);
                            if (cid == null) continue;
                            if (cid.equals(clubId)) { removed = true; continue; }
                            newArr.put(cid);
                        }
                        if (removed) {
                            uJson.put("clubs", newArr);
                            usersRoot.put(uname, uJson);
                            changed = true;
                        }
                    }
                } catch (Exception ignored) {}
            }
            if (changed) {
                // Persist entire users root once
                JSONObject data = getJsonObject();
                data.put("users", usersRoot);
                saveJSONObject(data);
                // Invalidate cache after save
                cachedUsersRoot = null; cacheTimestampMs = 0L;
            }
        } catch (Exception ex) {
            System.out.println("[DBUserDAO][WARN] bulkRemoveClubFromUsers failed: " + ex.getMessage());
        }
    }

    @Override
    public void invalidateCache() {
        cachedUsersRoot = null;
        cacheTimestampMs = 0L;
        System.out.println("[DBUserDAO][DEBUG] User cache invalidated");
    }
}
