package data_access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.User;
import use_case.UserDataAccessInterface;
import use_case.create_post.CreatePostDataAccessInterface;
import use_case.note.DataAccessException;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements UserDataAccessObject {

    private static UserDataAccessObject instance;

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, String> notes = new HashMap<>();

    private String currentUsername;

    private InMemoryUserDataAccessObject() {
    }

    public static UserDataAccessObject getInstance() {
        if (instance == null) {
            instance = new InMemoryUserDataAccessObject();
        }
        return instance;
    }

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
        System.out.println("User " + user.getUsername() + " has been saved");
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void changePassword(User user) {
        // Replace the old entry with the new password
        users.put(user.getUsername(), user);
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
    public String saveNote(User user, String note) throws DataAccessException {
        notes.put(user.getUsername(), note);
        return loadNote(user);
    }

    @Override
    public String loadNote(User user) throws DataAccessException {
        return notes.get(user.getUsername());
    }

    @Override
    public void updateDisplayName(User user, String newDisplayName){
        user.setDisplayName(newDisplayName);
        save(user);
    }

    @Override
    public void updateBio(User user, String newBio){
        user.setBio(newBio);
        save(user);
    }

    @Override
    public void updateProfilePictureUrl(User user, String newProfilePictureUrl){
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
        removedUser.getFollowerAccounts().remove(username);
        user.getFollowingAccounts().remove(removedUsername);
        save(user);
        save(removedUser);
    }

    @Override
    public void setPrivacy(User user, boolean isPublic){
        user.setPublic(isPublic);
        save(user);
    }

    @Override
    public void setNotificationStatus(User user, boolean enabled){
        user.setNotificationsEnabled(enabled);
        save(user);
    }

    @Override
    public boolean canFollow(String username, String otherUsername){
        User user = get(username);
        return users.containsKey(otherUsername) && !user.getFollowingAccounts().containsKey(otherUsername);
    }

    @Override
    public void addFollowing(String username, String otherUsername) {
        User user = get(username);
        User followedUser = get(otherUsername);
        user.getFollowingAccounts().put(otherUsername, followedUser);
        followedUser.getFollowerAccounts().put(username, user);
        save(followedUser);
        save(user);
    }

    @Override
    public void addPost(long id, String username) {
        User user = get(username);
        user.getUserPosts().add(id);
        save(user);
    }

    @Override
    public void deleteAccount(String username) {
        User user = get(username);
        for (User followedAccount : user.getFollowingAccounts().values()) {
            followedAccount.getFollowerAccounts().remove(username);
            save(followedAccount);
        }
        for (User follower : user.getFollowerAccounts().values()) {
            follower.getFollowingAccounts().remove(username);
            save(follower);
        }
        users.remove(username);
    }
}