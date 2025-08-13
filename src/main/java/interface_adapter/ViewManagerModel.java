package interface_adapter;

import view.CreateNewPostView;
import view.CreateNewReviewView;
import view.HomePageView;
import view.PostView;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class ViewManagerModel extends ViewModel<String> {
    private PostView postView;
    private HomePageView homePageView;
    private CreateNewPostView createNewPostView;
    private CreateNewReviewView createNewReviewView;
    private view.SpecificClubView specificClubView;

    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }

    public void setState(String newState) {
        // System.out.println("[DEBUG] setState called with: " + newState);
        super.setState(newState);
        this.firePropertyChanged("state");
    }

    public PostView getPostView() {
        return postView;
    }

    public void setPostView(PostView postView) {
        this.postView = postView;
    }

    public HomePageView getHomePageView() {
        return homePageView;
    }

    public void setHomePageView(HomePageView homePageView) {
        this.homePageView = homePageView;
    }
    public CreateNewPostView getCreateNewPostView(){
        return createNewPostView;
    }
    public void setCreateNewPostView(CreateNewPostView postView){
        this.createNewPostView = postView;
    }
    public CreateNewReviewView getCreateNewReviewView() { return createNewReviewView; }
    public void setNewReviewView(CreateNewReviewView newReviewView) { this.createNewReviewView = newReviewView; }
    public view.SpecificClubView getSpecificClubView() {
        return specificClubView;
    }

    public void setSpecificClubView(view.SpecificClubView specificClubView) {
        this.specificClubView = specificClubView;
    }
}
