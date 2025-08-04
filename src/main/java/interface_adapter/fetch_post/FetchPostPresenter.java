package interface_adapter.fetch_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import use_case.fetch_post.FetchPostOutputBoundary;
import use_case.fetch_post.FetchPostOutputData;

public class FetchPostPresenter implements FetchPostOutputBoundary {
    private FetchPostOutputData viewModel;

    @Override
    public void prepareSuccessView(FetchPostOutputData data) {
        this.viewModel = data;
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println("Failed to fetch post: " + errorMessage);
    }
    public FetchPostOutputData getViewModel() {
    return viewModel;
}
}
