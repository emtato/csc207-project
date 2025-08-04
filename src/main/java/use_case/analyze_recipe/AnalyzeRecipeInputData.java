package use_case.analyze_recipe;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import entity.Recipe;

public class AnalyzeRecipeInputData {
    private final Recipe recipe;

    public AnalyzeRecipeInputData(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
