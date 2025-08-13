package interface_adapter.analyze_recipe;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import java.util.Map;

public class AnalyzeRecipeViewModel {
    private Map<String, String> result;
    private String error;

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }

    public void setFailureMessage(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }
}
