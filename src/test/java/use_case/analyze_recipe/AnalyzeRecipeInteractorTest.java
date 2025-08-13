package use_case.analyze_recipe;/**
 * Created by Emilia on 2025-08-11!
 * Description:
 * ^ • ω • ^
 */

import data_access.spoonacular.SpoonacularAPI;
import entity.Account;
import entity.Post;
import entity.Recipe;
import interface_adapter.analyze_recipe.AnalyzeRecipePresenter;
import interface_adapter.analyze_recipe.AnalyzeRecipeViewModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AnalyzeRecipeInteractorTest {

    @Test
    void successTest() {
        AnalyzeRecipeOutputBoundary successPresenter = new AnalyzeRecipeOutputBoundary() {

            @Override
            public void present(AnalyzeRecipeOutputData outputData) {
                assertEquals(false, outputData.getAnalysis().get("gluten free"));
                assertEquals(true, outputData.getAnalysis().get("dairy free"));
                assertEquals(false, outputData.getAnalysis().get("sustainable"));
                assertEquals(10, outputData.getAnalysis().get("weight watcher score"));
                assertEquals(false, outputData.getAnalysis().get("very healthy"));
                assertEquals(45, outputData.getAnalysis().get("health score"));
                assertEquals(false, outputData.getAnalysis().get("low FODMAP"));
                assertEquals(true, outputData.getAnalysis().get("vegan"));
                assertEquals(false, outputData.getAnalysis().get("cheap"));
                assertEquals(88, outputData.getAnalysis().get("spoonacular score"));
                assertEquals(true, outputData.getAnalysis().get("vegeterian"));

            }

            @Override
            public void prepareFailView(AnalyzeRecipeOutputData outputData) {
                fail("get analysis failed.");
            }
        };

        AnalyzeRecipeInteractor interactor = new AnalyzeRecipeInteractor(new SpoonacularAPI(), successPresenter);


        Recipe sampleRecipe = new Recipe(
                new Account("tester", "h"),
                9999999l,
                "Spaghetti with tomto Sauce",
                "Classic Italian pasta",
                new ArrayList<>(Arrays.asList("spaghetti", "tomato", "olive oil", "garlic", "basil")),
                "Boil pasta. Cook sauce. Combine.",
                new ArrayList<>(Arrays.asList("Italian"))
        );

        final AnalyzeRecipeInputData inputData = new AnalyzeRecipeInputData(sampleRecipe);
        interactor.executeTest(inputData);
    }

    @Test
    void failTest() {
        AnalyzeRecipeOutputBoundary successPresenter = new AnalyzeRecipeOutputBoundary() {

            @Override
            public void present(AnalyzeRecipeOutputData outputData) {
                fail("get analysis succeeded unexpectedly.");

            }

            @Override
            public void prepareFailView(AnalyzeRecipeOutputData outputData) {
                String output = outputData.getFailureMessage();
                String bwap = "Cannot invoke \"java.util.ArrayList.iterator()\" because \"ingredients\" is null";
                assertEquals(bwap, output);
            }
        };

        AnalyzeRecipeInteractor interactor = new AnalyzeRecipeInteractor(new SpoonacularAPI(), successPresenter);


        Recipe sampleRecipe = new Recipe(
                new Account("tester", "h"),
                9999999l,
                "",
                "",
                null,
                null,
                null
        );


        final AnalyzeRecipeInputData inputData = new AnalyzeRecipeInputData(sampleRecipe);
        interactor.executeTest(inputData);

    }
}
