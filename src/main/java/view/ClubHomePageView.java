package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.clubs.ClubViewModel;
import interface_adapter.clubs.ClubState;

/* Description:
 * ^ • ω • ^
 */

public class ClubHomePageView extends JPanel {

    private final String viewName = "club view";
    private final ClubViewModel clubViewModel;


    public ClubHomePageView(ClubViewModel clubViewModel) {
        System.out.println("ClubHomePageView constructor called");
        this.clubViewModel = clubViewModel;

        JLabel title = new JLabel("WAAAAAAAAAAAAAAAAAAAAA"); //get recipe/post title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
//        clubViewModel.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (evt.getPropertyName().equals(ClubState.TITLE_PROPERTY)) {
//                    title.setText(clubViewModel.getState().getTitle());
//                }
//            }
//        });
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new FlowLayout()); //pannel for main ui buttons (persists across views)
        JPanel rightPanel = new JPanel(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel centerPanel = new JPanel();

        //top
        //middle
        JTextArea recipeText = new JTextArea();

        this.add(title);

    }

    public String getViewName() {
        return viewName;
    }
}
