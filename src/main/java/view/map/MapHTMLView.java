package view.map;

import view.JFrame;

import javax.swing.*;
import java.awt.*;

public class MapHTMLView extends view.JFrame {

    // TODO: this is just an example. change later

    public MapHTMLView() {
        setTitle("HTML Display Example");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false); // Make it read-only

        // Example HTML content
        String htmlContent = "<html><body><h1>Welcome!</h1><p>This is a <b>simple</b> HTML example in Java Swing.</p></body></html>";
        editorPane.setText(htmlContent);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MapHTMLView().setVisible(true);
        });
    }
}