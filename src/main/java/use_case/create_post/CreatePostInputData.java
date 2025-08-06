package use_case.create_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import entity.Account;
import entity.Club;

import java.util.ArrayList;

public class CreatePostInputData {
    private final Account user;
    private final String title;
    private final String type;
    private final String body;
    private final ArrayList<String> ingredients;
    private final String steps;
    private final ArrayList<String> tags;
    private final ArrayList<String> images;
    private final ArrayList<Club> clubs;
    private Long clubId; // ID of the club this post belongs to, if any

    public CreatePostInputData(Account user, String title, String type,
                               String body, ArrayList<String> ingredients, String steps,
                               ArrayList<String> tags, ArrayList<String> images, ArrayList<Club> clubs) {
        this.user = user;
        this.title = title;
        this.type = type;
        this.body = body;
        this.ingredients = ingredients;
        this.steps = steps;
        this.tags = tags;
        this.images = images;
        this.clubs = clubs;
    }

    public Account getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public ArrayList<Club> getClubs() {
        return clubs;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public Long getClubId() {
        return clubId;
    }
}
