package interface_adapter.homepage;

import entity.Post;
import entity.Review;

import java.util.ArrayList;

public class HomePageState {
    ArrayList<Post> posts;
    ArrayList<Review> reviews;

    public ArrayList<Post> getRandomPosts() {
        return posts;
    }
    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }
    public ArrayList<Review> getRandomReviews() { return reviews; }
    public void setReviews(ArrayList<Review> reviews) { this.reviews = reviews; }
}
