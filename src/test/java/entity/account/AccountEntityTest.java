package entity.account;/**
 * Created by Emilia on 2025-08-14!
 * Description:
 * ^ • ω • ^
 */

import entity.Account;
import entity.Post;
import entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AccountEntityTest {

    @Test
    void test() {
        Account a = new Account("u","p");
        assertEquals("u", a.getUsername());
        assertEquals("p", a.getPassword());
        assertTrue(a.isPublic());
        assertTrue(a.isNotificationsEnabled());
        assertEquals("https://i.imgur.com/eA9NeJ1.jpeg", a.getProfilePictureUrl());
        assertNotNull(a.getFollowerAccounts());
        assertNotNull(a.getFollowingAccounts());
        assertNotNull(a.getRequestedAccounts());
        assertNotNull(a.getRequesterAccounts());
        assertNotNull(a.getBlockedAccounts());
        assertNotNull(a.getBlockedTerms());
        assertNotNull(a.getMutedAccounts());
        assertNotNull(a.getFoodPreferences());
        assertNotNull(a.getUserPosts());
        assertNotNull(a.getClubs());
    }

    @Test
    void usernamePasswordDisplayBioEmailProfile() {
        Account a = new Account("u","p");
        a.setUsername("x");
        a.setPassword("pw");
        a.setDisplayName("Name");
        a.setBio("bio");
        a.setEmail("e@e");
        a.setLikesUsernames(new ArrayList<>(Arrays.asList(22l)));
        a.setProfilePictureUrl("pic");
        assertEquals("x", a.getUsername());
        assertEquals("pw", a.getPassword());
        assertEquals("Name", a.getDisplayName());
        assertEquals("bio", a.getBio());
        assertEquals("e@e", a.getEmail());
        assertEquals(new ArrayList<>(Arrays.asList(22l)), a.getLikesUsernames());
        assertEquals("pic", a.getProfilePictureUrl());
    }

    @Test
    void publicAndNotificationsFlags() {
        Account a = new Account("u","p");
        a.setPublic(false);
        a.setNotificationsEnabled(false);
        assertFalse(a.isPublic());
        assertFalse(a.isNotificationsEnabled());
        a.setPublic(true);
        a.setNotificationsEnabled(true);
        assertTrue(a.isPublic());
        assertTrue(a.isNotificationsEnabled());
    }

    @Test
    void likesFlow() {
        Account a = new Account("u","p");
        Post p = new Post(a, 100L, "t","d");
        assertFalse(a.liked(p));
        a.like(p);
        assertTrue(a.liked(p));
        a.dislike(p);
        assertFalse(a.liked(p));
    }

    @Test
    void followerFollowingMaps_andCounts() {
        Account a = new Account("u","p");
        Account f1 = new Account("f1","p");
        Account f2 = new Account("f2","p");
        Map<String, User> followers = new HashMap<>();
        followers.put(f1.getUsername(), f1);
        followers.put(f2.getUsername(), f2);
        a.setFollowerAccounts(followers);
        assertEquals(2, a.getNumFollowers());

        Account g1 = new Account("g1","p");
        Map<String, User> following = new HashMap<>();
        following.put(g1.getUsername(), g1);
        a.setFollowingAccounts(following);
        assertEquals(1, a.getNumFollowing());
        assertEquals(following, a.getFollowingAccounts());
        assertEquals(followers, a.getFollowerAccounts());
    }

    @Test
    void setFriends_isFriend_add_remove() {
        Account a = new Account("u","p");
        Account b = new Account("b","p");
        Account c = new Account("c","p");
        ArrayList<User> friends = new ArrayList<>(Arrays.asList(b, c));
        a.setFriends(friends);
        assertTrue(a.isFriend(b));
        assertTrue(a.isFriend(c));
        a.removeFriend(b);
        assertFalse(a.isFriend(b));
        a.addFriend(b);
        assertTrue(a.isFriend(b));
    }

    @Test
    void blockedMutedFood_andPosts() {
        Account a = new Account("u","p");
        a.setBlockedAccounts(new HashMap<>());
        a.setBlockedTerms(new ArrayList<>(Arrays.asList("x","y")));
        a.setMutedAccounts(new ArrayList<>(Arrays.asList(new Account("m","p"))));
        a.setFoodPreferences(new ArrayList<>(Arrays.asList("sushi","noodles")));
        a.setUserPosts(new ArrayList<>(Arrays.asList(1L,2L)));
        assertEquals(2, a.getBlockedTerms().size());
        assertEquals(1, a.getMutedAccounts().size());
        assertEquals(Arrays.asList("sushi","noodles"), a.getFoodPreferences());
        assertEquals(Arrays.asList(1L,2L), a.getUserPosts());
    }

    @Test
    void requesterRequested() {
        Account a = new Account("u","p");
        Account r1 = new Account("r1","p");
        Map<String, User> req = new HashMap<>();
        req.put(r1.getUsername(), r1);
        a.setRequesterAccounts(req);
        a.setRequestedAccounts(req);
        assertEquals(req, a.getRequesterAccounts());
        assertEquals(req, a.getRequestedAccounts());
    }

    @Test
    void clubsAndLocation() {
        Account a = new Account("u","p");
        a.setClubs(new ArrayList<>(Arrays.asList("Chess")));
        a.setLocation("Toronto");
        assertEquals(new ArrayList<>(Arrays.asList("Chess")), a.getClubs());
        assertEquals("Toronto", a.getLocation());
    }

    @Test
    void toStringHasFields() {
        Account a = new Account("u","p");
        a.setDisplayName("Name");
        String s = a.toString();
        assertTrue(s.contains("username=u"));
        assertTrue(s.contains("password=p"));
        assertTrue(s.contains("name=Name"));
    }
}
