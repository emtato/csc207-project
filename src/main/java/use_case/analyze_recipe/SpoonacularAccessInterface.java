package use_case.analyze_recipe;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import entity.Recipe;

import java.io.IOException;

public interface SpoonacularAccessInterface {
    String analyzeRecipe(Recipe recipe) throws IOException, InterruptedException;
}
