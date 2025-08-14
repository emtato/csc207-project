package entity;

import java.util.ArrayList;

public class Club {
    private final String name;
    private final long id;
    private String description;
    private final ArrayList<Account> members = new ArrayList<>();
    private final ArrayList<String> foodPreferences = new ArrayList<>();
    private final ArrayList<Post> posts = new ArrayList<>();
    private final ArrayList<String> tags = new ArrayList<>();
    private final String imageUrl; // new field for club profile image

    public Club(String name, String description, String imageUrl, ArrayList<Account> members, ArrayList<String> foodPreferences, ArrayList<Post> posts, long id, ArrayList<String> tags) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.imageUrl = imageUrl;
        if (members != null) {
            this.members.addAll(members);
        }
        if (foodPreferences != null) {
            this.foodPreferences.addAll(foodPreferences);
        }
        if (posts != null) {
            this.posts.addAll(posts);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    // Length of bio is less than 200 characters.
    public String editDescription(String description) {
        if (description.length() <= 200) {
            this.description = description;
        }
        return this.description;
    }

    public String getName() { return name; }
    public long getId() { return id; }
    public ArrayList<String> getTags() { return tags; }
    public String getImageUrl() { return imageUrl; }

    public void setDisplayName(String newDisplayName) { }
    public void setDescription(String newBio) { }
    public void setPreferences(String newPreferences) { }

    public String getDescription() { return description; }
    public ArrayList<Account> getMembers() { return members; }
    public ArrayList<String> getFoodPreferences() { return foodPreferences; }
    public ArrayList<Post> getPosts() {return posts;}
}
