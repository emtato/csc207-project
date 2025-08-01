package view;

import interface_adapter.ViewManagerModel;
import view.UI_components.MenuBarPanel;

import javax.swing.*;
import javax.swing.JLabel;
import java.awt.*;

public class NotificationsView extends JPanel {
    private final String viewName = "notifications view";
    private final ViewManagerModel viewManagerModel;

    public NotificationsView(ViewManagerModel viewManagerModel) {

        this.viewManagerModel = viewManagerModel;

        javax.swing.JLabel title = new JLabel("Notifications Page"); //get recipe/post title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(title);
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        add(menuBar, BorderLayout.NORTH);
    }

    public String getViewName() {
        return viewName;
    }

}
