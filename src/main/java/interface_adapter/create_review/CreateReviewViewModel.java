package interface_adapter.create_review;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CreateReviewViewModel extends ViewModel {
    private CreateReviewState state = new CreateReviewState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public CreateReviewViewModel() {
        super("create review");
    }

    public void setState(CreateReviewState state) { this.state = state; }

    public CreateReviewState getState() { return state; }

    @Override
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
