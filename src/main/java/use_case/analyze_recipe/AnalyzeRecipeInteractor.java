package use_case.analyze_recipe;

import data_access.spoonacular.SpoonacularAPI;
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

    /**
     * Executes recipe analysis.
     *
     * @param inputData recipe text and related info
     * @param testMode  if true, use mock/test API path; otherwise call the live API
     * @throws IOException          if the request fails due to network or I/O errors
     * @throws InterruptedException if the request is interrupted
     */
    @Override
    public void execute(AnalyzeRecipeInputData inputData, boolean testMode) {

        try {
            String result;
            if (testMode)
                result = spoonacularAPI.analyzeRecipeTest(inputData.getRecipe());
            else
                result = spoonacularAPI.analyzeRecipe(inputData.getRecipe());
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

            AnalyzeRecipeOutputData outputData = new AnalyzeRecipeOutputData(map, null);
            presenter.present(outputData);
        }
        catch (Exception e) {
            //e.printStackTrace();
            AnalyzeRecipeOutputData outputData = new AnalyzeRecipeOutputData(null, e.getMessage());
            presenter.prepareFailView(outputData);

        }
    }
}
