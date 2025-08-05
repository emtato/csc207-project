// src/main/java/view/MenuBarPanel.java
package view.ui_components;

import javax.swing.*;
import java.awt.*;

import interface_adapter.ViewManagerModel;
import view.GUIConstants;

public class MenuBarPanel extends JPanel {
    public MenuBarPanel(ViewManagerModel viewManagerModel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        Dimension buttonSize = new Dimension(GUIConstants.MAIN_BUTTON1_WIDTH, GUIConstants.MAIN_BUTTON1_HEIGHT);

        // Create home button with custom image
        ImageIcon homeIcon = new ImageIcon(getClass().getResource("/images/home.png"));
        Image homeImg = homeIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton homeButton = new JButton(new ImageIcon(homeImg));
        homeButton.setToolTipText("Home");
        styleButton(homeButton, buttonSize);

        // Create clubs button with system icon
        ImageIcon clubsIcon = new ImageIcon(getClass().getResource(
                "/images/toppng.com-white-person-icon-people-white-icon-abstract-backgrounds-436x368.png"));
        Image clubImg = clubsIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton clubsButton = new JButton(new ImageIcon(clubImg));
        clubsButton.setToolTipText("Clubs");
        styleButton(clubsButton, buttonSize);

        // Create settings button with system icon
        ImageIcon settingsIcon = new ImageIcon(getClass().getResource(
                "/images/[CITYPNG.COM]Settings White Outline Icon Download PNG - 1500x1500.png"));
        Image settingsImg = settingsIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton settingsButton = new JButton(new ImageIcon(settingsImg));
        settingsButton.setToolTipText("Settings");
        styleButton(settingsButton, buttonSize);

        // Create explore button with system icon
        ImageIcon exploreIcon = new ImageIcon(getClass().getResource(
                "/images/[CITYPNG.COM]Search Explore White Icon Transparent PNG - 4000x4000.png"));
        Image exploreImg = exploreIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton exploreButton = new JButton(new ImageIcon(exploreImg));
        exploreButton.setToolTipText("Explore");
        styleButton(exploreButton, buttonSize);

        // Create notifications button with system icon
        ImageIcon notificationsIcon = new ImageIcon(getClass().getResource("/images/appointment-reminders.png"));
        Image notificationsImg = notificationsIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton notificationsButton = new JButton(new ImageIcon(notificationsImg));
        notificationsButton.setToolTipText("Notifications");
        styleButton(notificationsButton, buttonSize);

        // Create profile button with system icon
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/images/pngaaa.com-4877784.png"));
        Image profileImg = profileIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton profileButton = new JButton(new ImageIcon(profileImg));
        profileButton.setToolTipText("Profile");
        styleButton(profileButton, buttonSize);

        // Add buttons to panel
        add(Box.createHorizontalStrut(10));  // Add some padding
        add(homeButton);
        add(Box.createHorizontalStrut(5));
        add(exploreButton);
        add(Box.createHorizontalStrut(5));
        add(notificationsButton);
        add(Box.createHorizontalStrut(5));
        add(clubsButton);
        add(Box.createHorizontalStrut(5));
        add(profileButton);
        add(Box.createHorizontalStrut(5));
        add(settingsButton);
        add(Box.createHorizontalStrut(10));

        homeButton.addActionListener(e -> viewManagerModel.setState("homepage view"));
        clubsButton.addActionListener(e -> viewManagerModel.setState("club view"));
        settingsButton.addActionListener(e -> viewManagerModel.setState("settings"));
        exploreButton.addActionListener(e -> viewManagerModel.setState("explore view"));
        notificationsButton.addActionListener(e -> viewManagerModel.setState("notifications view"));
        profileButton.addActionListener(e -> viewManagerModel.setState("profile"));
    }

    private void styleButton(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBackground(GUIConstants.RED);
        button.setOpaque(true);
    }
}
