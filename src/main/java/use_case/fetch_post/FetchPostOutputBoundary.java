package use_case.fetch_post;/**
 * Created by Emilia on 2025-08-03!
 * Description:
 * ^ • ω • ^
 */

public interface FetchPostOutputBoundary {
    void prepareSuccessView(FetchPostOutputData data);
    void prepareFailView(String errorMessage);

}
