package entity;

import java.util.ArrayList;

/**
 * An implementation of the User interface.
 */
public class Account implements User {
    private String username;
    private String password;
    private String name;
    private String email;
    private String bio;
    private ArrayList<Account> followerAccounts;
    private ArrayList<Account> blockedAccounts;
    private ArrayList<String> blockedTerms;
    private ArrayList<Account> mutedAccounts;
    private ArrayList<String> foodPreferences;
    private ArrayList<Integer> likesUsernames;
    private ArrayList<String> followingAccounts;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        likesUsernames = new ArrayList<>();
        followingAccounts = new ArrayList<>();
        followerAccounts = new ArrayList<>();
        blockedAccounts = new ArrayList<>();
        blockedTerms = new ArrayList<>();
        mutedAccounts = new ArrayList<>();
        foodPreferences = new ArrayList<>();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ArrayList<Integer> getLikesUsernames() {
        return likesUsernames;
    }

    public void setLikesUsernames(ArrayList<Integer> likesUsernames) {
        this.likesUsernames = likesUsernames;
    }

    public ArrayList<Account> getFollowerAccounts() {
        return followerAccounts;
    }

    public void setFollowerAccounts(ArrayList<Account> followerAccounts) {
        this.followerAccounts = followerAccounts;
    }

    public ArrayList<Account> getBlockedAccounts() {
        return blockedAccounts;
    }

    public void setBlockedAccounts(ArrayList<Account> blockedAccounts) {
        this.blockedAccounts = blockedAccounts;
    }

    public ArrayList<String> getBlockedTerms() {
        return blockedTerms;
    }

    public void setBlockedTerms(ArrayList<String> blockedTerms) {
        this.blockedTerms = blockedTerms;
    }

    public ArrayList<Account> getMutedAccounts() {
        return mutedAccounts;
    }

    public void setMutedAccounts(ArrayList<Account> mutedAccounts) {
        this.mutedAccounts = mutedAccounts;
    }

    public ArrayList<String> getFoodPreferences() {
        return foodPreferences;
    }

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
        likesUsernames.remove( (Integer) p.getID());
    }


    public void setFriends(ArrayList<Account> friends) {
        followingAccounts = new ArrayList<>();
        for (Account account : friends) {
            followingAccounts.add(account.getUsername());
        }
    }

    public void setFollowingAccounts(ArrayList<String> followingAccounts) {
        this.followingAccounts = followingAccounts;
    }

    public ArrayList<String> getFollowingAccounts() {
        return followingAccounts;
    }

    public boolean isFriend(Account account) {
        return followingAccounts.contains(account.getUsername());
    }

    public void addFriend(Account f) {
        followingAccounts.add(f.getUsername());
    }

    public void removeFriend(Account f) {
        followingAccounts.remove(f.getUsername());
    }
}
