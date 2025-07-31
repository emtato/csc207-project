package view;

import entity.Account;
import interface_adapter.ViewManagerModel;

import javax.swing.*;

import interface_adapter.explore_events.ExploreEventsViewModel;
import java.awt.*;

import entity.Account;
import entity.Event;

import javax.swing.JLabel;

import java.time.LocalDate;
import java.util.ArrayList;



/* Description:
 * ✨look at events ✨
 */


public class ExploreEventsView extends JPanel {
    private final String viewName = "explore events view";
    private final ViewManagerModel viewManagerModel;

    private Event jet2holiday = new Event(new Account("Jingle", "Bell"), 1234321234,
        "Jet2Holiday", "Nothing beats a Jet2 holiday, and right now, you can save £50 per person. \" " +
        "+\n" + "That's £200 off a family of 4!", "BA3185", LocalDate.of(2025, 8, 12),
        new ArrayList<>(), new ArrayList<>());

    public ExploreEventsView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.setLayout(new BorderLayout());

        // set fonts
        Font titleFont = new Font("Roboto", Font.BOLD, 20);
        Font eventTitleFont = new Font("Roboto", Font.BOLD, 18);
        Font eventDescFont = new Font("Roboto", Font.PLAIN, 15);

        // title
        JLabel title = new JLabel("Explore Events");

        title.setFont(titleFont);
        this.add(title, BorderLayout.NORTH);



        // events grid
        JPanel eventsGrid = new JPanel();
        eventsGrid.setLayout(new FlowLayout(FlowLayout.CENTER));

        for(int i = 0; i < 10; i++ ) {
            // event 1
            JLabel eventlabel = new JLabel(jet2holiday.getTitle());
            eventlabel.setFont(eventTitleFont);
            JLabel eventdate = new JLabel("Date: " + jet2holiday.getDate().toString());
            JLabel eventlocation = new JLabel("Location: " + jet2holiday.getLocation());
            JLabel eventdesc = new JLabel(jet2holiday.getDescription());
            eventdesc.setFont(eventDescFont);
            eventdate.setFont(eventDescFont);
            eventlocation.setFont(eventDescFont);
            JButton viewevent = new JButton("View");
            viewevent.setFont(eventDescFont);

            JPanel event = new JPanel();
            event.setLayout(new BoxLayout(event, BoxLayout.Y_AXIS));
            event.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            event.setPreferredSize(new Dimension(600, 200));
            event.add(eventlabel);
            event.add(eventdate);
            event.add(eventlocation);
            event.add(eventdesc);
            event.add(viewevent);
            formatEventPanel(event);
            eventsGrid.add(event);
        }

        this.add(eventsGrid, BorderLayout.CENTER);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        add(menuBar, BorderLayout.SOUTH);
    }

    public static void formatEventPanel(JPanel eventPanel) {
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBackground(Color.PINK);
    }

    public String getViewName() {
        return viewName;
    }




////  test  /////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ExploreEventsView(new ViewManagerModel()));
        frame.pack();
        frame.setVisible(true);
    }

}




