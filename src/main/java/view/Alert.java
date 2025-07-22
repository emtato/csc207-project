package view;

import org.jetbrains.annotations.NotNull;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Alert {

    public Alert(String content, JFrame parent) {
        JFrame frame = new JFrame();
        frame.setSize(GUIConstants.ALERT_WIDTH, GUIConstants.ALERT_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        JPanel panel = getJPanel(content);

        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
    }

    @NotNull
    private static JPanel getJPanel(String content) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(GUIConstants.WHITE);

        JLabel title = new JLabel("Alert", GUIConstants.HEADER_SIZE, GUIConstants.RED, Font.BOLD);
        title.setHorizontalAlignment(JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);

        JLabel msg = new JLabel(content, GUIConstants.TEXT_SIZE, GUIConstants.BLACK, Font.BOLD);
        msg.setHorizontalAlignment(JLabel.CENTER);
        panel.add(msg, BorderLayout.CENTER);
        return panel;
    }
}
