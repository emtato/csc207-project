package view.ui_components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import entity.Restaurant;
import interface_adapter.map.MapViewModel;
import view.map.MapView;

/**
 * Simple Swing panel that displays a few Restaurant/map fields from a MapViewModel.
 * It does not depend on your MapView implementation — you call update(...) with concrete data.
 */
public class MapPanel extends JPanel {
    private final JLabel nameLabel = new JLabel();
    private final JLabel addressLabel = new JLabel();
    private final JLabel phoneLabel = new JLabel();
    private final JLabel cuisinesLabel = new JLabel();
    private final JLabel priceLabel = new JLabel();
    JPanel mapViewPanel = new JPanel();

    public MapPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        setBackground(Color.WHITE);

        nameLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        add(nameLabel);
        add(Box.createRigidArea(new Dimension(0,6)));
        add(addressLabel);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(phoneLabel);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(cuisinesLabel);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(priceLabel);
        add(mapViewPanel);
    }

    public void update(MapViewModel mapViewModel, Restaurant restaurant) {

        MapView mapView = new MapView(mapViewModel, restaurant, mapViewPanel);

        String name = restaurant.getName();
        String address = restaurant.getAddress();
        String phone = restaurant.getPhone();
        ArrayList<String> cuisines = restaurant.getCuisines();
        String priceLevel = restaurant.getPriceLevel();

        nameLabel.setText(name == null || name.isBlank() ? "Unknown" : "<html><b>" + name + "</b></html>");
        addressLabel.setText(address == null || address.isBlank() ? "Address unknown" : address);
        phoneLabel.setText(phone == null || phone.isBlank() ? "" : "Phone: " + phone);
        cuisinesLabel.setText(cuisines.toString() == null || cuisines.toString().isBlank() ? "" : "Cuisines: " + cuisines);
        priceLabel.setText(priceLevel == null || priceLevel.isBlank() ? "" : "Price: " + priceLevel);
        revalidate();
        repaint();
    }
}
