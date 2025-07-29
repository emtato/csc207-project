package entity;

import java.time.LocalDate;
import java.util.Set;

public class Event extends Post {
    private String location;
    private LocalDate date;
    private Set<Account> participants;
    private Set<String> foodPreferences;

    /**
     * @param user              The user who created the event post.
     * @param postID            The unique ID for this post.
     * @param title             The title of the event.
     * @param description       A short description of the event.
     * @param location          The location of the event.
     * @param date              The date of the event.
     * @param participants      A list of attendees' accounts
     * @param foodPreferences   A list of food preferences for the event.
     *                    <p>
     *                    constructor for building recipe from scratch without a Post object
     */

    public Event(Account user, long postID, String title, String description, String location, LocalDate date, Set<Account> participants, Set<String> foodPreferences) {
        super(user, postID, title, description);
        this.location = location;
        this.date = date;
        this.participants = participants;
        this.foodPreferences = foodPreferences;


    }

    public String getLocation() {return location;}

    public LocalDate getDate() {return date;}

    public Set<Account> getParticipants() {return participants;}

    public Set<String> getFoodPreferences() {return foodPreferences;}

    public void addParticipant(Account account) {participants.add(account);}

    public void removeParticipant(Account account) {participants.remove(account);}

    public void addFoodPreference(String preference) {foodPreferences.add(preference);}

    public void removeFoodPreference(String preference) {foodPreferences.remove(preference);}

    public void editDate(LocalDate newdate) {this.date = newdate;}

    public void editLocation(String newlocation) {this.location = newlocation;}
}
