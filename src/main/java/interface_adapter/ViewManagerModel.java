package interface_adapter;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class ViewManagerModel extends ViewModel<String> {

    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }

    public void setState(String newState) {
       // System.out.println("[DEBUG] setState called with: " + newState);
        super.setState(newState);
        this.firePropertyChanged("state");
    }

}
