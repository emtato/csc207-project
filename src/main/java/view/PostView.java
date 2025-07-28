package view;

import entity.Account;
import interface_adapter.ViewManagerModel;
import interface_adapter.post_view.PostViewModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
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
    private final PostViewModel viewModel;
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

    private final JLabel title;
    private final JLabel subtitle;

    private boolean liked;

    //TODO: keep track of which posts liked to update this according to user and postID

    public PostView(PostViewModel viewModel, ViewManagerModel viewManagerModel, Post post) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.post = post;
        this.repice = null;

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JPanel centerPanel = new JPanel();

        // top
        title = new JLabel("HELLLOOOO aaiaiaiee"); //recipe/post title
        title.setFont(fontTitle);
        title.setText(post.getTitle());

        topPanel.add(title);
        subtitle = new JLabel(post.getUser().getUsername() + " | " + post.getDateTime() + " | " + post.getLikes() + " likes"); // post author and date
        subtitle.setFont(subtite);
        subtitle.setForeground(Color.GRAY);
        JLabel tags = new JLabel("tags");
        tags.setFont(text);
        tags.setForeground(Color.LIGHT_GRAY);
        tags.setText("tags: " + post.getTags());

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

        JScrollPane scrollPane = new JScrollPane(postText);


        JTextArea comments = new JTextArea();
        scrollPane.add(comments);
        scrollPane.setPreferredSize(new
                Dimension(1300, 800));
        centerPanel.add(scrollPane);
        comments.setBackground(Color.PINK);
        comments.setOpaque(true);

        // bottom
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);

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
        for (JButton button : rightButtons) {
            button.setFont(text);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBackground(Color.PINK);
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
        }

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
        this.post = newPost;
        if (newPost instanceof Recipe) {
            this.repice = (Recipe) newPost;
        }
        else {
            this.repice = null;
        }

        title.setText(newPost.getTitle());
        subtitle.setText(newPost.getUser().getUsername() + " | " + newPost.getLikes() + " likes");

        if (newPost instanceof Recipe) {
            postText.setText(repice.getDescription() + "\n" + repice.getIngredients() + "\n" + repice.getSteps());
        }
        else {
            postText.setText("No recipe to display.");
        }

        // refresh UI as needed
        revalidate();
        repaint();
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
            HashMap<String, String> result = spon.callAPI(repice);
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

        frame.add(new PostView(new PostViewModel(), new ViewManagerModel(), trialpost));
        frame.setPreferredSize(new Dimension(1920, 1080));
        frame.pack();
        frame.setVisible(true);
    }
}
