package use_case.create_review;

import entity.Account;
import java.util.ArrayList;

public class CreateReviewInputData {
    private final Account user;
    private final String title;
    private final String body;
    private final double rating;
    private final ArrayList<String> tags;

    public CreateReviewInputData(Account user, String title, String body,
                                 double rating, ArrayList<String> tags) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.rating = rating;
        this.tags = tags;
    }

    public Account getUser() { return user; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public double getRating() { return rating; }
    public ArrayList<String> getTags() { return tags; }
}
