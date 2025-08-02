package data_access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.User;
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

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements
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

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, String> notes = new HashMap<>();

    private String currentUsername;

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
        user.getFollowerAccounts().remove(removedUsername);
        save(user);
    }

    @Override
    public void removeFollowing(String username, String removedUsername) {
        User user = get(username);
        user.getFollowingAccounts().remove(removedUsername);
        save(user);
    }
}