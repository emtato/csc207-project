package interface_adapter.analyze_recipes;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the meow
 */
public class AnalyzeRecipesViewModel extends ViewModel<AnalyzeState> {




    public AnalyzeRecipesViewModel() {
        super("analyze");
        setState(new AnalyzeState());
    }
}
