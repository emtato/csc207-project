package view;

import data_access.FilePostCommentLikesDataAccessObject;
import data_access.PostCommentsLikesDataAccessObject;
import entity.Account;
import entity.Comment;
import interface_adapter.ViewManagerModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import interface_adapter.analyze_recipe.AnalyzeRecipeController;
import interface_adapter.analyze_recipe.AnalyzeRecipePresenter;
import interface_adapter.analyze_recipe.AnalyzeRecipeViewModel;
import interface_adapter.fetch_post.FetchPostController;
import interface_adapter.get_comments.GetCommentsController;
import interface_adapter.get_comments.GetCommentsPresenter;
import interface_adapter.get_comments.GetCommentsViewModel;
import use_case.analyze_recipe.AnalyzeRecipeInteractor;
import use_case.analyze_recipe.SpoonacularAccessInterface;
import use_case.comment.CommentPostInputData;
import use_case.comment.CommentPostInteractor;
import use_case.fetch_post.FetchPostInteractor;
import use_case.get_comments.GetCommentsInteractor;
import use_case.get_comments.GetCommentsOutputBoundary;
import use_case.like_post.LikePostInputData;
import use_case.like_post.LikePostInteractor;
import view.ui_components.JFrame;
import view.ui_components.MenuBarPanel;
import view.ui_components.RoundedButton;
import app.Session;
/* Description:
 * ^ • ω • ^
 */

public class PostView extends JPanel {

    private final String viewName = "post view";
    private Account currentLoggedInUser;
    //private final PostViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    private Post post;
    private Recipe repice;

    // fonts & styles
    private final Font fontTitle = new Font("Roboto", Font.BOLD, 20);
    private final Font subtite = new Font("Roboto", Font.BOLD, 16);
    private final Font subtite2 = new Font("Roboto", Font.BOLD, 14);
    private final Font commenter = new Font("Roboto", Font.PLAIN, 14);
    private final Font text = new Font("Roboto", Font.PLAIN, 15);
    private final Font whimsy = new Font("papyrus", Font.BOLD, 20);
    // middle
    private JPanel postText;
    private JPanel centerPanel;
    private JScrollPane scrollPane;
    private JPanel wrapper;

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

    private final PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject;
    private LikePostInteractor likePostInteractor;
    private CommentPostInteractor commentPostInteractor;
    private GetCommentsController getCommentsController;
    private final GetCommentsViewModel getCommentsViewModel = new GetCommentsViewModel();
    private AnalyzeRecipeController analyzeRecipeController;
    private AnalyzeRecipeViewModel analyzeRecipeViewModel = new AnalyzeRecipeViewModel();
    private SpoonacularAPI spoonacularAPI;

    private final JPanel mainPanel;
    //TODO: keep track of which posts liked to update this according to user and postID


