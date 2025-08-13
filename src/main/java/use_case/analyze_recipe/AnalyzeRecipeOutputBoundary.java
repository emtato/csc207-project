package use_case.analyze_recipe;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public interface AnalyzeRecipeOutputBoundary {
    void present(AnalyzeRecipeOutputData outputData);
    void prepareFailView(AnalyzeRecipeOutputData outputData);
}
