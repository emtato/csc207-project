package use_case.fetch_post;

import entity.Post;

public class FetchPostOutputData {
    private final Post post;

    public FetchPostOutputData(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
