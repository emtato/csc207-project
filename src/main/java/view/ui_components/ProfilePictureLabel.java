package view.ui_components;

import java.net.MalformedURLException;
import java.net.URL;

public class ProfilePictureLabel extends JLabel {
    private int width;
    private int height;
    public ProfilePictureLabel(String url, int width, int height) {
        this.width = width;
        this.height = height;
        try {
            final URL pfpURL = new URL(url);
            Image profilePictureImage = new ImageIcon(pfpURL).getImage();
            profilePictureImage = profilePictureImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            final ImageIcon profilePictureIcon = new ImageIcon(profilePictureImage);
            this.setIcon(profilePictureIcon);
            this.setPreferredSize(new Dimension(width, height));
        }
        catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
    }

    public void updateIcon(String url){
        try {
            final URL pfpURL = new URL(url);
            Image profilePictureImage = new ImageIcon(pfpURL).getImage();
            profilePictureImage = profilePictureImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            final ImageIcon profilePictureIcon = new ImageIcon(profilePictureImage);
            this.setIcon(profilePictureIcon);
        }
        catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
    }
}
