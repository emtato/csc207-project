package entity;

import java.util.ArrayList;

public class Recipe extends Post {

    private ArrayList<String> ingredients;
    private String steps;
    private ArrayList<String> cuisines;

    /**
     * @param user        The user who created the recipe post.
     * @param postID      The unique ID for this post.
     * @param title       The title of the recipe.
     * @param description A short description of the recipe.
     * @param ingredients A list of ingredients required for the recipe.
     * @param steps       The instructions to follow for making the recipe.
     * @param cuisines    A list of cuisines that the recipe belongs to.
     *                    <p>
     *                    constructor for building recipe from scratch without a Post object
     */
    public Recipe(Account user, long postID, String title, String description, ArrayList<String> ingredients,
                  String steps, ArrayList<String> cuisines) {
        super(user, postID, title, description);
        this.ingredients = ingredients;
        this.steps = steps;
        this.cuisines = cuisines;
    }

    /**
     * Constructs a Recipe object from an existing Post object.
     *
     * @param post        The existing Post object to base this Recipe on.
     * @param ingredients A list of ingredients required for the recipe.
     * @param steps       The instructions to follow for making the recipe.
     * @param cuisines    A list of cuisines that the recipe belongs to.
     */
    public Recipe(Post post, ArrayList<String> ingredients,
                  String steps, ArrayList<String> cuisines) {
        super(post.getUser(), post.getID(), post.getTitle(), post.getDescription());
        this.ingredients = ingredients;
        this.steps = steps;
        this.cuisines = cuisines;
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
