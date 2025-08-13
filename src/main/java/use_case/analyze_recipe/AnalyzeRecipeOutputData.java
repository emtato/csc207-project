package use_case.analyze_recipe;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import java.util.Map;

public class AnalyzeRecipeOutputData {
    private final Map<String, String> analysis;
    private final String failureMessage;

    public AnalyzeRecipeOutputData(Map<String, String> analysis, String failureMessage) {
        this.analysis = analysis;
        this.failureMessage = failureMessage;
    }

    public Map<String, String> getAnalysis() {
        return analysis;
    }

    public String getFailureMessage() {
        return failureMessage;
    }
}
