package entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Event {
    private String name;
    private String description;
    private String location;
    private LocalDate date;
    private ArrayList<Account> participants;
    private ArrayList<String> foodPreferences;

    public Event(String name, String description, String location, LocalDate date, ArrayList<Account> participants, ArrayList<String> foodPreferences) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.participants = participants;
        this.foodPreferences = foodPreferences;
    }
    public String getName() {return name;}

    public String getDescription() {return description;}

    public String getLocation() {return location;}

    public LocalDate getDate() {return date;}

    public ArrayList<Account> getParticipants() {return participants;}

    public ArrayList<String> getFoodPreferences() {return foodPreferences;}

    public void addParticipant(Account account) {participants.add(account);}

    public void removeParticipant(Account account) {participants.remove(account);}

    public void addFoodPreference(String preference) {foodPreferences.add(preference);}

    public void removeFoodPreference(String preference) {foodPreferences.remove(preference);}

    public void editDate(LocalDate newdate) {this.date = newdate;}

    public void editDescription(String newdescription) {this.description = newdescription;}

    public void editName(String newname) {this.name = newname;}

    public void editLocation(String newlocation) {this.location = newlocation;}
}
