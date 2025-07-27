package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Post {
    // TODO: add types of post (e.g. image/video, recipe, review)
    // TODO: public or community post

    private long postID;
    private String title;
    private String description;
    private Account user;
    private Recipe recipeObj;
    private ArrayList<String> tags;

    private boolean isImageVideo;
    private boolean isRecipe;
    private boolean isReview;
    private boolean isPublic;
    private boolean isClub;

    private LocalDateTime dateTime;
    private HashMap<Integer, Comment> comments;
    private long likes = 0;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");

    public Post(Account user, long ID, String title, String description) {
        this.user = user;
        this.postID = ID;
        this.title = title;
        this.description = description;
        this.tags = new ArrayList<>();
        dateTime = LocalDateTime.now();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public long getID() {
        return postID;
    }

    public void setID(long ID) {
        this.postID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTimeToString() {
        return dateTimeFormatter.format(dateTime);
    }

    public void setDateTimeFromString(String dateTime) {
        this.dateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    public String getDateToString() {
        return dateFormatter.format(dateTime);
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public boolean isImageVideo() {
        return isImageVideo;
    }

    public void setImageVideo(boolean imageVideo) {
        isImageVideo = imageVideo;
    }

    public boolean isRecipe() {
        return isRecipe;
    }

    public void setRecipe(boolean recipe) {
        isRecipe = recipe;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isClub() {
        return isClub;
    }

    public void setClub(boolean club) {
        isClub = club;
    }

    public Recipe getRecipeObj() {
        return recipeObj;
    }

    public void setRecipeObj(Recipe recipeObj) {
        this.recipeObj = recipeObj;
    }


    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}

