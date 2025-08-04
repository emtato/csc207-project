package use_case.analyze_recipe;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import java.util.Map;

public class AnalyzeRecipeOutputData {
    private final Map<String, String> analysis;

    public AnalyzeRecipeOutputData(Map<String, String> analysis) {
        this.analysis = analysis;
    }

    public Map<String, String> getAnalysis() {
        return analysis;
    }
}
