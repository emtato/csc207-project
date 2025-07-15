package data_access.spoonacular;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.json.JSONArray;



public class SpoonacularAPITest {
    public static void main(String[] args) throws Exception {
        JSONObject body = new JSONObject();
        body.put("title", "Simple Salad");
        body.put("instructions", "1. Toss...");

        JSONArray ingredients = new JSONArray();
        ingredients.put("1 cup chopped kale");
        ingredients.put("1/2 cup cooked quinoa");
// etc.

        body.put("ingredients", ingredients);

        String json = body.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spoonacular.com/recipes/analyze?apiKey=3969e578906e44e5abd5998606e750d1&language=en&includeNutrition=false&includeTaste=false"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Body:\n" + response.body());
    }
}
