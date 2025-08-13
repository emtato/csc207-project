package entity;

import data_access.FilePostCommentLikesDataAccessObject;

import java.util.Locale;
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
    private String type;

    private boolean isImageVideo;
    private boolean isReview;
    private boolean isPublic;
    private boolean isClub;

    private LocalDateTime dateTime;
    private String time;
    //can probably delete since comments are stored in JSON
    //private ArrayList<Comment> comments;
    private long likes = 0;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");

    /**
     * Constructor for creating a new post
     *
     * @param user        The user who created the post
     * @param ID          The unique identifier of the post
     * @param title       The title of the post
     * @param description The description/content of the post
     * @param imageURLs   List of image URLs associated with the post
     * @param contents    Map containing additional post content (e.g., ingredients, steps)
     * @param type        The type of post
     * @param timestamp   The timestamp when the post was created
     * @param tags        List of tags associated with the post
     */
    public Post(Account user, long ID, String title, String description,
                ArrayList<String> imageURLs, HashMap<String, ArrayList<String>> contents,
                String type, String timestamp, ArrayList<String> tags) {
        this.user = user;
        this.postID = ID;
        this.title = title;
        this.description = description;
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.imageURLs = imageURLs != null ? new ArrayList<>(imageURLs) : new ArrayList<>();
        this.type = type;
        if (timestamp != null) {
            String normalized = timestamp.trim().replaceAll("\\s+", " ");
            // Try multiple patterns (12-hour & 24-hour) in case of format inconsistency
            String[] patterns = {"yyyy-MM-dd hh:mm a", "yyyy-MM-dd HH:mm a"};
            LocalDateTime parsed = null;
            for (String p : patterns) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(p, Locale.ENGLISH);
                    parsed = LocalDateTime.parse(normalized, formatter);
                    break;
                } catch (Exception ignored) {}
            }
            if (parsed == null) {
                try {
                    // Last resort: drop AM/PM and parse 24h
                    String noMeridiem = normalized.replace(" AM", "").replace(" PM", "");
                    DateTimeFormatter fallback = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
                    parsed = LocalDateTime.parse(noMeridiem, fallback);
                } catch (Exception e) {
                    System.out.println("DEBUG: Fallback timestamp parse failed for '" + timestamp + "' -> using now()");
                    parsed = LocalDateTime.now();
                }
            }
            this.dateTime = parsed;
        }
        else {
            this.dateTime = LocalDateTime.now();
        }
        this.isImageVideo = false;
        this.isReview = false;
        this.isPublic = false;
        this.isClub = false;
    }

    public Post(Account user, long ID, String title, String description,
                ArrayList<String> imageURLs, HashMap<String, ArrayList<String>> contents,
                String type, LocalDateTime timestamp, ArrayList<String> tags) {
        this.user = user;
        this.postID = ID;
        this.title = title;
        this.description = description;
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.imageURLs = imageURLs != null ? new ArrayList<>(imageURLs) : new ArrayList<>();
        this.type = type;
        this.dateTime = timestamp;
        this.isImageVideo = false;
        this.isReview = false;
        this.isPublic = false;
        this.isClub = false;
    }

    /**
     * Basic constructor for legacy compatibility
     */
    public Post(Account user, long ID, String title, String description,
                ArrayList<String> imageURLs, String type) {
        this(user, ID, title, description, imageURLs, new HashMap<>(), type, LocalDateTime.now(), new ArrayList<>());
    }

    /**
     * Simple constructor for basic posts
     */
    public Post(Account user, long ID, String title, String description) {
        this(user, ID, title, description, new ArrayList<>(), "general");
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
     * Returns the post's timestamp formatted as "yyyy-MM-dd HH:mm AM/PM".
     *
     * @return Formatted timestamp string
     */
    public String getDateTimeToString() {
        return dateTimeFormatter.format(dateTime);
    }

    /**
     * Sets the post's timestamp using a formatted datetime string.
     *
     * @param dateTime A string representing datetime in "yyyy-MM-dd HH:mm AM/PM" format
     */
    public void setDateTimeFromString(String dateTime) {
        if (dateTime.charAt(dateTime.length() - 4) == 'a' || dateTime.charAt(dateTime.length() - 4) == 'p') {
            if (dateTime.charAt(dateTime.length() - 4) == 'a') {
                dateTime = dateTime.substring(0, dateTime.length() - 4) + "AM";
            }
            else {
                dateTime = dateTime.substring(0, dateTime.length() - 4) + "PM";
            }
        }
        this.dateTime = LocalDateTime.parse(dateTime.trim(), dateTimeFormatter);
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

    public ArrayList<String> getImageURLs() {
        return imageURLs;
    }

    /**
     * sets image url of post. automatically updates post field for isImageVideo.
     */
    public void setImageURLs(ArrayList<String> imageURLs) {
        this.imageURLs = imageURLs;
        if (imageURLs != null && !imageURLs.isEmpty()) {
            this.isImageVideo = true;
        }
        else this.isImageVideo = false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
