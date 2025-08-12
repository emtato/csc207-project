package interface_adapter.write_comment;/**
 * Created by Emilia on 2025-08-11!
 * Description:
 * ^ • ω • ^
 */

import interface_adapter.ViewManagerModel;
import interface_adapter.get_comments.GetCommentsViewModel;
import interface_adapter.homepage.HomePageState;
import interface_adapter.homepage.HomePageViewModel;
import use_case.comment.CommentPostOutputBoundary;
import use_case.comment.CommentPostOutputData;
import use_case.fetch_post.FetchPostOutputBoundary;
import use_case.fetch_post.FetchPostOutputData;


public class WriteCommentPresenter implements CommentPostOutputBoundary {
//    private HomePageViewModel homePageViewModel;
//
//        public WriteCommentPresenter(ViewManagerModel homePageViewModel) {
//        this.homePageViewModel = homePageViewModel;
//    }

    @Override
    public void prepareSuccessView(CommentPostOutputData outputData) {
        System.out.println("yay");
    }

    @Override
    public void prepareFailView(String error) {
        System.err.println("noo");
    }
}
