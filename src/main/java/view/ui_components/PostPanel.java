package view.ui_components;

import entity.Post;
import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.like_post.LikePostController;
import view.PostView;

import javax.swing.*;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Simplified PostPanel view to be embedded onto another view
 * <p>
 * This panel displays a summary of a Post or Recipe with user info, tags,
 * and a short preview of the post content. it also provides action buttons
 * to like, save, share, or view the full post
 * </p>
 */
public class PostPanel extends JPanel {

    private final ViewManagerModel viewManagerModel;

    private Post post;
    private Recipe repice;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    // fonts & styles
    private final Font fontTitle = new Font("Roboto", Font.BOLD, 16);
    private final Font subtite = new Font("Roboto", Font.PLAIN, 15);
    private final Font text = new Font("Roboto", Font.PLAIN, 13);
    // middle
    private JTextPane postText = new JTextPane();
    // bottom
    private RoundedButton likeButton = new RoundedButton("Like");
    private final RoundedButton saveButton = new RoundedButton("Add to list");
    private final RoundedButton shareButton = new RoundedButton("Share");
    private final RoundedButton viewFullPost = new RoundedButton("view full post");

    private final JLabel title;
    private final JLabel subtitle;

    private boolean liked;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private LikePostController likePostController;

    public PostPanel(ViewManagerModel viewManagerModel, Post post, int postWidth, int postHeight,
                     LikePostController likePostController) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        this.viewManagerModel = viewManagerModel;
        this.post = post;
        this.likePostController = likePostController;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel = new JPanel();
        // topPanel.setAlignmentX();
        bottomPanel.setLayout(new FlowLayout());
        centerPanel = new JPanel();

        // top
        title = new javax.swing.JLabel(post.getTitle()); //recipe/post title "HELLLOOOO aaiaiaiee" you will not be forgotten
        title.setFont(fontTitle);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(title); //TODO: fix datetime thing
        subtitle = new javax.swing.JLabel(post.getUser().getUsername() + " | " + post.getDateTime().format(formatter) + " | " + post.getLikes() + " likes"); // post author and date
        subtitle.setFont(subtite);
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        javax.swing.JLabel tags = new JLabel("tags: " + post.getTags());
        tags.setFont(text);
        tags.setForeground(Color.LIGHT_GRAY);
        tags.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(subtitle);
        topPanel.add(tags);

        // if has media:
        int maxBoxHeight = 739123617;
        if (post.isImageVideo()) {
            try {
                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                ArrayList<String> imageURLS = post.getImageURLs();
                for (int i = 0; i < Math.min(3, imageURLS.size()); i++) {
                    String imageURL = imageURLS.get(i);
                    if (!imageURL.startsWith("http://") && !imageURL.startsWith("https://")) {
                        System.out.println("Invalid image URL: " + imageURL);
                        continue; // skip invalid URLs
                    }
                    URL url = new URL(imageURL);
                    ImageIcon imageIcon = new ImageIcon(url);
                    Image img = imageIcon.getImage().getScaledInstance(-1, 200, Image.SCALE_SMOOTH);

                    int imgW = img.getWidth(this);
                    int imgH = img.getHeight(this);

                    int finalW = imgW;
                    int finalH = imgH;
                    float ratioW;
                    if (imgW > 150) {
                        finalW = 150;
                        ratioW = imgW / 150f;
                        finalH = (int) (imgH / ratioW);
                    }


                    img = img.getScaledInstance(finalW, finalH, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(img);
                    JLabel image = new JLabel(scaledIcon);
                    image.setAlignmentX(Component.CENTER_ALIGNMENT);
                    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

                    imagePanel.add(image);
                }
                centerPanel.add(imagePanel);

                maxBoxHeight = 5;
            }
            catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        // middle
        postText.setEditable(false);
        postText.setPreferredSize(new Dimension(postWidth, postHeight));
        postText.setMaximumSize(new Dimension(postWidth, Integer.MAX_VALUE));
        if (post instanceof Recipe) {
            this.repice = (Recipe) post;
            //TODO: hiii em its me work on this part next html formatting to make things pretty okay thanks bye + add comments
            String mainContent = """
                    <html>
                      <body style='font-family: comic sans, sans-serif'>
                        <h1 style='font-size: 18pt; color: #333'> <strong>Description</strong> </h1>
                        <p style='font-size: 14pt;'> """ + this.repice.getDescription() + """ 
                    </p>
                          </body>
                        </html>
                    """;
            postText.setContentType("text/html");
            postText.setText(mainContent);

        }
        else if (post.isImageVideo()) {
            System.out.println("cry");
        }
        else {
            String description = post.getDescription();
            postText.setText(description);

        }


        centerPanel.add(postText);

        centerPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        // bottom
        ArrayList<JButton> bottomButtons = new ArrayList<>();
        if (liked) {
            likeButton.setText("unlike");
        }
        bottomButtons.add(likeButton);
        bottomButtons.add(saveButton);
        bottomButtons.add(shareButton);
        bottomButtons.add(viewFullPost);
        for (JButton button : bottomButtons) {
            button.setFont(text);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.addActionListener(e -> {
                try {
                    actionPerformed(e);
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                    System.out.println("NOOOOOO D:");
                }
            });
            bottomPanel.add(button);
            //bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    }

    public void actionPerformed(ActionEvent e) throws IOException, InterruptedException {
        if (e.getSource() == likeButton) {
            boolean isLiking = !liked;
            likePostController.likePost(post.getID(), isLiking);

            if (isLiking) {
                likeButton.setText("Unlike");
                post.setLikes(post.getLikes() + 1);
            }
            else {
                likeButton.setText("Like");
                post.setLikes(post.getLikes() - 1);
            }
            liked = isLiking;

            subtitle.setText(post.getUser().getUsername() + " | " + post.getDateTime().format(formatter) + " | " + post.getLikes() + " likes");

        }
        if (e.getSource() == saveButton) {
            JOptionPane.showMessageDialog(null, "havent added that yet cry abt it", "nerd", JOptionPane.INFORMATION_MESSAGE);

            System.out.println("popup add to list");
        }
        if (e.getSource() == shareButton) {
            System.out.println("share slop");
            JOptionPane.showMessageDialog(null, "here is the id of ths post share that or something \n" + post.getID(), "nerd", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == viewFullPost) {
            viewManagerModel.setState("post view");

            PostView currentPostView = viewManagerModel.getPostView();
            currentPostView.displayPost(this.post);   // push the chosen post into it

        }
    }
}
