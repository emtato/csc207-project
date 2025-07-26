package interface_adapter.explore;

import interface_adapter.ViewModel;

public class ExploreViewModel extends ViewModel<ExploreState> {

    public ExploreViewModel() {
        super ("explore_events");
        setState(new ExploreState());
        //fix edit
    }
}
