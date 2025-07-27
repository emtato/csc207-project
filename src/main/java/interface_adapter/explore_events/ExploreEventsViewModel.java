package interface_adapter.explore_events;

import interface_adapter.ViewModel;


public class ExploreEventsViewModel extends ViewModel<ExploreEventsState> {
    public static final String TITLE_LABEL = "Explore Events";

    public ExploreEventsViewModel() {
        super("explore_events");
        setState(new ExploreEventsState());
    }
}
        //fix edit

