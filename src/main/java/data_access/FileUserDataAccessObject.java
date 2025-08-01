package data_access;

import entity.Account;
import entity.Post;
import entity.User;
import org.json.JSONObject;
import org.json.JSONArray;
import use_case.UserDataAccessInterface;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.edit_profile.EditProfileUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.manage_followers.ManageFollowersUserDataAccessInterface;
import use_case.manage_following.ManageFollowingUserDataAccessInterface;
import use_case.note.DataAccessException;
import use_case.note.NoteDataAccessInterface;
import use_case.profile.ProfileUserDataAccessInterface;
import use_case.settings.SettingsUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FileUserDataAccessObject implements
        UserDataAccessInterface,
        SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        NoteDataAccessInterface,
        SettingsUserDataAccessInterface,
        ProfileUserDataAccessInterface,
        EditProfileUserDataAccessInterface,
        ManageFollowingUserDataAccessInterface,
        ManageFollowersUserDataAccessInterface {

    private final String filePath = "src/main/java/data_access/user_data.json";
    private String currentUsername;

    /**
     * Gets the JSON object from the file, creating an empty one if the file doesn't exist
     */
    private JSONObject getJsonObject() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONObject(content);
        } catch (IOException e) {
            return new JSONObject();
        }
    }

    /**
     * Writes the JSON object to the file
     */
    private void writeToFile(JSONObject data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
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
        userJson.put("name", account.getUsername());
        userJson.put("email", account.getEmail());
        userJson.put("bio", account.getBio());

        // Store lists and maps
        userJson.put("likesUsernames", new JSONArray(account.getLikesUsernames()));
        userJson.put("followingAccounts", new JSONArray(account.getFollowingAccounts()));
        userJson.put("blockedTerms", new JSONArray(account.getBlockedTerms()));
        userJson.put("foodPreferences", new JSONArray(account.getFoodPreferences()));

        // Store maps (only store usernames, reconstruct objects on load)
        JSONObject followerAccountsJson = new JSONObject();
        for (String key : account.getFollowerAccounts().keySet()) {
            followerAccountsJson.put(key, account.getFollowerAccounts().get(key).getUsername());
        }
        userJson.put("followerAccounts", followerAccountsJson);

        JSONObject blockedAccountsJson = new JSONObject();
        for (String key : account.getBlockedAccounts().keySet()) {
            blockedAccountsJson.put(key, account.getBlockedAccounts().get(key).getUsername());
        }
        userJson.put("blockedAccounts", blockedAccountsJson);

        // Store muted accounts (usernames only)
        JSONArray mutedAccountsJson = new JSONArray();
        for (Account mutedAccount : account.getMutedAccounts()) {
            mutedAccountsJson.put(mutedAccount.getUsername());
        }
        userJson.put("mutedAccounts", mutedAccountsJson);

        // Store user posts
        if (account.getUserPosts() != null) {
            JSONObject userPostsJson = new JSONObject();
            for (Long postId : account.getUserPosts().keySet()) {
                Post post = account.getUserPosts().get(postId);
                JSONObject postJson = new JSONObject();
                postJson.put("id", post.getID());
                postJson.put("title", post.getTitle());
                postJson.put("description", post.getDescription());
                userPostsJson.put(String.valueOf(postId), postJson);
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
        account.setName(userJson.optString("name", ""));
        account.setEmail(userJson.optString("email", ""));
        account.setBio(userJson.optString("bio", ""));

        // Load lists
        if (userJson.has("likesUsernames")) {
            ArrayList<Long> likes = new ArrayList<>();
            JSONArray likesArray = userJson.getJSONArray("likesUsernames");
            for (int i = 0; i < likesArray.length(); i++) {
                likes.add(likesArray.getLong(i));
            }
            account.setLikesUsernames(likes);
        }

        if (userJson.has("followingAccounts")) {
            ArrayList<String> following = new ArrayList<>();
            JSONArray followingArray = userJson.getJSONArray("followingAccounts");
            for (int i = 0; i < followingArray.length(); i++) {
                following.add(followingArray.getString(i));
            }
            account.setFollowingAccounts(following);
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
        // Note: For follower, blocked, and muted accounts, we only store usernames and
        // create stub Account objects. Full account data would need to be loaded separately if needed.

        if (userJson.has("followerAccounts")) {
            HashMap<String, Account> followers = new HashMap<>();
            JSONObject followersJson = userJson.getJSONObject("followerAccounts");
            for (String key : followersJson.keySet()) {
                String followerUsername = followersJson.getString(key);
                followers.put(key, new Account(followerUsername, ""));
            }
            account.setFollowerAccounts(followers);
        }

        if (userJson.has("blockedAccounts")) {
            HashMap<String, Account> blocked = new HashMap<>();
            JSONObject blockedJson = userJson.getJSONObject("blockedAccounts");
            for (String key : blockedJson.keySet()) {
                String blockedUsername = blockedJson.getString(key);
                blocked.put(key, new Account(blockedUsername, ""));
            }
            account.setBlockedAccounts(blocked);
        }

        if (userJson.has("mutedAccounts")) {
            ArrayList<Account> muted = new ArrayList<>();
            JSONArray mutedArray = userJson.getJSONArray("mutedAccounts");
            for (int i = 0; i < mutedArray.length(); i++) {
                muted.add(new Account(mutedArray.getString(i), ""));
            }
            account.setMutedAccounts(muted);
        }

        // Load user posts
        if (userJson.has("userPosts")) {
            HashMap<Long, Post> posts = new HashMap<>();
            JSONObject postsJson = userJson.getJSONObject("userPosts");
            for (String postId : postsJson.keySet()) {
                JSONObject postJson = postsJson.getJSONObject(postId);
                Post post = new Post(account, postJson.getLong("id"),
                    postJson.getString("title"), postJson.getString("description"));
                posts.put(Long.parseLong(postId), post);
            }
            account.setUserPosts(posts);
        }

        return account;
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
}

