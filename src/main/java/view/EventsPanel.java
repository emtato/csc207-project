package view;

import entity.Account;
import entity.Event;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import javax.swing.JLabel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventsPanel extends JPanel {

    private final ViewManagerModel viewManagerModel;

    private Event jet2holiday = new Event(new Account("Jingle", "Bell"), 1234321234,
            "Jet2Holiday", "Nothing beats a Jet2 holiday, and right now, you can save £50 per person. \" " +
            "+\n" + "That's £200 off a family of 4!", "BA3185", LocalDate.of(2025, 8, 12),
            new ArrayList<>(), new ArrayList<>());

    public EventsPanel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.setLayout(new BorderLayout());

        // set fonts
        Font eventTitleFont = new Font("Sans Serif", Font.BOLD, 14);
        Font eventDescFont = new Font("Sans Serif", Font.PLAIN, 10);


        // Single event panel
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));

        // Event components
        JLabel eventLabel = new JLabel(jet2holiday.getTitle());
        eventLabel.setFont(eventTitleFont);
        JLabel eventDate = new JLabel("Date: " + jet2holiday.getDate().toString());
        JLabel eventLocation = new JLabel("Location: " + jet2holiday.getLocation());
        JLabel eventDesc = new JLabel(jet2holiday.getDescription());
        eventDesc.setFont(eventDescFont);
        eventDate.setFont(eventDescFont);
        eventLocation.setFont(eventDescFont);
        JButton viewEvent = new JButton("View");
        viewEvent.setFont(eventDescFont);

        // Add components to event panel
        eventPanel.add(eventLabel);
        eventPanel.add(eventDate);
        eventPanel.add(eventLocation);
        eventPanel.add(eventDesc);
        eventPanel.add(viewEvent);
        formatEventPanel(eventPanel);

        // Add event panel to main panel
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(eventPanel);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    public static void formatEventPanel(JPanel eventPanel) {
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBackground(Color.PINK);
    }
}
