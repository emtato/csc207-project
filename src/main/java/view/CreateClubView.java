package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;

public class CreateClubView extends JPanel {
    private final String viewName = "create club view";
    private final ViewManagerModel viewManagerModel;

    public CreateClubView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;

        JPanel mainPanel = new JPanel();



        this.add(mainPanel);
    }

    public String getViewName() {
        return viewName;
    }

}
