package entity;

import java.util.ArrayList;

/**
 * A simple implementation of the User interface.
 */
public class Account implements User {

    private final String name;
    private final String password;

    private String bio;
    private final ArrayList<Account> followingAccounts = new ArrayList<>();
    private final ArrayList<Account> followerAccounts = new ArrayList<>();
    private final ArrayList<Account> blockedAccounts = new ArrayList<>();
    private final ArrayList<String> blockedTerms = new ArrayList<>();
    private final ArrayList<Account> mutedAccounts = new ArrayList<>();
    private final ArrayList<String> foodPreferences = new ArrayList<>();

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Length of bio is less than 200 characters.
    public String editBio(String bio) {
        if (bio.length() <= 200) {
            this.bio = bio;
        }
        return this.bio;
    }


    public void toggleFollowing(Account following) {
        // TODO: add if Account following is a valid username (already created)
        if (this.followerAccounts.contains(following)) {
            this.followerAccounts.remove(following);
        }
        else {
            this.followerAccounts.add(following);
        }
    }

    // TODO: how to see follower list


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getBio() {
        return bio;
    }

    public ArrayList<Account> getFollowingAccounts() {
        return followingAccounts;
    }

    public ArrayList<String> getBlockedTerms() {
        return blockedTerms;
    }

    public ArrayList<Account> getMutedAccounts() {
        return mutedAccounts;
    }

    public ArrayList<String> getFoodPreferences() {
        return foodPreferences;
    }
}
