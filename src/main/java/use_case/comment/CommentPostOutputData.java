package use_case.comment;

import entity.Account;

/**
 * Created by Emilia on 2025-08-11!
 * Description:
 * ^ • ω • ^
 */
public class CommentPostOutputData {
    private final Account user;
    private final String time;

    public CommentPostOutputData(Account user, String time) {
        this.user = user;
        this.time = time;
    }

    public Account getUser() {
        return user;
    }

    public String getTime() {
        return time;
    }
}
