package view;

import data_access.DBUserDataAccessObject;
import entity.Account;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

// TODO: modify parameters, using GUI constants (ie. remove magic numbers)

public class Modify {
    public Modify(Account user, DBUserDataAccessObject database) {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(null);

        panel.setBorder(BorderFactory.createEmptyBorder(72, 84, 149, 84));
        panel.add(new JLabel("Modify Your Profile", 40, GUIConstants.RED, Font.BOLD),
                BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 10, 10));
        center.setBackground(null);
        center.setBorder(BorderFactory.createEmptyBorder(50, 231, 17, 231));


        JTextField username = new JTextField("Username");
        username.setText(user.getUsername());
        username.setForeground(Color.BLACK);
        center.add(username);

        JTextField name = new JTextField("Name");
        name.setText(user.getName());
        name.setForeground(Color.BLACK);
        center.add(name);

        JTextField email = new JTextField("Email");
        email.setText(user.getEmail());
        email.setForeground(Color.BLACK);
        center.add(email);

        JButton submit = new JButton("Submit");
        submit.addMouseListener(new MouseListener() {
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
                if (username.getText().isEmpty()) {
                    new Alert("First Name cannot be empty", frame);
                    return;
                }
                if (name.getText().isEmpty()) {
                    new Alert("Last Name cannot be empty", frame);
                    return;
                }
                if (email.getText().isEmpty()) {
                    new Alert("Email cannot be empty", frame);
                    return;
                }

                Account updatedUser = user;
                updatedUser.setUsername(username.getText());
                updatedUser.setName(name.getText());
                updatedUser.setEmail(email.getText());
                new HomeView(user, database);
            }
        });
        center.add(submit);
        panel.add(center, BorderLayout.CENTER);

        JLabel changePassword = new JLabel("Change Password", 20, GUIConstants.BLACK, Font.BOLD);
        changePassword.addMouseListener(new MouseListener() {
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
                // TODO: fix ChangePassword
//                new ChangePassword(user, database);
//                frame.dispose();
            }
        });
        changePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePassword.setHorizontalAlignment(JLabel.CENTER);
        panel.add(changePassword, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.requestFocus();
    }
}
