package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;


import interface_adapter.ViewManagerModel;


/**
 * The View for the Club Home Page.
 * This view displays the club's home page with a title.
 */
public class ClubHomePageView extends JPanel {

    private final String viewName = "club view";
    private final ViewManagerModel viewManagerModel;


    public ClubHomePageView(ViewManagerModel viewManagerModel) {

        this.viewManagerModel = viewManagerModel;

        JLabel title = new JLabel("Clubs Page"); //get recipe/post title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(title);
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        add(menuBar, BorderLayout.NORTH);

    }

    public String getViewName() {
        return viewName;
    }
}
