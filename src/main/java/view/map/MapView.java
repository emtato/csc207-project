package view.map;

import app.AppProperties;
import entity.Restaurant;
import interface_adapter.map.MapViewModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.*;


// Source: https://stackoverflow.com/questions/17598074/google-map-in-java-swing
public class MapView {
    private final MapViewModel mapViewModel;
    public MapView(MapViewModel mapViewModel, Restaurant restaurant, JPanel test) {
        this.mapViewModel = mapViewModel;

  //      JFrame test = new JFrame("Google Maps");

        try {
            // Load API key from AppProperties
            AppProperties appProps = new AppProperties();
            Properties props = appProps.getProperties();
            String apiKey = props.getProperty("PLACES_API_KEY");
            String location =  ""; // e.g. "43.9559913,-78.8758528"

            if (restaurant != null) {
                 location = restaurant.getLocation();
            }

            // Build valid Static Maps API URL
            String imageUrl = "https://maps.googleapis.com/maps/api/staticmap"
                    + "?center=" + location
                    + "&zoom=15"
                    + "&size=800x800"
                    + "&key=" + apiKey;

            // Download the image
            String destinationFile = "image.jpg";
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();

            // Display the image in a JFrame
            ImageIcon icon = new ImageIcon(new ImageIcon(destinationFile)
                    .getImage().getScaledInstance(630, 600, java.awt.Image.SCALE_SMOOTH));

            test.add(new JLabel(icon));
//            test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            test.pack();
            test.setVisible(true);
            test.repaint();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

        public String getViewName() {
        return "map view";
    }
}
