package view;

import javax.swing.*;
import java.awt.*;


/* Description:
 * ✨look at events ✨
 */


public class ExploreEventsView extends JPanel {
    private final String viewName = "explore events view";

    public ExploreEventsView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // set fonts
        Font titleFont = new Font("Sans Serif", Font.BOLD, 20);
        Font eventTitleFont = new Font("Sans Serif", Font.BOLD, 14);
        Font eventDescFont = new Font("Sans Serif", Font.PLAIN, 10);

        // title
        JLabel title = new JLabel("Explore Events");
        title.setFont(titleFont);
        this.add(title);

        // events grid
        JPanel eventsGrid = new JPanel();
        eventsGrid.setLayout(new FlowLayout(FlowLayout.LEFT));

        // event 1
        JLabel event1label = new JLabel("Event 1");
        event1label.setFont(eventTitleFont);
        JLabel event1desc= new JLabel("Nothing beats a Jet2 holiday, and right now, you can save £50 per person. " +
            "That's £200 off a family of 4!");
        event1desc.setFont(eventDescFont);
        JButton view1 = new JButton("View");
        view1.setFont(eventDescFont);

        JPanel event1 = new JPanel();
        event1.add(event1label);
        event1.add(event1desc);
        event1.add(view1);
        formatEventPanel(event1);
        eventsGrid.add(event1);

        // event 2
        JLabel event2label = new JLabel("Event 2");
        event2label.setFont(eventTitleFont);
        JLabel event2desc= new JLabel("Nothing beats a Jet2 holiday, and right now, you can save £50 per person. " +
            "That's £200 off a family of 4!");
        event2desc.setFont(eventDescFont);
        JButton view2 = new JButton("View");
        view2.setFont(eventDescFont);

        JPanel event2 = new JPanel();
        event2.add(event2label);
        event2.add(event2desc);
        event2.add(view2);
        formatEventPanel(event2);
        eventsGrid.add(event2);

        // event 3
        JLabel event3label = new JLabel("Event 3");
        event3label.setFont(eventTitleFont);
        JLabel event3desc= new JLabel("Nothing beats a Jet2 holiday, and right now, you can save £50 per person. " +
            "That's £200 off a family of 4!");
        event3desc.setFont(eventDescFont);
        JButton view3 = new JButton("View");
        view3.setFont(eventDescFont);

        JPanel event3 = new JPanel();
        event3.add(event3label);
        event3.add(event3desc);
        event3.add(view3);
        formatEventPanel(event3);
        eventsGrid.add(event3);

        // event 4
        JLabel event4label = new JLabel("Event 4");
        event4label.setFont(eventTitleFont);
        JLabel event4desc= new JLabel("Nothing beats a Jet2 holiday, and right now, you can save £50 per person. " +
            "That's £200 off a family of 4!");
        event4desc.setFont(eventDescFont);
        JButton view4 = new JButton("View");
        view4.setFont(eventDescFont);

        JPanel event4 = new JPanel();
        event4.add(event4label);
        event4.add(event4desc);
        event4.add(view4);
        formatEventPanel(event4);
        eventsGrid.add(event4);

        this.add(eventsGrid);

        JPanel buttonsBar = new JPanel();
        buttonsBar.setLayout(new BoxLayout(buttonsBar, BoxLayout.X_AXIS));
        JButton home = new JButton("Home");
        JButton settings = new JButton("Settings");
        JButton explore = new JButton("Explore");
        buttonsBar.add(home);
        buttonsBar.add(explore);
        buttonsBar.add(settings);
        this.add(buttonsBar);
    }

    public static void formatEventPanel(JPanel eventPanel) {
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBackground(Color.PINK);
    }



//  test  /////////////////////////////////////////////////////////////////////
/*
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ExploreEventsView());
        frame.pack();
        frame.setVisible(true);
    }
*/
}




