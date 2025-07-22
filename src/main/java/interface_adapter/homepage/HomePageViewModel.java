package interface_adapter.homepage;

import interface_adapter.ViewModel;


public class HomePageViewModel extends ViewModel<HomePageState>{

    public HomePageViewModel() {
        super("homepage view");
        setState(new HomePageState());
    }
}
