package interface_adapter.create_post_view;

import java.util.ArrayList;

public class CreatePostState {
    private String title = "";
    private String body = "";
    private String type = "";
    private String steps = "";
    private ArrayList<String> ingredients = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> clubs = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private String error = null;

    public CreatePostState() {}

    public void setTitle(String title) { this.title = title; }
    public void setBody(String body) { this.body = body; }
    public void setType(String type) { this.type = type; }
    public void setSteps(String steps) { this.steps = steps; }
    public void setIngredients(ArrayList<String> ingredients) { this.ingredients = ingredients; }
    public void setTags(ArrayList<String> tags) { this.tags = tags; }
    public void setClubs(ArrayList<String> clubs) { this.clubs = clubs; }
    public void setImages(ArrayList<String> images) { this.images = images; }
    public void setError(String error) { this.error = error; }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getType() { return type; }
    public String getSteps() { return steps; }
    public ArrayList<String> getIngredients() { return ingredients; }
    public ArrayList<String> getTags() { return tags; }
    public ArrayList<String> getClubs() { return clubs; }
    public ArrayList<String> getImages() { return images; }
    public String getError() { return error; }
}
