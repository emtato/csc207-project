package use_case.fetch_post;

import entity.Post;

import java.util.ArrayList;

public class FetchPostOutputData {
    private final ArrayList<Post> posts;

    public FetchPostOutputData(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
