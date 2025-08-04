package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import javax.swing.*;


import data_access.PostCommentsLikesDataAccessObject;
import entity.Account;
import entity.Post;
import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.fetch_post.FetchPostController;
import interface_adapter.like_post.LikePostController;
import use_case.fetch_post.FetchPostInteractor;
import use_case.like_post.LikePostInteractor;
import view.ui_components.MenuBarPanel;
import view.ui_components.PostPanel;

/**
 * The View for the Home Page.
 */
public class HomePageView extends JPanel {

    private final String viewName = "homepage view";
    private final ViewManagerModel viewManagerModel;
    private JPanel feedPanel;
    private String steps = "1. smash 4 glorbles of bean paste into a sock, microwave till it sings\n" + "2.sprinkle in 2 blinks of mystery flakes, scream gently\n" + "3.serve upside-down on a warm tile  hi\nhih\nhi\njo";
    private Recipe trialpost = new Recipe(new Account("meow", "woof"), 483958292, "repice for glunking", "i made it for the tiger but the bird keeps taking it", new ArrayList<>(Arrays.asList("glorbles", "beans", "tile", "dandelion")), steps, new ArrayList<>(Arrays.asList("yeah")));
    private Post postex2 = new Post(new Account("jinufan333", "WOOF ARF BARK BARK"), 2384723473L, "titler?", "IS THAT MY HANDSOME, ELEGANT, INTELLIGENT, CHARMING, KIND, THOUGHTFUL, STRONG, COURAGEOUS, CREATIVE, BRILLIANT, GENTLE, HUMBLE, GENEROUS, PASSIONATE, WISE, FUNNY, LOYAL, DEPENDABLE, GRACEFUL, RADIANT, CALM, CONFIDENT, WARM, COMPASSIONATE, WITTY, ADVENTUROUS, RESPECTFUL, SINCERE, MAGNETIC, BOLD, ARTICULATE, EMPATHETIC, INSPIRING, HONEST, PATIENT, POWERFUL, ATTENTIVE, UPLIFTING, CLASSY, FRIENDLY, RELIABLE, AMBITIOUS, INTUITIVE, TALENTED, SUPPORTIVE, GROUNDED, DETERMINED, CHARISMATIC, EXTRAORDINARY, TRUSTWORTHY, NOBLE, DIGNIFIED, PERCEPTIVE, INNOVATIVE, REFINED, CONSIDERATE, BALANCED, OPEN-MINDED, COMPOSED, IMAGINATIVE, MINDFUL, OPTIMISTIC, VIRTUOUS, NOBLE-HEARTED, WELL-SPOKEN, QUICK-WITTED, DEEP, PHILOSOPHICAL, FEARLESS, AFFECTIONATE, EXPRESSIVE, EMOTIONALLY INTELLIGENT, RESOURCEFUL, DELIGHTFUL, FASCINATING, SHARP, SELFLESS, DRIVEN, ASSERTIVE, AUTHENTIC, VIBRANT, PLAYFUL, OBSERVANT, SKILLFUL, GENEROUS-SPIRITED, PRACTICAL, COMFORTING, BRAVE, WISE-HEARTED, ENTHUSIASTIC, DEPENDABLE, TACTFUL, ENDURING, DISCREET, WELL-MANNERED, COMPOSED, MATURE, TASTEFUL, JOYFUL, UNDERSTANDING, GENUINE, BRILLIANT-MINDED, ENCOURAGING, WELL-ROUNDED, MAGNETIC, DYNAMIC, RADIANT, RADIANT-SPIRITED, SOULFUL, RADIANT-HEARTED, INSIGHTFUL, CREATIVE-SOULED, JUSTICE-MINDED, RELIABLE-HEARTED, TENDER, UPLIFTING-MINDED, PERSEVERING, DEVOTED, ANGELIC, DOWN-TO-EARTH, GOLDEN-HEARTED, GENTLE-SPIRITED, CLEVER, COURAGEOUS-HEARTED, COURTEOUS, HARMONIOUS, LOYAL-MINDED, BEAUTIFUL-SOULED, EASYGOING, SINCERE-HEARTED, RESPECTFUL-MINDED, COMFORTING-VOICED, CONFIDENT-MINDED, EMOTIONALLY STRONG, RESPECTFUL-SOULED, IMAGINATIVE-HEARTED, PROTECTIVE, NOBLE-MINDED, CONFIDENT-SOULED, WISE-EYED, LOVING, SERENE, MAGNETIC-SOULED, EXPRESSIVE-EYED, BRILLIANT-HEARTED, INSPIRING-MINDED, AND ABSOLUTELY UNFORGETTABLE JINU SPOTTED?!?? \n haha get it jinu is sustenance");
    private PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject;
    private FetchPostController fetchPostController;

