package view;

import entity.Account;
import entity.Comment;
import interface_adapter.ViewManagerModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.JLabel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import data_access.spoonacular.SpoonacularAPI;
import entity.Post;
import entity.Recipe;
/* Description:
 * ^ • ω • ^
 */

public class PostView extends JPanel {

    private final String viewName = "post view";
    //private final PostViewModel viewModel;
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
    private JPanel centerPanel;
    private JScrollPane scrollPane;
    // top
    private final JLabel title;
    private final JLabel subtitle;
    // right
    private JPanel rightPanel;
    private boolean liked;
    private boolean xPresent;
    private RoundedButton likeButton = new RoundedButton("Like");
    private final RoundedButton analyzeButton = new RoundedButton("Analyze");
    private final RoundedButton saveButton = new RoundedButton("Add to list");
    private final RoundedButton shareButton = new RoundedButton("Share");
    private final RoundedButton commentButton = new RoundedButton("coment");


    private final JPanel mainPanel;
    //TODO: keep track of which posts liked to update this according to user and postID

    public PostView(ViewManagerModel viewManagerModel, Post post) {
        //this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.post = post;
        this.repice = null;

        mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        centerPanel = new JPanel();

        // top
        title = new JLabel(post.getTitle()); //recipe/post title "HELLLOOOO aaiaiaiee" you will not be forgotten
        title.setFont(fontTitle);

        topPanel.add(title); //TODO: fix datetime thing
        subtitle = new JLabel(post.getUser().getUsername() + " | " + post.getDateTime() + " | " + post.getLikes() + " likes"); // post author and date
        subtitle.setFont(subtite);
        subtitle.setForeground(Color.GRAY);
        JLabel tags = new JLabel("tags: " + post.getTags());
        tags.setFont(text);
        tags.setForeground(Color.LIGHT_GRAY);


        topPanel.add(subtitle);
        topPanel.add(tags);

        // middle
        scrollPane = new JScrollPane(postText);
        displayPost(post);

        // PROBABLY CAN BE REMOVED, BUT I WANNA MAKE SURE DISPLAYPOST WORKS 100%
        /*
        // if has media:
        int maxBoxHeight = 739123617;
        if (post.isImageVideo()) {
            try {
                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                ArrayList<String> imageURLS = post.getImageURLs();
                for (String imageURL : imageURLS) {
                    URL url = new URL(imageURL);
                    ImageIcon imageIcon = new ImageIcon(url);
                    Image img = imageIcon.getImage().getScaledInstance(-1, 450, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(img);
                    JLabel image = new JLabel(scaledIcon);
                    image.setAlignmentX(Component.CENTER_ALIGNMENT);
                    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
                    imagePanel.add(image);
                }
                centerPanel.add(imagePanel);

                maxBoxHeight = 350;
            }
            catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        postText.setEditable(false);
        if (post instanceof Recipe || this.repice != null) {
            this.repice = (Recipe) post;
            //TODO: hiii em its me work on this part next html formatting to make things pretty okay thanks bye + add comments
            String ingredientsText = "";
            ArrayList<String> ingredients = this.repice.getIngredients();
            for (String ingredient : ingredients) {
                ingredientsText += ingredient + "<br>";
            }
            System.out.println(ingredientsText);
            String mainContent = """
                    <html>
                      <body style='font-family: comic sans, sans-serif'>
                        <h1 style='font-size: 18pt; color: #333'> <strong>Description</strong> </h1>
                        <p style='font-size: 14pt;'> """ + this.repice.getDescription() + """
                    </p>

                    <h2 style='font-size: 16pt; color: #555;'>Ingredients</h2>
                    <ul>""" + ingredientsText + """
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
        else {
            postText.setText(post.getDescription());
        }

        JTextArea comments = new JTextArea();
        scrollPane.add(comments);
        scrollPane.setPreferredSize(new Dimension(1400, Math.min(850, maxBoxHeight)));
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        comments.setBackground(Color.PINK);
        comments.setOpaque(true);
*/

        // bottom
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        menuBar.setPreferredSize(new Dimension(1200, 100));

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
        rightPanel.add(Box.createRigidArea(new Dimension(0, 680)));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    /**
     * Display a new post in the current view to avoid having to recreate entire view every switch.
     *
     * @param newPost new post object
     */
    public void displayPost(Post newPost) {
        centerPanel.removeAll();
        this.post = newPost;

        int maxBoxHeight = 739123617;
        if (post.isImageVideo()) {
            try {
                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                ArrayList<String> imageURLS = post.getImageURLs();
                for (String imageURL : imageURLS) {
                    URL url = new URL(imageURL);
                    ImageIcon imageIcon = new ImageIcon(url);
                    Image img = imageIcon.getImage().getScaledInstance(-1, 450, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(img);
                    JLabel image = new JLabel(scaledIcon);
                    image.setAlignmentX(Component.CENTER_ALIGNMENT);
                    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
                    imagePanel.add(image);
                }
                centerPanel.add(imagePanel);

                maxBoxHeight = 350;
            }
            catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        postText.setEditable(false);

        JTextArea comments = new JTextArea();
        scrollPane.add(comments);
        scrollPane.setPreferredSize(new Dimension(1400, Math.min(850, maxBoxHeight)));
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        comments.setBackground(Color.PINK);
        comments.setOpaque(true);

        if (newPost instanceof Recipe) {

            this.repice = (Recipe) newPost;

            String ingredientsText = "";
            ArrayList<String> ingredients = this.repice.getIngredients();
            for (String ingredient : ingredients) {
                ingredientsText += ingredient + "<br>";
            }
            System.out.println(ingredientsText);
            String mainContent = """
                    <html>
                      <body style='font-family: comic sans, sans-serif'>
                        <h1 style='font-size: 18pt; color: #333'> <strong>Description</strong> </h1>
                        <p style='font-size: 14pt;'> """ + this.repice.getDescription() + """ 
                    </p>
                    
                    <h2 style='font-size: 16pt; color: #555;'>Ingredients</h2>
                    <ul>""" + ingredientsText + """
                    </ul>
                    <h2 style='font-size: 16pt; color: #555;'>Steps</h2>
                    <p>""" + this.repice.getSteps().replace("\n", "<br>") + """
                          </p>
                          </body>
                        </html>
                    """;
            postText.setContentType("text/html");
            postText.setText(mainContent);

            // scrollPane.add(comments);

            mainPanel.add(centerPanel, BorderLayout.CENTER);
            this.add(mainPanel);
        }

        else {
            repice = null;
            postText.setText(post.getDescription());
        }

        title.setText(newPost.getTitle());
        subtitle.setText(post.getUser().getUsername() + " | " + post.getDateTime() + " | " + post.getLikes() + " likes");

        //TODO: update comments for new post
        revalidate();
        repaint();

    }

    /**
     * function to execute button presses
     *
     * @param e action event
     * @throws IOException          idk
     * @throws InterruptedException idk
     */
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
            subtitle.setText(post.getUser().getUsername() + " | " + post.getDateTime() + " | " + post.getLikes() + " likes");

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
            xButton.addActionListener(new ActionListener() {
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
            postButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String mesage = commentsArea.getText();
                    //TODO: send comment to whoever deals with this idk
                    centerPanel.remove(commentsArea);
                    rightPanel.remove(subRight);
                    commentButton.setText("comment");
                    commentButton.setOpaque(false);
                    xPresent = false;
                    HashMap<Integer, Comment> map = post.getComments();
                    if (map == null) {
                        map = new HashMap<Integer, Comment>();
                    }
                    //TODO: account user implementation, and send comment to scroll window
                    Comment comment = new Comment(new Account("hi", "bye"), mesage, LocalDateTime.now(), 0);
                    map.put(comment.getID(), comment);
                    post.setComments(map);
                }
            });
        }

    }

