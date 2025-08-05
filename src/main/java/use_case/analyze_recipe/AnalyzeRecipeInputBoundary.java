package use_case.analyze_recipe;

import java.io.IOException;

/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public interface AnalyzeRecipeInputBoundary {
    void execute(AnalyzeRecipeInputData inputData) throws IOException, InterruptedException;
}
