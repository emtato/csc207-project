package entity;

import java.util.ArrayList;

public class Club implements User{
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

    public ArrayList<Account> getMembers() {
        return members;
    }

    public ArrayList<String> getFoodPreferences() {
        return foodPreferences;
    }

    public ArrayList<Post> getPosts() {return posts;}
}
