package interface_adapter.create_post_view;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CreatePostViewModel extends ViewModel {
    private CreatePostState state = new CreatePostState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public CreatePostViewModel() {
        super("create post");
    }

    public void setState(CreatePostState state) {
        this.state = state;
    }

    public CreatePostState getState() {
        return state;
    }

    @Override
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
