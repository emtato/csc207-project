package interface_adapter.get_comments;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

import use_case.get_comments.GetCommentsInputBoundary;
import use_case.get_comments.GetCommentsInputData;

public class GetCommentsController {
    private final GetCommentsInputBoundary interactor;

    public GetCommentsController(GetCommentsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void getComments(long postID) {
        GetCommentsInputData inputData = new GetCommentsInputData(postID);
        interactor.execute(inputData);
    }
}
