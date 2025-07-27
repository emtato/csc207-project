package data_access.spoonacular;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import entity.Recipe;
import org.json.JSONObject;
import org.json.JSONArray;

public class SpoonacularAPI {
    public String callAPI(Recipe recipe) throws IOException, InterruptedException {
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
        System.out.println("Body:\n" + response.body());

        return response.body();


    }
}
