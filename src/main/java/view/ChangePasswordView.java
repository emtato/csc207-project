package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_adapter.change_password.*;

// TODO: Check if code works

public class ChangePasswordView implements ActionListener, PropertyChangeListener {

    private ChangePasswordController changePasswordController;

    public ChangePasswordView(LoggedInViewModel loggedInViewModel) {

        loggedInViewModel.addPropertyChangeListener(this);

        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(null);
        panel.setBorder(BorderFactory.createEmptyBorder(83, 99, 175, 99));
        panel.add(new JLabel(getViewName(), 40, GUIConstants.RED, Font.BOLD),
                BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 10, 10));
        center.setBackground(null);
        center.setBorder(BorderFactory.createEmptyBorder(58, 216, 0, 216));

        JTextField oldPassword = new JTextField("Old Password");
        center.add(oldPassword);
        JTextField newPassword = new JTextField("New Password");
        center.add(newPassword);
        JTextField confirmPassword = new JTextField("Confirm Password");
        center.add(confirmPassword);

        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(null);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(83, 99, 175, 99));
        buttonPanel.add(submit);
        buttonPanel.add(cancel);

        submit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(submit)) {
                            final LoggedInState currentState = loggedInViewModel.getState();
                            if (oldPassword.getText().isEmpty()) {
                                new Alert("Please enter your old password", frame);
                                return;
                            }
                            if (!oldPassword.getText().equals(currentState.getPassword())) {
                                new Alert("Old password doesn't match", frame);
                                return;
                            }
                            if (newPassword.getText().isEmpty()) {
                                new Alert("Please enter new password", frame);
                                return;
                            }
                            if (newPassword.getText().length()<6) {
                                new Alert("Password must contains at least 6 characters", frame);
                                return;
                            }
                            if (confirmPassword.getText().isEmpty()) {
                                new Alert("Please confirm password", frame);
                                return;
                            }
                            if (!newPassword.getText().equals(confirmPassword.getText())) {
                                new Alert("Password doesn't match", frame);
                                return;
                            }
                            currentState.setPassword(newPassword.getText());
                            changePasswordController.execute(
                                    currentState.getPassword(),
                                    currentState.getUsername()
                            );
                        }
                    }
                }
        );

        cancel.addActionListener(this);

        panel.add(center, BorderLayout.CENTER);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.requestFocus();
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    @Override
    public void actionPerformed(ActionEvent evt) { System.out.println("Click " + evt.getActionCommand()); }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public String getViewName() {
        return "Change Password";
    }
}
