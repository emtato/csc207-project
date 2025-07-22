package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;


import interface_adapter.clubs.ClubViewModel;
import interface_adapter.clubs.ClubState;

/**
 * The View for the Club Home Page.
 * This view displays the club's home page with a title.
 */
public class ClubHomePageView extends JPanel {

    private final String viewName = "club view";
    private final ClubViewModel clubViewModel;


    public ClubHomePageView(ClubViewModel clubViewModel) {
//        System.out.println("ClubHomePageView constructor called");
        this.clubViewModel = clubViewModel;

        JLabel title = new JLabel("WAAAAAAAAAAAAAAAAAAAAA"); //get recipe/post title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(title);

    }

    public String getViewName() {
        return viewName;
    }
}
