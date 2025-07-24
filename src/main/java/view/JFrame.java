package view;

@SuppressWarnings("serial")
public class JFrame extends javax.swing.JFrame {

    public JFrame() {
        super("Munchable");
        getContentPane().setBackground(GUIConstants.WHITE);
        setSize(GUIConstants.WINDOW_WIDTH, GUIConstants.WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

}