    public PostView(ViewManagerModel viewManagerModel, Post post, PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject) {

        //this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.post = post;
        this.postCommentsLikesDataAccessObject = postCommentsLikesDataAccessObject;
        this.repice = null;
        this.spoonacularAPI = new SpoonacularAPI();
        this.likePostInteractor = new LikePostInteractor(postCommentsLikesDataAccessObject);
        this.commentPostInteractor = new CommentPostInteractor(postCommentsLikesDataAccessObject);
        this.getCommentsController = new GetCommentsController(
                new GetCommentsInteractor(postCommentsLikesDataAccessObject,
                        new GetCommentsPresenter(getCommentsViewModel))
        );
        this.analyzeRecipeController = new AnalyzeRecipeController(new AnalyzeRecipeInteractor((SpoonacularAccessInterface) spoonacularAPI, new AnalyzeRecipePresenter(analyzeRecipeViewModel)));
        mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel splitTop = new JPanel();
        splitTop.setLayout(new BoxLayout(splitTop, BoxLayout.Y_AXIS));
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        centerPanel = new JPanel();

        // top
        title = new JLabel(post.getTitle()); //recipe/post title "HELLLOOOO aaiaiaiee" you will not be forgotten
        title.setFont(fontTitle);

        splitTop.add(title);
        subtitle = new JLabel(post.getUser().getUsername() + " | " + post.getDateTime().format(formatter) + " | " + post.getLikes() + " likes"); // post author and date
        subtitle.setFont(subtite);
        subtitle.setForeground(Color.GRAY);
        JLabel tags = new JLabel("tags: " + post.getTags());
        tags.setFont(text);
        tags.setForeground(Color.LIGHT_GRAY);


        splitTop.add(subtitle);
        splitTop.add(tags);

        topPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        topPanel.add(splitTop);

        // middle
        displayPost(post);

        // bottom
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        menuBar.setPreferredSize(new Dimension(1200, 100));

        // right
        ArrayList<JButton> rightButtons = new ArrayList<>();
//        if (liked) {
//            likeButton.setText("unlike");
//        }
//        rightButtons.add(likeButton);
//        if (post instanceof Recipe) {
//            rightButtons.add(analyzeButton);
//        }
//        rightButtons.add(saveButton);
//        rightButtons.add(shareButton);
//        rightButtons.add(commentButton);
//        for (JButton button : rightButtons) {
//            button.setFont(text);
//            button.setAlignmentX(Component.CENTER_ALIGNMENT);
//            button.addActionListener(e -> {
//                try {
//                    actionPerformed(e);
//                }
//                catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//                catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                    System.out.println("NOOOOOO D:");
//                }
//            });
//            rightPanel.add(button);
//            rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//        }
//        rightPanel.add(Box.createRigidArea(new Dimension(0, 540)));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    public String wrapText(String text, int maxLength) {
        String res = "";
        String curWord = "";
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
            counter++;
            curWord += text.charAt(i);
            if (text.charAt(i) == ' ') {
                res += curWord;
                curWord = "";
            }
            if (counter % maxLength == 0) {
                res += "<br>";
            }
            if (curWord.length() > 50) {
                res += curWord;
                curWord = "";

            }
        }
        res += curWord;
        return res;
    }

    /**
     * Display a new post in the current view to avoid having to recreate entire view every switch.
     *
     * @param newPost new post object
     */
    public void displayPost(Post newPost) {
        currentLoggedInUser = Session.getCurrentAccount();

        centerPanel.removeAll();
        liked = false;
        likeButton.setText("like");
        xPresent = false;
        //TODO: UPDATE THIS TO RETRIEVE IF LIKED BY CURRENT USER
        this.post = newPost;

        postText = new JPanel();
        postText.setLayout(new BoxLayout(postText, BoxLayout.Y_AXIS));

        wrapper = new JPanel(new BorderLayout());
        wrapper.add(postText, BorderLayout.CENTER);
        scrollPane = new JScrollPane(wrapper);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1300, 700));
        scrollPane.setBorder(null);


        if (post.isImageVideo()) {
            System.out.println("isimage");
            try {
                ArrayList<String> imageURLS = post.getImageURLs();
                JPanel imagePanel = new JPanel();
                imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
                int imagesRn = 0;
                for (String imageURL : imageURLS) {
                    URL url = new URL(imageURL);
                    ImageIcon imageIcon = new ImageIcon(url);

                    Image img = imageIcon.getImage().getScaledInstance(-1, 450, Image.SCALE_SMOOTH);

                    int imgW = imageIcon.getIconWidth();
                    int imgH = imageIcon.getIconHeight();

                    int finalW = imgW;
                    int finalH = imgH;
                    float ratioW;
                    if (imgW > 367) {
                        finalW = 367;
                        ratioW = imgW / 367f;
                        finalH = (int) (imgH / ratioW);
                    }

                    img = img.getScaledInstance(finalW, finalH, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(img);

                    JLabel image = new JLabel(scaledIcon);
                    image.setAlignmentX(Component.CENTER_ALIGNMENT);
                    imagePanel.add(image);
                    imagesRn++;
                    if (imagesRn == 3) {
                        break;
                    }
                }
                wrapper.add(imagePanel, BorderLayout.NORTH);

            }
            catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel commentsHeader = new JLabel("Comments");
        commentsHeader.setFont(subtite);
        commentsHeader.setForeground(Color.BLACK);

        JPanel commentsPanel = new JPanel();

        commentsPanel.add(commentsHeader);

        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));

        getCommentsController.getComments(post.getID());
        ArrayList<Comment> comments = new ArrayList<>(getCommentsViewModel.getComments());
        for (Comment comment : comments) {
            JLabel commentUser = new JLabel(comment.getAccount().getUsername() + " on " + comment.getDate().format(formatter));
            commentUser.setFont(subtite2);

            JLabel commentContent = new JLabel("<html>" + wrapText(comment.getComment(), 150) + "</html>");
            commentContent.setFont(commenter);
            commentContent.setForeground(Color.GRAY);

            commentsPanel.add(commentUser);
            commentsPanel.add(Box.createRigidArea(new Dimension(10, 5)));
            commentsPanel.add(commentContent);
            commentsPanel.add(Box.createRigidArea(new Dimension(10, 12)));
        }

        postText.setLayout(new BoxLayout(postText, BoxLayout.Y_AXIS));
        postText.add(Box.createRigidArea(new Dimension(10, 20)));

        //redraw right buttons
        rightPanel.removeAll();
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
            for (ActionListener old : button.getActionListeners()) {
                button.removeActionListener(old);
            }
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
        rightPanel.add(Box.createRigidArea(new Dimension(0, 500)));


        if (newPost instanceof Recipe) {
            this.repice = (Recipe) newPost;

            String ingredientsText = "";
            ArrayList<String> ingredients = this.repice.getIngredients();
            for (String ingredient : ingredients) {
                ingredientsText += ingredient.replace("\n", "<br>") + "<br>";
            }

            JLabel postDescriptionHeader = new JLabel("Description");
            JLabel postIngredientsHeader = new JLabel("Ingredients");
            JLabel postStepsHeader = new JLabel("Steps");
            postDescriptionHeader.setFont(subtite);
            postDescriptionHeader.setForeground(Color.BLACK);
            postIngredientsHeader.setFont(subtite);
            postIngredientsHeader.setForeground(Color.BLACK);
            postStepsHeader.setFont(subtite);
            postStepsHeader.setForeground(Color.BLACK);

            JLabel postDescription = new JLabel("<html>" + wrapText(this.post.getDescription(), 125) + "</html>");
            postDescription.setFont(text);
            postDescription.setForeground(Color.DARK_GRAY);
            JLabel postIngredients = new JLabel("<html>" + wrapText(ingredientsText, 125) + "</html>");
            postIngredients.setFont(text);
            postIngredients.setForeground(Color.DARK_GRAY);
            JLabel postSteps = new JLabel("<html>" + wrapText(this.repice.getSteps(), 125).replace("\n", "<br>") + "</html>");
            postSteps.setFont(text);
            postSteps.setForeground(Color.DARK_GRAY);

            postText.add(postDescriptionHeader);
            postText.add(postDescription);
            postText.add(Box.createRigidArea(new Dimension(10, 12)));
            postText.add(postIngredientsHeader);
            postText.add(postIngredients);
            postText.add(Box.createRigidArea(new Dimension(10, 12)));
            postText.add(postStepsHeader);
            postText.add(postSteps);
            postText.add(Box.createRigidArea(new Dimension(10, 20)));
        }

        else { //general post display

            JLabel postDescriptionHeader = new JLabel("Description");
            postDescriptionHeader.setFont(subtite);
            postDescriptionHeader.setForeground(Color.BLACK);

            JLabel postDescription = new JLabel("<html>" + wrapText(this.post.getDescription(), 125) + "</html>");
            postDescription.setFont(text);
            postDescription.setForeground(Color.DARK_GRAY);


            postText.add(postDescriptionHeader);
            postText.add(postDescription);
            postText.add(Box.createRigidArea(new Dimension(10, 20)));
            repice = null;
        }
        postText.add(commentsPanel);


        mainPanel.add(centerPanel, BorderLayout.CENTER);
        this.add(mainPanel);


        title.setText(newPost.getTitle());
        subtitle.setText(post.getUser().getUsername() + " | " + post.getDateTime().format(formatter) + " | " + post.getLikes() + " likes");

        revalidate();
        repaint();

    }

    /**
     * function to executeViewProfile button presses
     *
     * @param e action event
     * @throws IOException          idk
     * @throws InterruptedException idk
     */
    public void actionPerformed(ActionEvent e) throws IOException, InterruptedException {
        if (e.getSource() == likeButton) {
            boolean isLiking = !liked;
            LikePostInputData inputData = new LikePostInputData(post.getID(), isLiking);
            likePostInteractor.execute(inputData);

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
        if (e.getSource() == analyzeButton) {
            System.out.println("hmmmm \uD83E\uDD13");
            analyzeRecipeController.analyze(repice);

            this.repice.setRestrictionsMap((HashMap<String, String>) analyzeRecipeViewModel.getResult());
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
            JPanel commentsPanel = new JPanel();
            commentsPanel.setLayout(new FlowLayout());
            JTextArea commentsArea = new JTextArea(2, 100);
            commentsArea.setLineWrap(true);
            commentsArea.setWrapStyleWord(true);
            commentsPanel.add(commentsArea, BorderLayout.CENTER);

            scrollPane.setSize(new Dimension(1200, 560)); //YOPPP WORKS
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.add(commentsPanel);

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

                    xPresent = false;
                }
            });
            postButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String mesage = commentsArea.getText();
                    centerPanel.remove(commentsArea);
                    rightPanel.remove(subRight);
                    commentButton.setText("comment");
                    commentButton.setOpaque(false);
                    xPresent = false;

