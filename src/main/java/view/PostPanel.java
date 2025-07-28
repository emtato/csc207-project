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

public class PostPanel extends JPanel {

    private final ViewManagerModel viewManagerModel;

    private Post post;
    private Recipe repice;

    // fonts & styles
    private final Font fontTitle = new Font("Roboto", Font.BOLD, 20);
    private final Font subtite = new Font("Roboto", Font.PLAIN, 16);
    private final Font text = new Font("Roboto", Font.PLAIN, 15);
    private final Font whimsy = new Font("papyrus", Font.BOLD, 20);
    // middle
    private JTextPane postText = new JTextPane();
    // bottom

    // right
    private RoundedButton likeButton = new RoundedButton("Like");
    private final RoundedButton analyzeButton = new RoundedButton("Analyze");
    private final RoundedButton saveButton = new RoundedButton("Add to list");
    private final RoundedButton shareButton = new RoundedButton("Share");
    private final RoundedButton commentButton = new RoundedButton("coment");

    private final JLabel title;
    private final JLabel subtitle;

    private boolean liked;
    private JPanel centerPanel;
    private JScrollPane scrollPane;
    private JPanel rightPanel;
    private boolean xPresent;

    public PostPanel(ViewManagerModel viewManagerModel, Post post, int postWidth, int postHeight) {
        this.viewManagerModel = viewManagerModel;
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
            //TODO: hiii em its me work on this part next html formatting to make things pretty okay thanks bye
            String mainContent = "Description: " + this.repice.getDescription() + "\n";
            //like here

            postText.setText(this.repice.getDescription() + "\n" + this.repice.getIngredients() + "\n" + this.repice.getSteps());

        }
        else if (post.isImageVideo()) {
            System.out.println("cry");
        }

        scrollPane = new JScrollPane(postText);


        JTextArea comments = new JTextArea();
        scrollPane.add(comments);
        scrollPane.setPreferredSize(new Dimension(postWidth, postHeight));
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        comments.setBackground(Color.PINK);
        comments.setOpaque(true);

        // right
        ArrayList<JButton> rightButtons = new ArrayList<>();
        if (liked) {
            likeButton.setText("unlike");
        }
        rightButtons.add(likeButton);
        if (post instanceof Recipe) {
            rightButtons.add(analyzeButton);
        }
        rightButtons.add(saveButton);
        rightButtons.add(shareButton);
        rightButtons.add(commentButton);
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
        rightPanel.add(Box.createRigidArea(new Dimension(0, 666)));

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
        if (e.getSource() == analyzeButton) {
            System.out.println("hmmmm \uD83E\uDD13");
            SpoonacularAPI spon = new SpoonacularAPI();
            this.repice.setRestrictionsMap(spon.callAPI(repice));
            HashMap<String, String> result = this.repice.getRestrictionsMap();
            System.out.println(result);
            String resultDisplay = "";
            String numers = "";
            for (String key : result.keySet()) {
                String loopRes = String.valueOf(result.get(key));
                if (loopRes.equals("true") || loopRes.equals("false")) {
                    if (loopRes.equals("true")) {
                        resultDisplay += "is " + key + " ✅\uD83D\uDE04💪 \n";
                    }
                    else {
                        resultDisplay += "is not " + key + "💔😿😔 \n";
                    }
                }
                else {
                    if (Double.valueOf(loopRes) > 30) {
                        numers += key + ": " + loopRes + " \n";
                    }
                    else {
                        numers += key + ": " + loopRes + " \uD83D\uDE25  \n";
                    }
                }
            }
            JTextArea messageArea = new JTextArea("according to le analysis: \n" + resultDisplay + numers);
            messageArea.setFont(whimsy);
            messageArea.setEditable(false);
            messageArea.setOpaque(false);
            JOptionPane.showMessageDialog(null, messageArea, "nerd", JOptionPane.INFORMATION_MESSAGE);

        }
        if (e.getSource() == saveButton) {
            JOptionPane.showMessageDialog(null, "havent added that yet cry abt it", "nerd", JOptionPane.INFORMATION_MESSAGE);

            System.out.println("popup add to list");
        }
        if (e.getSource() == shareButton) {
            System.out.println("share slop");
            JOptionPane.showMessageDialog(null, "here is the id of ths post share that or something \n" + post.getID(), "nerd", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == commentButton && !xPresent) {
            JTextArea commentsArea = new JTextArea(2, 20);
            scrollPane.setSize(new Dimension(1400, 800)); //YOPPP WORKS
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.add(commentsArea);

            commentButton.setOpaque(true);
            commentButton.setText("");

            JPanel subRight = new JPanel(new FlowLayout(FlowLayout.LEFT));
            RoundedButton xButton = new RoundedButton("x");
            RoundedButton postButton = new RoundedButton("Post!");
            subRight.add(postButton);
            subRight.add(xButton);
            rightPanel.add(subRight);

            xPresent = true;
            xButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            centerPanel.remove(commentsArea);
                            rightPanel.remove(subRight);
                            commentButton.setText("comment");
                            commentButton.setOpaque(false);

//                          centerPanel.revalidate();
//                          centerPanel.repaint();
//                          rightPanel.revalidate();
//                          rightPanel.repaint();
                            xPresent = false;
                        }
                    });
            postButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String mesage = commentsArea.getText();
                            //TODO: send comment to whoever deals with this idk
                            centerPanel.remove(commentsArea);
                            rightPanel.remove(subRight);
                            commentButton.setText("comment");
                            commentButton.setOpaque(false);
                            xPresent = false;
                            HashMap<Integer, Comment> map = post.getComments();
                            //TODO: account user implementation
                            Comment comment = new Comment(new Account("hi", "bye"), mesage, LocalDateTime.now(), 0);
                            map.put(comment.getID(), comment);
                            post.setComments(map);
                        }
                    }
            );
        }

    }

}
