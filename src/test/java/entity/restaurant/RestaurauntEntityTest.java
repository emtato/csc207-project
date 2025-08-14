package entity.restaurant;/**
 * Created by Emilia on 2025-08-14!
 * Description:
 * ^ • ω • ^
 */

import entity.Restaurant;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurauntEntityTest {

    @Test
    void test() {
        ArrayList<String> cuisines = new ArrayList<>(Arrays.asList("Sushi","Ramen"));
        Restaurant r = new Restaurant(cuisines, "Toronto");
        assertNull(r.getName());
        assertNull(r.getAddress());
        assertNull(r.getPhone());
        assertNull(r.getPriceLevel());
        assertNull(r.getURI());
        assertEquals("Toronto", r.getLocation());
        assertNotNull(r.getCuisines());
        assertNotNull(r.getReviews());
        assertEquals(2, r.getCuisines().size());
        assertEquals(0, r.getReviews().size());
    }

    @Test
    void settersWork() {
        Restaurant r = new Restaurant(new ArrayList<>(), "Toronto");
        r.setName("Izakaya Go!");
        r.setAddress("123 Queen St W");
        r.setPhone("416-000-0000");
        r.setPriceLevel("$$");
        r.setURI(URI.create("https://example.com/r"));
        assertEquals("Izakaya Go!", r.getName());
        assertEquals("123 Queen St W", r.getAddress());
        assertEquals("416-000-0000", r.getPhone());
        assertEquals("$$", r.getPriceLevel());
        assertEquals(URI.create("https://example.com/r"), r.getURI());
    }

    @Test
    void cuisinesIsSameReference() {
        ArrayList<String> cuisines = new ArrayList<>(Arrays.asList("Dim Sum"));
        Restaurant r = new Restaurant(cuisines, "Toronto");
        cuisines.add("BBQ");
        assertEquals(Arrays.asList("Dim Sum","BBQ"), r.getCuisines());
    }

    @Test
    void reviewsStartEmpty() {
        Restaurant r = new Restaurant(new ArrayList<>(), "Toronto");
        assertNotNull(r.getReviews());
        assertEquals(0, r.getReviews().size());
    }
}
