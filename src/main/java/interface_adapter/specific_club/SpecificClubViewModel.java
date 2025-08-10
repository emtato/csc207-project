package interface_adapter.specific_club;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SpecificClubViewModel extends ViewModel {
    private SpecificClubState state = new SpecificClubState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public SpecificClubViewModel() {
        super("specific club view");
    }

    public void setState(SpecificClubState state) {
        this.state = state;
    }

    public SpecificClubState getState() {
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
