package entity;

import java.util.ArrayList;

public class Club {
    private final String name;

    private String description;
    private final ArrayList<Account> members = new ArrayList<>();
    private final ArrayList<String> foodPreferences = new ArrayList<>();
    private final ArrayList<Post> posts = new ArrayList<>();

    public Club(String name, String bio, ArrayList<Account> members, ArrayList<String> foodPreferences, ArrayList<Post> posts) {
        this.name = name;
        this.description = bio;
        if (members != null) {
            this.members.addAll(members);
        }
        if (foodPreferences != null) {
            this.foodPreferences.addAll(foodPreferences);
        }
        if (posts != null) {
            this.posts.addAll(posts);
        }
    }

    // Length of bio is less than 200 characters.
    public String editDescription(String description) {
        if (description.length() <= 200) {
            this.description = description;
        }
        return this.description;
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String newDisplayName) {

    }

    public void setDescription(String newBio) {

    }

    public void setPreferences(String newPreferences) {

    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Account> getMembers() {
        return members;
    }

    public ArrayList<String> getFoodPreferences() {
        return foodPreferences;
    }

    public ArrayList<Post> getPosts() {return posts;}
}
