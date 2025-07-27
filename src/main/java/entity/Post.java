package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Post {
    // TODO: add types of post (e.g. image/video, recipe, review)
    // TODO: public or community post

    private long ID;
    private String Title;
    private String content;
    private Account user;
    private LocalDateTime dateTime;
    private HashMap<Integer, Comment> comments;
    private long likes = 0;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");

    public Post(Account user, long ID) {
        this.user = user;
        this.ID = ID;
        dateTime = LocalDateTime.now();
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}

