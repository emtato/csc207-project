package interface_adapter.analyze_recipe;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import use_case.analyze_recipe.AnalyzeRecipeOutputBoundary;
import use_case.analyze_recipe.AnalyzeRecipeOutputData;

public class AnalyzeRecipePresenter implements AnalyzeRecipeOutputBoundary {

    private final AnalyzeRecipeViewModel viewModel;

    public AnalyzeRecipePresenter(AnalyzeRecipeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AnalyzeRecipeOutputData outputData) {
        viewModel.setResult(outputData.getAnalysis());
    }
}
