package entity;

import data_access.DataStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a generic social media post made by a user.
 * A post can have tags, marked as public or club-only, and categorized (e.g., image/video, review).
 */
public class Post {
    // TODO: add types of post (e.g. image/video, recipe, review)
    // TODO: public or community post

    private long postID;
    private String title;
    private String description;
    private Account user;
    private ArrayList<String> tags;
    private ArrayList<String> imageURLs;

    private boolean isImageVideo;
    private boolean isReview;
    private boolean isPublic;
    private boolean isClub;

    private LocalDateTime dateTime;
    //can probably delete since comments are stored in JSON
    //private ArrayList<Comment> comments;
    private long likes = 0;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");

    /**
     * Constructs a new Post object with the given user, ID, title, and description.
     * Initializes default flags and timestamp.
     *
     * @param user        The account that created the post
     * @param ID          The unique identifier of the post
     * @param title       The title of the post
     * @param description The description/content of the post
     */
    public Post(Account user, long ID, String title, String description) {
        this.user = user;
        this.postID = ID;
        this.title = title;
        this.description = description;
        this.tags = new ArrayList<>();
        dateTime = LocalDateTime.now();
        this.isImageVideo = false;
        this.isReview = false;
        this.isPublic = false;
        this.isClub = false;
        this.imageURLs = new ArrayList<>();
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

    /**
     * Returns the post's timestamp formatted as "yyyy-MM-dd  HH:mm:ss".
     *
     * @return Formatted timestamp string
     */
    public String getDateTimeToString() {
        return dateTimeFormatter.format(dateTime);
    }

    /**
     * Sets the post's timestamp using a formatted datetime string.
     *
     * @param dateTime A string representing datetime in "yyyy-MM-dd  HH:mm:ss" format
     */
    public void setDateTimeFromString(String dateTime) {
        this.dateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    /**
     * Returns the post's date formatted as "EEE, dd MMM yyyy".
     *
     * @return Formatted date string
     */
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


    /**
     * Returns the list of tags associated with the post.
     *
     * @return A list of tags
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * Sets the list of tags for the post.
     *
     * @param tags A list of tags to associate with the post
     */
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    /**
     * Basic debugging toString method
     *
     * @return postID, title, user
     */
    @Override
    public String toString() {
        return "PostID=" + postID + ", Title=" + title + ", User=" + user.getUsername();
    }

    public ArrayList<Comment> getComments() {
        DataStorage dataStorage = new DataStorage();
        ArrayList<Comment> comments = dataStorage.getComments(postID);
        return comments;
    }

    public ArrayList<String> getImageURLs() {
        System.out.println("getImageURLs called " + this.imageURLs);
        return imageURLs;
    }

    /**
     * sets image url of post. automatically updates post field for isImageVideo.
     */
    public void setImageURLs(ArrayList<String> imageURLs) {
        System.out.println("before " + imageURLs + " after setImageURLs " + imageURLs);
        this.imageURLs = imageURLs;
        if (imageURLs != null && !imageURLs.isEmpty()) {
            this.isImageVideo = true;
        }
        else this.isImageVideo = false;
    }
}


