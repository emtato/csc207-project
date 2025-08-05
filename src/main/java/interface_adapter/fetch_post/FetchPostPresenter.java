package interface_adapter.fetch_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import interface_adapter.homepage.HomePageState;
import interface_adapter.homepage.HomePageViewModel;
import use_case.fetch_post.FetchPostOutputBoundary;
import use_case.fetch_post.FetchPostOutputData;

public class FetchPostPresenter implements FetchPostOutputBoundary {
    private HomePageViewModel homePageViewModel;

    public FetchPostPresenter(HomePageViewModel homePageViewModel) {
        this.homePageViewModel = homePageViewModel;
    }

    @Override
    public void prepareSuccessView(FetchPostOutputData data) {
        final HomePageState state = this.homePageViewModel.getState();
        state.setPosts(data.getPosts());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println("Failed to fetch post: " + errorMessage);
    }

}
