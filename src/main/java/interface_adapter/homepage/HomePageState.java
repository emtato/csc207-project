package interface_adapter.homepage;

import entity.Post;

import java.util.ArrayList;

public class HomePageState {
    ArrayList<Post> posts;

    public ArrayList<Post> getRandomPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }
}
