package app;

import javax.swing.*;
import java.awt.*;

/**
 * Uses the App Director instead of App Builder to build the program
 */
public class Munchables {
    /**
     * Builds and runs the CA architecture of the application.
     *
     * @param args unused arguments
     */
    public static void main(String[] args) {
        final AppDirector appDirector = AppDirector.getInstance();
        final JFrame application = appDirector.buildDBApp();
        application.setPreferredSize(new Dimension(1440, 900));
        application.pack();
        application.setVisible(true);
    }
}
