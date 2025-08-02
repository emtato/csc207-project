package interface_adapter;

import view.PostView;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class ViewManagerModel extends ViewModel<String> {
    private PostView postView;

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
}
