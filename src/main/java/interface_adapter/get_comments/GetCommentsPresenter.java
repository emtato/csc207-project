package interface_adapter.get_comments;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import use_case.get_comments.GetCommentsOutputBoundary;
import use_case.get_comments.GetCommentsOutputData;

public class GetCommentsPresenter implements GetCommentsOutputBoundary {
    private final GetCommentsViewModel viewModel;

    public GetCommentsPresenter(GetCommentsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GetCommentsOutputData outputData) {
        viewModel.setComments(outputData.getComments());
        viewModel.firePropertyChanged();
    }

    public void prepareFailView(String errorMessage) {
        System.err.println("Failed to get comments: " + errorMessage);
    }
}
