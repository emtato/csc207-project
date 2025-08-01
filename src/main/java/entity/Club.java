package entity;

import java.util.ArrayList;

public class Club{
    private final String name;
    private final String password;
    private String bio;
    private final ArrayList<Account> members = new ArrayList<>();
    private final ArrayList<String> foodPreferences = new ArrayList<>();
    private final ArrayList<Post> posts = new ArrayList<>();

    public Club(String name, String password) {
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

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setDisplayName(String newDisplayName) {

    }

    public void setBio(String newBio) {

    }

    public void setPreferences(String newPreferences) {

    }

    public String getBio() {
        return bio;
    }

    public ArrayList<Account> getMembers() {
        return members;
    }

    public ArrayList<String> getFoodPreferences() {
        return foodPreferences;
    }

    public ArrayList<Post> getUserPosts() {return posts;}
}
