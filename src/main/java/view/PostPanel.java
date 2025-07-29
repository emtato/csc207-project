package view;

import data_access.spoonacular.SpoonacularAPI;
import entity.Account;
import entity.Comment;
import entity.Post;
import entity.Recipe;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

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
    private JPanel cardPanel;

    // fonts & styles
    private final Font fontTitle = new Font("Roboto", Font.BOLD, 20);
    private final Font subtite = new Font("Roboto", Font.PLAIN, 16);
    private final Font text = new Font("Roboto", Font.PLAIN, 15);
    // middle
    private JTextPane postText = new JTextPane();
    // bottom

    // right
    private RoundedButton likeButton = new RoundedButton("Like");
    private final RoundedButton saveButton = new RoundedButton("Add to list");
    private final RoundedButton shareButton = new RoundedButton("Share");
    private final RoundedButton viewFullPost = new RoundedButton("view full post");

    private final JLabel title;
    private final JLabel subtitle;

    private boolean liked;
    private JPanel centerPanel;
    private JPanel rightPanel;

    public PostPanel(ViewManagerModel viewManagerModel, Post post, int postWidth, int postHeight, JPanel cardPanel) {
        this.viewManagerModel = viewManagerModel;
        this.post = post;
        this.cardPanel = cardPanel;

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        centerPanel = new JPanel();

        // top
        title = new javax.swing.JLabel(post.getTitle()); //recipe/post title "HELLLOOOO aaiaiaiee" you will not be forgotten
        title.setFont(fontTitle);

        topPanel.add(title); //TODO: fix datetime thing
        subtitle = new javax.swing.JLabel(post.getUser().getUsername() + " | " + post.getDateTime() + " | " + post.getLikes() + " likes"); // post author and date
        subtitle.setFont(subtite);
        subtitle.setForeground(Color.GRAY);
        javax.swing.JLabel tags = new JLabel("tags: " + post.getTags());
        tags.setFont(text);
        tags.setForeground(Color.LIGHT_GRAY);


        topPanel.add(subtitle);
        topPanel.add(tags);

        // middle
        postText.setEditable(false);
       if (post instanceof Recipe) {
            this.repice = (Recipe) post;
            //TODO: hiii em its me work on this part next html formatting to make things pretty okay thanks bye + add comments
            String mainContent = """
                    <html>
                      <body style='font-family: comic sans, sans-serif'>
                        <h1 style='font-size: 18pt; color: #333'> <strong>Description</strong> </h1>
                        <p style='font-size: 14pt;'> """ + this.repice.getDescription() + """ 
                    </p>
                    
                    <h2 style='font-size: 16pt; color: #555;'>Ingredients</h2>
                    <ul>""" + this.repice.getIngredients() + """
                    </ul>
                    <h2 style='font-size: 16pt; color: #555;'>Steps</h2>
                    <p>""" + this.repice.getSteps().replace("\n", "<br>") + """
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


        centerPanel.add(postText);
        centerPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        // right
        ArrayList<JButton> rightButtons = new ArrayList<>();
        if (liked) {
            likeButton.setText("unlike");
        }
        rightButtons.add(likeButton);
        rightButtons.add(saveButton);
        rightButtons.add(shareButton);
        rightButtons.add(viewFullPost);
        for (JButton button : rightButtons) {
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
            rightPanel.add(button);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        this.add(mainPanel);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void actionPerformed(ActionEvent e) throws IOException, InterruptedException {
        if (e.getSource() == likeButton) {
            if (!liked) {
                System.out.println("me likey likey");
                post.setLikes(post.getLikes() + 1);
                likeButton.setText("unlike");
                liked = true;
            }
            else {
                post.setLikes(post.getLikes() - 1);
                likeButton.setText("like");
                liked = false;
            }
            subtitle.setText(post.getUser().getUsername() + " | " + post.getLikes() + " likes");

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

            for (Component c : cardPanel.getComponents()) {
                if (c instanceof PostView) {
                    ((PostView) c).displayPost(this.post);   // push the chosen post into it
                    break;
                }
            }
        }
    }
}
