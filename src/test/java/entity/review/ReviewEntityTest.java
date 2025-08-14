package entity.review;

import entity.Review;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewEntityTest {

    @Test
    void testConstructorDefaults() {
        Review review = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        assertEquals(-1, review.getRating());
        assertFalse(review.isRestaurantReview());
        assertFalse(review.isRecipeReview());
        assertTrue(review.isReview());
        assertEquals("review", review.getType());
    }

    @Test
    void testSetRatingWithinRange() {
        Review review = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        review.setRating(0);
        assertEquals(0, review.getRating());
        review.setRating(5);
        assertEquals(5, review.getRating());
        review.setRating(3);
        assertEquals(3, review.getRating());
    }

    @Test
    void testSetRatingOutsideRange() {
        Review review = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        review.setRating(-2);
        assertEquals(-1, review.getRating());
        review.setRating(6);
        assertEquals(-1, review.getRating());
    }

    @Test
    void testSetRestaurantReviewToggling() {
        Review review = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        review.setRestaurantReview(true);
        assertTrue(review.isRestaurantReview());
        review.setRestaurantReview(false);
        assertFalse(review.isRestaurantReview());
    }

    @Test
    void testSetRecipeReviewToggling() {
        Review review = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        review.setRecipeReview(true);
        assertTrue(review.isRecipeReview());
        review.setRecipeReview(false);
        assertFalse(review.isRecipeReview());
    }

    @Test
    void testAverageRatingWithValidAndInvalidRatings() {
        Review r1 = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        Review r2 = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        Review r3 = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        r1.setRating(5);
        r2.setRating(3);
        r3.setRating(-1);
        float avg = r1.averageRating(new ArrayList<>(Arrays.asList(r1, r2, r3)));
        assertEquals(4.0, avg);
    }

    @Test
    void testToStringContainsExpectedParts() {
        Review review = new Review(new entity.Account("u", "p"), 1L, "t", "d");
        review.setRating(4);
        review.setRestaurantReview(true);
        String result = review.toString();
        assertTrue(result.contains("4"));
        assertTrue(result.toLowerCase().contains("restaurant"));
    }
}
