package entity;

import java.time.LocalDateTime;

/**
 * comment made by a user.
 * contains info about the authoring account, the comment text, timestamp, number of likes, and a unique ID
 */
public class Comment {
    private Account account;
    private String comment;
    private LocalDateTime date;
    private int likes;
    private int ID;

    /**
     * Constructs a comment obj with the given account, comment text, date, and number of likes.
     *
     * @param account the account that made the comment
     * @param comment the content of the comment
     * @param date    the date and time the comment was made
     * @param likes   the number of likes the comment has
     */
    public Comment(Account account, String comment, LocalDateTime date, int likes) {
        this.account = account;
        this.comment = comment;
        this.date = date;
        this.likes = likes;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
