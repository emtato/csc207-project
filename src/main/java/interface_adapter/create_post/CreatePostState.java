package interface_adapter.create_post;

import entity.Club;

import java.util.ArrayList;

public class CreatePostState {
    private String title = "";
    private String body = "";
    private String type = "";
    private String steps = "";
    private ArrayList<String> ingredients = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<Club> clubs = new ArrayList<>();
    private String error = "";
    private Long postId = null;

    public CreatePostState(CreatePostState copy) {
        title = copy.title;
        body = copy.body;
        type = copy.type;
        steps = copy.steps;
        ingredients = new ArrayList<>(copy.ingredients);
        tags = new ArrayList<>(copy.tags);
        images = new ArrayList<>(copy.images);
        clubs = new ArrayList<>(copy.clubs);
        error = copy.error;
        postId = copy.postId;
    }

    public CreatePostState() {}

    public void setTitle(String title) { this.title = title; }
    public void setBody(String body) { this.body = body; }
    public void setType(String type) { this.type = type; }
    public void setSteps(String steps) { this.steps = steps; }
    public void setIngredients(ArrayList<String> ingredients) { this.ingredients = ingredients; }
    public void setTags(ArrayList<String> tags) { this.tags = tags; }
    public void setClubs(ArrayList<Club> clubs) { this.clubs = clubs; }
    public void setImages(ArrayList<String> images) { this.images = images; }
    public void setError(String error) { this.error = error; }
    public void setPostId(Long postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getType() { return type; }
    public String getSteps() { return steps; }
    public ArrayList<String> getIngredients() { return ingredients; }
    public ArrayList<String> getTags() { return tags; }
    public ArrayList<Club> getClubs() { return clubs; }
    public ArrayList<String> getImages() { return images; }
    public String getError() { return error; }
    public Long getPostId() { return postId; }
}
