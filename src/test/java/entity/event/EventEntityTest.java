package entity.event;/**
 * Created by Emilia on 2025-08-14!
 * Description:
 * ^ • ω • ^
 */

import entity.Account;
import entity.Event;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class EventEntityTest {

    @Test
    void test() {
        Account owner = new Account("u","p");
        ArrayList<Account> participants = new ArrayList<>(Arrays.asList(new Account("a","p")));
        ArrayList<String> foods = new ArrayList<>(Arrays.asList("veg"));
        LocalDate d = LocalDate.of(2025, 8, 20);
        Event e = new Event(owner, 1L, "t", "dsc", "Toronto", d, participants, foods);
        assertEquals("Toronto", e.getLocation());
        assertEquals(d, e.getDate());
        assertEquals(1, e.getParticipants().size());
        assertEquals(1, e.getFoodPreferences().size());
        assertEquals("t", e.getTitle());
        assertEquals("dsc", e.getDescription());
        assertEquals(1L, e.getID());
        assertEquals("u", e.getUser().getUsername());
    }

    @Test
    void add_remove_participant() {
        Event e = new Event(new Account("u","p"), 2L, "t", "d", "T", LocalDate.now(), new ArrayList<>(), new ArrayList<>());
        Account a = new Account("a","p");
        e.addParticipant(a);
        assertEquals(1, e.getParticipants().size());
        e.removeParticipant(a);
        assertEquals(0, e.getParticipants().size());
    }

    @Test
    void add_remove_foodPref() {
        Event e = new Event(new Account("u","p"), 3L, "t", "d", "T", LocalDate.now(), new ArrayList<>(), new ArrayList<>());
        e.addFoodPreference("halal");
        assertEquals(1, e.getFoodPreferences().size());
        e.removeFoodPreference("halal");
        assertEquals(0, e.getFoodPreferences().size());
    }

    @Test
    void edit_date_and_location() {
        Event e = new Event(new Account("u","p"), 4L, "t", "d", "Old", LocalDate.of(2025,1,1), new ArrayList<>(), new ArrayList<>());
        e.editDate(LocalDate.of(2026,2,2));
        e.editLocation("New");
        assertEquals(LocalDate.of(2026,2,2), e.getDate());
        assertEquals("New", e.getLocation());
    }

    @Test
    void participants_and_food_lists_are_same_reference() {
        ArrayList<Account> plist = new ArrayList<>();
        ArrayList<String> flist = new ArrayList<>();
        Event e = new Event(new Account("u","p"), 5L, "t", "d", "T", LocalDate.now(), plist, flist);
        plist.add(new Account("x","p"));
        flist.add("kosher");
        assertEquals(1, e.getParticipants().size());
        assertEquals(1, e.getFoodPreferences().size());
    }
}
