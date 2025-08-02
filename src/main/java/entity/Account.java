package entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An implementation of the User interface.
 */
public class Account implements User {
    private String profilePictureUrl;
    private String username;
    private String password;
    private String displayName;
    private String email;
    private String bio;
    private HashMap<String, User> followerAccounts;
    private HashMap<String, User> followingAccounts;
    private HashMap<String, User> blockedAccounts;
    private ArrayList<String> blockedTerms;
    private ArrayList<User> mutedAccounts;
    private ArrayList<String> foodPreferences;
    private ArrayList<Long> likesUsernames;
    private HashMap<Long, Post> userPosts;
    private boolean isPublic;

    public Account(String username, String password) {
        this.profilePictureUrl = "https://i.imgur.com/eA9NeJ1.jpeg";
        this.username = username;
        this.password = password;
        likesUsernames = new ArrayList<>();
        followingAccounts = new HashMap<>();
        followerAccounts = new HashMap<>();
        blockedAccounts = new HashMap<>();
        blockedTerms = new ArrayList<>();
        mutedAccounts = new ArrayList<>();
        foodPreferences = new ArrayList<>();
        userPosts = new HashMap<>();
        isPublic = true;
    }
    @Override
    public boolean isPublic(){
        return isPublic;
    }

    @Override
    public void setPublic(boolean isPublic){
        this.isPublic = isPublic;
    }

    @Override
    public String getProfilePictureUrl() {return profilePictureUrl;}

    @Override
    public void setProfilePictureUrl(String profilePictureUrl) {this.profilePictureUrl = profilePictureUrl;}

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setDisplayName(String newDisplayName) {
        this.displayName = newDisplayName;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getBio() {
        return bio;
    }

    @Override
    public void setBio(String bio) {
        this.bio = bio;
    }

    public ArrayList<Long> getLikesUsernames() {
        return likesUsernames;
    }

    public void setLikesUsernames(ArrayList<Long> likesUsernames) {
        this.likesUsernames = likesUsernames;
    }

    @Override
    public HashMap<String, User> getFollowerAccounts() {
        return followerAccounts;
    }

    @Override
    public void setFollowerAccounts(HashMap<String, User> followerAccounts) {
        this.followerAccounts = followerAccounts;
    }

    public HashMap<String, User> getBlockedAccounts() {
        return blockedAccounts;
    }

    public void setBlockedAccounts(HashMap<String, User> blockedAccounts) {
        this.blockedAccounts = blockedAccounts;
    }

    public ArrayList<String> getBlockedTerms() {
        return blockedTerms;
    }

    public void setBlockedTerms(ArrayList<String> blockedTerms) {
        this.blockedTerms = blockedTerms;
    }

    public ArrayList<User> getMutedAccounts() {
        return mutedAccounts;
    }

    public void setMutedAccounts(ArrayList<User> mutedAccounts) {
        this.mutedAccounts = mutedAccounts;
    }

    @Override
    public ArrayList<String> getFoodPreferences() {
        return foodPreferences;
    }

    @Override
    public void setFoodPreferences(ArrayList<String> foodPreferences) {
        this.foodPreferences = foodPreferences;
    }

    public boolean liked(Post p) {
        return likesUsernames.contains(p.getID());
    }

    public void like(Post p) {
        likesUsernames.add(p.getID());
    }

    public void dislike(Post p) {
        likesUsernames.remove(p.getID());
    }

    public void setFriends(ArrayList<User> friends) {
        followingAccounts = new HashMap<>();
        for (User account : friends) {
            followingAccounts.put(account.getUsername(), account);
        }
    }

    @Override
    public void setFollowingAccounts(HashMap<String, User> followingAccounts) {
        this.followingAccounts = followingAccounts;
    }

    @Override
    public HashMap<String, User> getFollowingAccounts() {
        return followingAccounts;
    }

    public boolean isFriend(User account) {
        return followingAccounts.containsKey(account.getUsername());
    }

    public void addFriend(User f) {
        followingAccounts.put(f.getUsername(), f);
    }

    public void removeFriend(User f) {
        followingAccounts.remove(f.getUsername());
    }

    @Override
    public int getNumFollowers() {
        return followerAccounts.size();
    }

    @Override
    public int getNumFollowing() {
        return followingAccounts.size();
    }

    /**
     * Basic debugging toString method
     *
     * @return postID, title, user, ingredients
     */
    @Override
    public String toString() {
        return "Account username=" + username + ", password=" + password + ", name=" + displayName;
    }

    @Override
    public HashMap<Long, Post> getUserPosts() {
        return userPosts;
    }

    @Override
    public void setUserPosts(HashMap<Long, Post> userPosts) {
        this.userPosts = userPosts;
    }
}
