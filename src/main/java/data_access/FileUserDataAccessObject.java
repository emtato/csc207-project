package data_access;

import entity.Account;
import entity.Post;
import entity.User;
import org.json.JSONObject;
import org.json.JSONArray;
import use_case.note.DataAccessException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FileUserDataAccessObject implements UserDataAccessObject {

    private final String filePath = "src/main/java/data_access/user_data.json";
    private String currentUsername;

    /**
     * Gets the JSON object from the file, creating an empty one if the file doesn't exist
     */
    private JSONObject getJsonObject() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONObject(content);
        }
        catch (IOException e) {
            return new JSONObject();
        }
    }

    /**
     * Writes the JSON object to the file
     */
    private void writeToFile(JSONObject data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        }
        catch (IOException e) {
            throw new RuntimeException("Error writing to file", e);
        }
    }

    @Override
    public boolean existsByName(String identifier) {
        JSONObject data = getJsonObject();
        return data.has("users") && data.getJSONObject("users").has(identifier);
    }

    @Override
    public void save(User user) {
        // Cast User to Account since we're working with Account implementation
        Account account = (Account) user;
        JSONObject data = getJsonObject();

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
        writeToFile(data);
    }

    @Override
    public User get(String username) {
        JSONObject data = getJsonObject();
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
    public String saveNote(User user, String note) throws DataAccessException {
        JSONObject data = getJsonObject();

        if (!data.has("notes")) {
            data.put("notes", new JSONObject());
        }

        JSONObject notes = data.getJSONObject("notes");
        notes.put(user.getUsername(), note);
        data.put("notes", notes);

        writeToFile(data);
        return note;
    }

    @Override
    public String loadNote(User user) throws DataAccessException {
        JSONObject data = getJsonObject();

        if (!data.has("notes")) {
            return null;
        }

        JSONObject notes = data.getJSONObject("notes");
        return notes.optString(user.getUsername(), null);
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
}

