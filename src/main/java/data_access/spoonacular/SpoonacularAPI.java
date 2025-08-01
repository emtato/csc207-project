package data_access.spoonacular;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

import entity.Recipe;
import org.json.JSONObject;
import org.json.JSONArray;


/**
 * A data access class for interacting with the Spoonacular API to analyze recipes.
 * Takes a Recipe object and returns a map of dietary and nutritional properties
 */
public class SpoonacularAPI {
    /**
     * Sends a recipe to the Spoonacular API for analysis.
     * Constructs a POST request using the recipe's details and parses the returned JSON
     * to extract various dietary and health-related attributes.
     *
     * @param recipe The recipe to analyze
     * @return A map of analyzed properties including vegetarian, vegan, gluten-free, etc.
     * @throws IOException If there is a problem with the HTTP request
     * @throws InterruptedException If the request is interrupted
     */
    public HashMap<String, String> callAPI(Recipe recipe) throws IOException, InterruptedException {
        String title = recipe.getTitle();
        String description = recipe.getDescription();
        ArrayList<String> ingredients = recipe.getIngredients();
        String steps = recipe.getSteps();

        JSONArray ingredientList = new JSONArray();
        for (String ingredient : ingredients) {
            ingredientList.put(ingredient);
        }

        JSONObject body = new JSONObject();
        body.put("title", title);
        body.put("description", description);
        body.put("ingredients", ingredientList);
        body.put("steps", steps);

        String json = body.toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spoonacular.com/recipes/analyze?apiKey=3969e578906e44e5abd5998606e750d1&language=en&includeNutrition=false&includeTaste=false"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("BWAHHHHGHHHH");

        String responseJson = response.body().toString();
        JSONObject responseJsonObject = new JSONObject(responseJson);
        HashMap map = new HashMap<String, String>();
        map.put("vegeterian", responseJsonObject.get("vegetarian"));
        map.put("vegan", responseJsonObject.get("vegan"));
        map.put("gluten free", responseJsonObject.get("glutenFree"));
        map.put("dairy free", responseJsonObject.get("dairyFree"));
        map.put("very healthy", responseJsonObject.get("veryHealthy"));
        map.put("cheap", responseJsonObject.get("cheap"));
        map.put("sustainable", responseJsonObject.get("sustainable"));
        map.put("low FODMAP", responseJsonObject.get("lowFodmap"));
        map.put("weight watcher score", responseJsonObject.get("weightWatcherSmartPoints"));
        map.put("health score", responseJsonObject.get("healthScore"));
        map.put("spoonacular score", responseJsonObject.get("spoonacularScore"));
        return map;
    }
}
