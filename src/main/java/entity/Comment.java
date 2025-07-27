package entity;

import java.time.LocalDateTime;

public class Comment {
    private Account account;
    private String comment;
    private LocalDateTime date;
    private int likes;
    private int ID;

    public Comment(Account account, String comment, LocalDateTime date, int likes) {
        this.account = account;
        this.comment = comment;
        this.date = date;
        this.likes = likes;
    }

    // TODO: add methods
}
