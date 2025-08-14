package interface_adapter.analyze_recipe;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import use_case.analyze_recipe.AnalyzeRecipeInputBoundary;
import use_case.analyze_recipe.AnalyzeRecipeInputData;
import entity.Recipe;

import java.io.IOException;

public class AnalyzeRecipeController {

    private final AnalyzeRecipeInputBoundary interactor;

    public AnalyzeRecipeController(AnalyzeRecipeInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void analyze(Recipe recipe) throws IOException, InterruptedException {
        AnalyzeRecipeInputData inputData = new AnalyzeRecipeInputData(recipe);
        interactor.execute(inputData, false);
    }
}
