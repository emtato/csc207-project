package entity.post;

import entity.Account;
import entity.Post;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PostEntityTest {

    @Test
    void test() {
        Account user = new Account("alice", "pw");
        String ts = "2025-08-07 02:12 AM";

        Post p = new Post(user, 42L, "title", "desc", new ArrayList<>(), new HashMap<>(),
                "recipe", ts, new ArrayList<>());

        assertEquals("title", p.getTitle());
        assertEquals("desc", p.getDescription());
        assertEquals("recipe", p.getType());
        assertEquals(42L, p.getID());
        assertEquals("alice", p.getUser().getUsername());

        assertEquals("2025-08-07 02:12 AM", p.getDateTimeToString());

        assertEquals("Thu, 07 Aug 2025", p.getDateToString());
    }

    @Test
    void test_localDateTime() {
        Account user = new Account("bob", "pw");
        LocalDateTime t = LocalDateTime.of(2025, 1, 2, 23, 5); // 11:05 PM

        Post p = new Post(
                user, 7L, "x", "y",
                new ArrayList<>(), new HashMap<>(),
                "general", t, new ArrayList<>()
        );

        assertEquals(t, p.getDateTime());
        assertEquals("2025-01-02 11:05 PM", p.getDateTimeToString());
    }

    @Test
    void setDateTimeFromString() {
        Account user = new Account("c", "pw");
        Post p = new Post(user, 1L, "t", "d");

        p.setDateTimeFromString("2025-08-07 09:03 a.m.");
        assertEquals("2025-08-07 09:03 AM", p.getDateTimeToString());

        p.setDateTimeFromString("2025-08-07 09:03 p.m.");
        assertEquals("2025-08-07 09:03 PM", p.getDateTimeToString());
    }

    @Test
    void imageUrls_toggle_isImageVideo() {
        Account user = new Account("x", "pw");
        Post p = new Post(user, 1L, "t", "d");

        assertFalse(p.isImageVideo());

        ArrayList<String> imgs = new ArrayList<>();
        imgs.add("https://example.com/1.jpg");
        p.setImageURLs(imgs);
        assertTrue(p.isImageVideo());
        assertEquals(imgs, p.getImageURLs());

        p.setImageURLs(new ArrayList<>()); // empty -> false
        assertFalse(p.isImageVideo());

        p.setImageURLs(null); // null -> false
        assertFalse(p.isImageVideo());
    }

    @Test
    void tags_get_set_and_nullSafety() {
        Account user = new Account("x", "pw");
        Post p = new Post(user, 1L, "t", "d");

        assertNotNull(p.getTags());
        assertEquals(0, p.getTags().size());

        ArrayList<String> tags = new ArrayList<>();
        tags.add("a");
        tags.add("b");
        p.setTags(tags);
        assertEquals(tags, p.getTags());

        tags.add("c");
        assertEquals(3, p.getTags().size());
    }

    @Test
    void toString_containsIdTitleAndUsername() {
        Account user = new Account("eve", "pw");
        Post p = new Post(user, 9L, "hello", "world");
        String s = p.toString();
        assertTrue(s.contains("PostID=9"));
        assertTrue(s.contains("Title=hello"));
        assertTrue(s.contains("User=eve"));
    }
    @Test
    void setters_for_title_desc_id_user() {
        Account u1 = new Account("a", "pw");
        Account u2 = new Account("b", "pw");
        Post p = new Post(u1, 1L, "t", "d");

        p.setTitle("new");
        p.setDescription("newd");
        p.setID(99L);
        p.setUser(u2);

        assertEquals("new", p.getTitle());
        assertEquals("newd", p.getDescription());
        assertEquals(99L, p.getID());
        assertEquals("b", p.getUser().getUsername());
    }

    @Test
    void public_and_club_flags_toggle() {
        Post p = new Post(new Account("a", "pw"), 1L, "t", "d");

        assertFalse(p.isPublic());
        assertFalse(p.isClub());

        p.setPublic(true);
        p.setClub(true);
        assertTrue(p.isPublic());
        assertTrue(p.isClub());

        p.setPublic(false);
        p.setClub(false);
        assertFalse(p.isPublic());
        assertFalse(p.isClub());
    }

    @Test
    void review_flag_toggle() {
        Post p = new Post(new Account("a", "pw"), 1L, "t", "d");
        assertFalse(p.isReview());
        p.setReview(true);
        assertTrue(p.isReview());
        p.setReview(false);
        assertFalse(p.isReview());
    }

    @Test
    void test_with24h_hits_second_pattern() {
        Account user = new Account("alice", "pw");
        String ts = "2025-08-07 14:05 PM";
        Post p = new Post(user, 1L, "t", "d", new ArrayList<>(), new HashMap<>(),
                "general", ts, new ArrayList<>());
        assertEquals("2025-08-07 02:05 PM", p.getDateTimeToString());
    }

    @Test
    void test_24h() {
        Account user = new Account("alice", "pw");
        String ts = "2025-08-07 14:30";
        Post p = new Post(user, 2L, "t", "d", new ArrayList<>(), new HashMap<>(),
                "general", ts, new ArrayList<>());
        assertEquals("2025-08-07 02:30 PM", p.getDateTimeToString());
    }

    @Test
    void test_timestampFail() {
        Account user = new Account("alice", "pw");
        LocalDateTime before = LocalDateTime.now();
        Post p = new Post(user, 3L, "t", "d", new ArrayList<>(), new HashMap<>(),
                "general", "not-a-timestamp", new ArrayList<>());
        LocalDateTime after = LocalDateTime.now();
        LocalDateTime dt = p.getDateTime();
        assertFalse(dt.isBefore(before));
        assertFalse(dt.isAfter(after));
    }

    @Test
    void test_timestamp() {
        Account user = new Account("alice", "pw");
        LocalDateTime before = LocalDateTime.now();
        Post p = new Post(user, 4L, "t", "d", new ArrayList<>(), new HashMap<>(),
                "general", (String) null, new ArrayList<>());
        LocalDateTime after = LocalDateTime.now();
        LocalDateTime dt = p.getDateTime();
        assertFalse(dt.isBefore(before));
        assertFalse(dt.isAfter(after));
    }
}
