
package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a recipe post, which extends a general Post with recipe-specific fields.
 * Includes ingredients, preparation steps, and associated cuisines.
 */
public class Recipe extends Post {

    private ArrayList<String> ingredients;
    private String steps;
    private ArrayList<String> cuisines;
    private HashMap<String, String> restrictionsMap;

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
        HashMap map = new HashMap<String, String>();
        map.put("vegeterian", false);
        map.put("vegan", false);
        map.put("gluten free", false);
        map.put("dairy free", false);
        map.put("very healthy", false);
        map.put("cheap", false);
        map.put("sustainable", false);
        map.put("low FODMAP", false);
        map.put("weight watcher score", 0);
        map.put("health score", 0);
        map.put("spoonacular score", 0);
        this.restrictionsMap = map;
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
        this.setTags(post.getTags());
        this.setImageURLs(post.getImageURLs());
        this.setLikes(post.getLikes());
        this.setDateTime(post.getDateTime());
        this.ingredients = ingredients;
        this.steps = steps;
        this.cuisines = cuisines;

    }

    /**
     * Returns the list of ingredients for this recipe.
     *
     * @return A list of ingredients
     */
    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the list of ingredients for this recipe.
     *
     * @param ingredients A list of ingredients
     */
    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the preparation steps for this recipe.
     *
     * @return The recipe instructions
     */
    public String getSteps() {
        return steps;
    }

    /**
     * Sets the preparation steps for this recipe.
     *
     * @param steps The recipe instructions
     */
    public void setSteps(String steps) {
        this.steps = steps;
    }

    /**
     * Returns the list of cuisines associated with this recipe.
     *
     * @return A list of cuisines
     */
    public ArrayList<String> getCuisines() {
        return cuisines;
    }

    /**
     * Sets the cuisines associated with this recipe.
     *
     * @param cuisines A list of cuisines
     */
    public void setCuisines(ArrayList<String> cuisines) {
        this.cuisines = cuisines;
    }


    /**
     * Basic debugging toString method
     *
     * @return postID, title, user, ingredients
     */
    @Override
    public String toString() {
        return "PostID=" + this.getID() + ", Title=" + getTitle() + ", User=" + getUser() + ", Ingredients=" + Arrays.toString(ingredients.toArray());
    }

    public HashMap<String, String> getRestrictionsMap() {
        return restrictionsMap;
    }

    public void setRestrictionsMap(HashMap<String, String> restrictionsMap) {
        this.restrictionsMap = restrictionsMap;
    }
}