    public String getViewName() {
        return viewName;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String steps = "1. smash 4 glorbles of bean paste into a sock, microwave till it sings\n" + "2.sprinkle in 2 blinks of mystery flakes, scream gently\n" + "3.serve upside-down on a warm tile \n \n \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n hi\nhih\nhi\njo";
        Recipe trialpost = new Recipe(new Account("meow", "woof"), 483958292, "repice for glunking", "i made it for the tiger but the bird keeps taking it", new ArrayList<>(Arrays.asList("glorbles", "beans", "tile", "dandelion")), steps, new ArrayList<>(Arrays.asList("yeah")));
        trialpost.setTags(new ArrayList<>(Arrays.asList("glorpy", "beany")));
        trialpost.setImageURLs(new ArrayList<>(Arrays.asList("https://i.imgur.com/eA9NeJ1.jpeg", "https://i.imgur.com/wzX83Zc.jpeg")));
//        Post trialpost2 = new Post(new Account("chef", "secret123"), 123456789);
//        trialpost2.setTitle(" salad");
//        trialpost2.setRecipeObj(new Recipe(
//                new Account("plantlover", "leaf123"),
//                "Chickpea Salad Bowl",
//                "A refreshing, hearty bowl of protein-packed chickpeas with crisp veggies and tahini dressing.",
//                new ArrayList<>(Arrays.asList("healthy", "salad", "plantbased", "quickmeal")),
//                new ArrayList<>(Arrays.asList("chickpeas", "cucumber", "cherry tomatoes", "red onion", "parsley", "lemon juice", "tahini", "olive oil", "salt", "pepper")),
//                "1. Rinse and drain the chickpeas.\n" +
//                        "2. Dice the cucumber, cherry tomatoes, and red onion.\n" +
//                        "3. Mix all ingredients in a bowl.\n" +
//                        "4. Whisk tahini, lemon juice, olive oil, salt, and pepper for dressing.\n" +
//                        "5. Toss the salad with dressing and garnish with fresh parsley.",
//                new ArrayList<>(Arrays.asList("mediterranean", "middle eastern"))
//        ));
//        trialpost2.setRecipe(true);

//        frame.add(new PostView(new PostViewModel(), new ViewManagerModel(), trialpost));
        frame.add(new PostView(new ViewManagerModel(), trialpost));

        frame.setPreferredSize(new Dimension(1728, 1080));
        frame.pack();
        frame.setVisible(true);
    }
}
