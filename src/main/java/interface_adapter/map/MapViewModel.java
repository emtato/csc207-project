package interface_adapter.map;

import interface_adapter.ViewModel;


public class MapViewModel extends ViewModel<MapState> {

    public MapViewModel() {
        super("map view");
        setState(new MapState());
    }
}
