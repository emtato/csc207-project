package use_case.analyze_recipe;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public class AnalyzeRecipeInteractor implements AnalyzeRecipeInputBoundary {

    private final SpoonacularAccessInterface spoonacularAPI;
    private final AnalyzeRecipeOutputBoundary presenter;

    public AnalyzeRecipeInteractor(SpoonacularAccessInterface spoonacularAPI,
                                   AnalyzeRecipeOutputBoundary presenter) {
        this.spoonacularAPI = spoonacularAPI;
        this.presenter = presenter;
    }

    @Override
    public void execute(AnalyzeRecipeInputData inputData) throws IOException, InterruptedException {
        String result = spoonacularAPI.analyzeRecipe(inputData.getRecipe());

        JSONObject responseJsonObject = new JSONObject(result);
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

        AnalyzeRecipeOutputData outputData = new AnalyzeRecipeOutputData(map);
        presenter.present(outputData);
    }
}
