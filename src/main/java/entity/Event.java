package entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Event extends Post {
    private String location;
    private LocalDate date;
    private ArrayList<String> participants;
    private ArrayList<String> foodPreferences = new ArrayList<>();

    /**
     * @param user              The user who created the event post.
     * @param postID            The unique ID for this post.
     * @param title             The title of the event.
     * @param description       A short description of the event.
     * @param location          The location of the event.
     * @param date              The date of the event.
     * @param participants      A list of attendees' names
     * @param foodPreferences   A list of food preferences for the event.
     *                    <p>
     *                    constructor for building recipe from scratch without a Post object
     */

    public Event(Account user, long postID, String title, String description, String location, LocalDate date, ArrayList<String> participants, ArrayList<String> foodPreferences) {
        super(user, postID, title, description);
        this.location = location;
        this.date = date;
        this.participants = participants;
        this.foodPreferences = foodPreferences;


    }
    /*     constructor for building recipe from scratch without a Post object */

    public Event(Post post, String location, LocalDate date, ArrayList<String> participants, ArrayList<String> foodPreferences) {
        super(post.getUser(), post.getID(), post.getTitle(), post.getDescription());
        this.location = location;
        this.date = date;
        this.participants = participants;
        this.foodPreferences = foodPreferences;
    }

    public String getLocation() {return location;}

    public LocalDate getDate() {return date;}

    public ArrayList<String> getParticipants() {return participants;}

    public ArrayList<String> getFoodPreferences() {return foodPreferences;}

}
