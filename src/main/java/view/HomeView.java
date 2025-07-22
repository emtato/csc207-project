package view;

import data_access.DBUserDataAccessObject;
import entity.Account;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

// TODO: remove magic numbers

public class HomeView {
    public HomeView(Account user, DBUserDataAccessObject database) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        // frame.setSize(GUIConstants.WINDOW_WIDTH, GUIConstants.WINDOW_HEIGHT);
        frame.setTitle("");
        frame.setVisible(true);

        JPanel sideBar = new JPanel();
        sideBar.setBackground(GUIConstants.WHITE);
        Dimension sideBarDim = new Dimension(182, 1000);
        sideBar.setPreferredSize(sideBarDim);
        sideBar.setMaximumSize(sideBarDim);
        sideBar.setMinimumSize(sideBarDim);
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.add(Box.createVerticalStrut(10));

        JPanel profile = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        profile.setMaximumSize(new Dimension(182, 50));
        profile.setBackground(GUIConstants.WHITE);
        profile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profile.add(new JLabel(user.getName(), 19, GUIConstants.BLACK, Font.BOLD));
        profile.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(null);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GUIConstants.WHITE);
        Dimension dimension = new Dimension(500, 159);
        header.setPreferredSize(dimension);
        header.setMinimumSize(dimension);
        header.setMaximumSize(dimension);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(null);
        north.add(new JLabel("Home", GUIConstants.TITLE_SIZE, GUIConstants.BLACK, Font.BOLD),
                BorderLayout.WEST);
        header.add(north, BorderLayout.NORTH);

        JTextArea postIn = new JTextArea("Share your thoughts...", 18, 20);
        header.add(new JScrollPane(postIn), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(null);

        // TODO: continue code
    }
}
