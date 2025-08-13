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
import use_case.analyze_recipe.SpoonacularAccessInterface;


/**
 * A data access class for interacting with the Spoonacular API to analyze recipes.
 * Takes a Recipe object and returns a map of dietary and nutritional properties
 */
public class SpoonacularAPI implements SpoonacularAccessInterface {
    /**
     * Sends a recipe to the Spoonacular API for analysis.
     * Constructs a POST request using the recipe's details and parses the returned JSON
     * to extract various dietary and health-related attributes.
     *
     * @param recipe The recipe to analyze
     * @return A map of analyzed properties including vegetarian, vegan, gluten-free, etc.
     */
    public String analyzeRecipe(Recipe recipe) throws IOException, InterruptedException {
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

        return responseJson;
    }

    public String analyzeRecipeTest(Recipe recipe) {
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


        String responseTest = "{\"id\":-1,\"image\":\"https://img.spoonacular.com/recipes/-1-556x370.\",\"imageType\":\"\",\"title\":\"Spaghetti with Tomato Sauce\",\"readyInMinutes\":null,\"servings\":1,\"sourceUrl\":null,\"vegetarian\":true,\"vegan\":true,\"glutenFree\":false,\"dairyFree\":true,\"veryHealthy\":false,\"cheap\":false,\"veryPopular\":false,\"sustainable\":false,\"lowFodmap\":false,\"weightWatcherSmartPoints\":10,\"gaps\":\"no\",\"preparationMinutes\":null,\"cookingMinutes\":null,\"aggregateLikes\":0,\"healthScore\":45,\"creditsText\":\"\",\"license\":null,\"sourceName\":\"\",\"pricePerServing\":96.0,\"extendedIngredients\":[{\"id\":11420420,\"aisle\":\"Pasta and Rice\",\"image\":\"spaghetti.jpg\",\"consistency\":\"SOLID\",\"name\":\"spaghetti\",\"nameClean\":\"spaghetti\",\"original\":\"spaghetti\",\"originalName\":\"spaghetti\",\"amount\":1.0,\"unit\":\"serving\",\"meta\":[],\"measures\":{\"us\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"},\"metric\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"}}},{\"id\":11529,\"aisle\":\"Produce\",\"image\":\"tomato.png\",\"consistency\":\"SOLID\",\"name\":\"tomato\",\"nameClean\":\"tomato\",\"original\":\"tomato\",\"originalName\":\"tomato\",\"amount\":1.0,\"unit\":\"serving\",\"meta\":[],\"measures\":{\"us\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"},\"metric\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"}}},{\"id\":4053,\"aisle\":\"Oil, Vinegar, Salad Dressing\",\"image\":\"olive-oil.jpg\",\"consistency\":\"LIQUID\",\"name\":\"olive oil\",\"nameClean\":\"olive oil\",\"original\":\"olive oil\",\"originalName\":\"olive oil\",\"amount\":1.0,\"unit\":\"serving\",\"meta\":[],\"measures\":{\"us\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"},\"metric\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"}}},{\"id\":11215,\"aisle\":\"Produce\",\"image\":\"garlic.png\",\"consistency\":\"SOLID\",\"name\":\"garlic\",\"nameClean\":\"garlic\",\"original\":\"garlic\",\"originalName\":\"garlic\",\"amount\":1.0,\"unit\":\"serving\",\"meta\":[],\"measures\":{\"us\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"},\"metric\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"}}},{\"id\":2044,\"aisle\":\"Produce;Spices and Seasonings\",\"image\":\"basil.jpg\",\"consistency\":\"SOLID\",\"name\":\"basil\",\"nameClean\":\"basil\",\"original\":\"basil\",\"originalName\":\"basil\",\"amount\":1.0,\"unit\":\"serving\",\"meta\":[],\"measures\":{\"us\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"},\"metric\":{\"amount\":1.0,\"unitShort\":\"serving\",\"unitLong\":\"serving\"}}}],\"summary\":null,\"cuisines\":[],\"dishTypes\":[\"side dish\",\"antipasti\",\"starter\",\"snack\",\"appetizer\",\"antipasto\",\"hor d'oeuvre\"],\"diets\":[\"dairy free\",\"lacto ovo vegetarian\",\"vegan\"],\"occasions\":[],\"instructions\":null,\"analyzedInstructions\":[],\"originalId\":null,\"spoonacularScore\":88}\n";


        return responseTest;
    }
}
