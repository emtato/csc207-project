package entity;

import java.util.ArrayList;

public class Recipe {
    private Account user;
    private String title;
    private String description;
    private ArrayList<String> tags;
    private ArrayList<String> ingredients;
    private String steps;
    private ArrayList<String> cuisines;

    public Recipe(Account user, String title, String description, ArrayList<String> tags, ArrayList<String> ingredients, String steps, ArrayList<String> cuisines) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.ingredients = ingredients;
        this.steps = steps;
        this.cuisines = cuisines;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public ArrayList<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(ArrayList<String> cuisines) {
        this.cuisines = cuisines;
    }
}