    public HomePageView(ViewManagerModel viewManagerModel, PostCommentsLikesDataAccessObject postCommentsLikesDataAccessObject) {
        this.viewManagerModel = viewManagerModel;
        this.postCommentsLikesDataAccessObject = postCommentsLikesDataAccessObject;
        this.fetchPostController = new FetchPostController(new FetchPostInteractor(postCommentsLikesDataAccessObject));

        JPanel mainPanel = new JPanel(new BorderLayout());

        // top tabs to switch between feeds
        Dimension buttonSize = new Dimension(450, 40);

        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JButton forYouButton = new JButton("For You");
        forYouButton.setPreferredSize(buttonSize);
        forYouButton.setBorder(BorderFactory.createEmptyBorder());
        JButton followingButton = new JButton("Following");
        followingButton.setBorder(BorderFactory.createEmptyBorder());
        followingButton.setPreferredSize(buttonSize);
        JButton tagsButton = new JButton("Tags");
        tagsButton.setPreferredSize(buttonSize);
        tagsButton.setBorder(BorderFactory.createEmptyBorder());
        JButton createButton = new JButton("NEW POST??");
        createButton.setPreferredSize(new Dimension(80, 30));
        createButton.setBorder(BorderFactory.createEmptyBorder());
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateNewPostView createNewPostView = viewManagerModel.getCreateNewPostView();
                createNewPostView.recipePostView();
                viewManagerModel.setState("create new post");

            }
        });
        tabsPanel.add(forYouButton);
        tabsPanel.add(followingButton);
        tabsPanel.add(tagsButton);
        tabsPanel.add(createButton);

        mainPanel.add(tabsPanel, BorderLayout.NORTH);

        // scrollable feed panel
        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(feedPanel, BorderLayout.CENTER);

        updateHomeFeed();

        JScrollPane mainScrollPane = new JScrollPane(wrapperPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        mainScrollPane.setPreferredSize(new Dimension(1300, 750));
        mainPanel.add(mainScrollPane, BorderLayout.CENTER);

        // bottom menu bar
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    public void updateHomeFeed() {
        feedPanel.removeAll();
        //TODO: refresh work properly after having made post
        trialpost.setImageURLs(new ArrayList<>(Arrays.asList("https://i.imgur.com/eA9NeJ1.jpeg", "https://i.imgur.com/wzX83Zc.jpeg", "https://i.ytimg.com/vi/4mr2dqI0VVs/maxresdefault.jpg")));

        int maxNumberOfDisplayingPosts = 10;
        //should technically make new use case, fetch_home_feed to abide by clean architecture exactly (using inputdata etc) oops
        List<Post> randomFeedPosts = fetchPostController.getRandomFeedPosts(maxNumberOfDisplayingPosts);
        List<Post> randomFeedPosts2 = fetchPostController.getRandomFeedPosts(maxNumberOfDisplayingPosts);


        for (int i = 0; i < randomFeedPosts.size(); i++) {
            JPanel feedRow = new JPanel();
            feedRow.setLayout(new BoxLayout(feedRow, BoxLayout.X_AXIS));
            feedRow.add(Box.createRigidArea(new Dimension(40, 0)));
            Post post1 = randomFeedPosts.get(i);
            PostPanel postPanel = new PostPanel(viewManagerModel, post1, 400, 400, postCommentsLikesDataAccessObject);
            postPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
            feedRow.setMaximumSize(new Dimension(2000, 420));
            feedRow.add(postPanel);

            Post post2 = randomFeedPosts2.get(i);
            PostPanel postTwo = new PostPanel(viewManagerModel, post2, 600, 400, postCommentsLikesDataAccessObject);
            postTwo.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
            feedRow.add(postTwo); // second post
            feedRow.add(Box.createHorizontalGlue());
//commented out due to lack of horizontal space (to make window fit on all screen sizes)
//            feedRow.add(Box.createRigidArea(new Dimension(20, 0))); // spacing
//            feedRow.add(new JLabel("placeholder for clubs list", 22, Color.RED, 3)); // clubs
//            feedRow.add(Box.createHorizontalGlue());

            feedPanel.add(feedRow);
            //feedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }


    public String getViewName() {
        return viewName;
    }

}