//                    ArrayList<Comment> lst = post.getComments();
//                    if (lst == null) {
//                        lst = new ArrayList<Comment>();
//                    }
//                    Comment comment = new Comment(currentLoggedInUser, mesage, LocalDateTime.now(), 0);
//                    lst.add(comment);

                    CommentPostInputData inputData = new CommentPostInputData(post.getID(), currentLoggedInUser, mesage, LocalDateTime.now());
                    commentPostInteractor.execute(inputData);
                    displayPost(post);
                }
            });
        }
    }

    public String getViewName() {
        return viewName;
    }


    public static void main(String[] args) {
        view.ui_components.JFrame frame = new view.ui_components.JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String steps = "1. smash 4 glorbles of bean paste into a sock, microwave till it sings\n" + "2.sprinkle in 2 blinks of mystery flakes, scream gently\n" + "3.serve upside-down on a warm tile";
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
//
//        frame.add(new PostView(new PostViewModel(), new ViewManagerModel(), trialpost));
        Post postex2 = new Post(new Account("jinufan333", "WOOF ARF BARK BARK"), 2384723473L, "titler?", "IS THAT MY HANDSOME, ELEGANT, INTELLIGENT, CHARMING, KIND, THOUGHTFUL, STRONG, COURAGEOUS, CREATIVE, BRILLIANT, GENTLE, HUMBLE, GENEROUS, PASSIONATE, WISE, FUNNY, LOYAL, DEPENDABLE, GRACEFUL, RADIANT, CALM, CONFIDENT, WARM, COMPASSIONATE, WITTY, ADVENTUROUS, RESPECTFUL, SINCERE, MAGNETIC, BOLD, ARTICULATE, EMPATHETIC, INSPIRING, HONEST, PATIENT, POWERFUL, ATTENTIVE, UPLIFTING, CLASSY, FRIENDLY, RELIABLE, AMBITIOUS, INTUITIVE, TALENTED, SUPPORTIVE, GROUNDED, DETERMINED, CHARISMATIC, EXTRAORDINARY, TRUSTWORTHY, NOBLE, DIGNIFIED, PERCEPTIVE, INNOVATIVE, REFINED, CONSIDERATE, BALANCED, OPEN-MINDED, COMPOSED, IMAGINATIVE, MINDFUL, OPTIMISTIC, VIRTUOUS, NOBLE-HEARTED, WELL-SPOKEN, QUICK-WITTED, DEEP, PHILOSOPHICAL, FEARLESS, AFFECTIONATE, EXPRESSIVE, EMOTIONALLY INTELLIGENT, RESOURCEFUL, DELIGHTFUL, FASCINATING, SHARP, SELFLESS, DRIVEN, ASSERTIVE, AUTHENTIC, VIBRANT, PLAYFUL, OBSERVANT, SKILLFUL, GENEROUS-SPIRITED, PRACTICAL, COMFORTING, BRAVE, WISE-HEARTED, ENTHUSIASTIC, DEPENDABLE, TACTFUL, ENDURING, DISCREET, WELL-MANNERED, COMPOSED, MATURE, TASTEFUL, JOYFUL, UNDERSTANDING, GENUINE, BRILLIANT-MINDED, ENCOURAGING, WELL-ROUNDED, MAGNETIC, DYNAMIC, RADIANT, RADIANT-SPIRITED, SOULFUL, RADIANT-HEARTED, INSIGHTFUL, CREATIVE-SOULED, JUSTICE-MINDED, RELIABLE-HEARTED, TENDER, UPLIFTING-MINDED, PERSEVERING, DEVOTED, ANGELIC, DOWN-TO-EARTH, GOLDEN-HEARTED, GENTLE-SPIRITED, CLEVER, COURAGEOUS-HEARTED, COURTEOUS, HARMONIOUS, LOYAL-MINDED, BEAUTIFUL-SOULED, EASYGOING, SINCERE-HEARTED, RESPECTFUL-MINDED, COMFORTING-VOICED, CONFIDENT-MINDED, EMOTIONALLY STRONG, RESPECTFUL-SOULED, IMAGINATIVE-HEARTED, PROTECTIVE, NOBLE-MINDED, CONFIDENT-SOULED, WISE-EYED, LOVING, SERENE, MAGNETIC-SOULED, EXPRESSIVE-EYED, BRILLIANT-HEARTED, INSPIRING-MINDED, AND ABSOLUTELY UNFORGETTABLE JINU SPOTTED?!?? \n haha get it jinu is sustenance");

        PostCommentsLikesDataAccessObject dao = FilePostCommentLikesDataAccessObject.getInstance();
        frame.add(new PostView(new ViewManagerModel(), trialpost, dao));

        frame.setPreferredSize(new Dimension(1728, 1080));
        frame.pack();
        frame.setVisible(true);
    }


}
