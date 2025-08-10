package view;

import app.Session;
import data_access.PostCommentsLikesDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Club;
import entity.Post;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_post_view.CreatePostController;
import interface_adapter.create_post_view.CreatePostPresenter;
import interface_adapter.create_post_view.CreatePostState;
import interface_adapter.create_post_view.CreatePostViewModel;
import use_case.create_post.CreatePostInputData;
import use_case.create_post.CreatePostInteractor;
import view.ui_components.MenuBarPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by Emilia on 2025-07-27!
 * Description:
 * ^ • ω • ^
 */

// This view is for the user to fill in information for their new post. It could be a recipe, an event, clubs or images
// Emilia- recipes

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;


public class CreateNewPostView extends JPanel implements PropertyChangeListener {
    private final JPanel contentPanel;
    private final ViewManagerModel viewManagerModel;
    private final JRadioButton recipes = new JRadioButton("post new recipe :3 ");
    private final JRadioButton generalPost = new JRadioButton("General Post");
    private final JRadioButton announcementPost = new JRadioButton("Announcement");
    private final String viewName = "create new post";
    private Club club;

    // Make these fields regular instance fields instead of having createPostController be accessed through this$0
    private CreatePostController createPostController;
    private final CreatePostViewModel createPostViewModel;

    public CreateNewPostView(ViewManagerModel viewManagerModel, CreatePostViewModel createPostViewModel) {
        this(viewManagerModel, createPostViewModel, null);
    }

    public CreateNewPostView(ViewManagerModel viewManagerModel, CreatePostViewModel createPostViewModel, Club club) {
        this.viewManagerModel = viewManagerModel;
        this.createPostViewModel = createPostViewModel;
        this.club = club;
        this.contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        setSize(1300, 800);
        setLayout(new BorderLayout());

        // If this is a club post, only show relevant options
        if (club != null) {
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(recipes);
            buttonGroup.add(generalPost);
            buttonGroup.add(announcementPost);

            JPanel radioPanel = new JPanel();
            radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Add spacing between buttons

            // Make announcement option more prominent for club posts
            announcementPost.setFont(new Font("Arial", Font.BOLD, 14));
            announcementPost.setForeground(new Color(0, 100, 0));  // Dark green color

            radioPanel.add(recipes);
            radioPanel.add(generalPost);
            radioPanel.add(announcementPost);

            JLabel label = new JLabel("Select what type of post you want to make", SwingConstants.CENTER);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setFont(new Font("Arial", Font.BOLD, 16));

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add padding
            topPanel.add(label);
            topPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing
            topPanel.add(radioPanel);

            add(topPanel, BorderLayout.NORTH);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
            add(contentPanel, BorderLayout.CENTER);
        } else {
            ButtonGroup group = new ButtonGroup();
            group.add(recipes);
            group.add(generalPost);

            JPanel radioPanel = new JPanel();
            radioPanel.add(recipes);
            radioPanel.add(generalPost);
            JLabel label = new JLabel("select what type of post u wanna make", SwingConstants.CENTER);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.add(label);
            topPanel.add(radioPanel);
            add(topPanel, BorderLayout.NORTH);
        }

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        menuBar.setPreferredSize(new Dimension(1200, 100));

        add(contentPanel, BorderLayout.CENTER);
        add(menuBar, BorderLayout.SOUTH);

        // Ensure action listeners are added for all buttons
        recipes.addActionListener(this::actionPerformed);
        generalPost.addActionListener(this::actionPerformed);
        announcementPost.addActionListener(this::actionPerformed);

        setVisible(true);
    }

    /**
     * Actionlistener function to delegate selections to functions to load desired views
     *
     * @param e actionevent
     */
    public void actionPerformed(ActionEvent e) {
        // Store the controller before clearing the panel
        final CreatePostController currentController = this.createPostController;
        contentPanel.removeAll();

        if (e.getSource() == recipes) {
            recipePostView();
        } else if (e.getSource() == generalPost) {
            generalPostView();
        } else if (e.getSource() == announcementPost) {
            announcementPostView();
        }
        // Restore the controller after rebuilding the view
        this.createPostController = currentController;

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    public void recipePostView() {
        contentPanel.removeAll();
        System.out.println(recipes.isSelected());
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JTextArea titleArea = new JTextArea("Enter post title", 2, 20);
        JTextArea bodyArea = new JTextArea("Enter recipe description", 6, 80);
        bodyArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea imagesArea = new JTextArea("Enter link to images, separated by commas, must end in .jpg, .png, etc", 3, 80);
        imagesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea ingredientsListArea = new JTextArea("Enter list of ingredients separated by commas", 5, 80);
        ingredientsListArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea stepsArea = new JTextArea("Enter steps to make the yum yum", 17, 80);
        stepsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea cuisinesArea = new JTextArea("Enter cuisines and tags separated by commas if u want", 1, 80);
        cuisinesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        //set wrapping
        titleArea.setLineWrap(true);
        titleArea.setWrapStyleWord(true);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        imagesArea.setLineWrap(true);
        ingredientsListArea.setLineWrap(true);
        stepsArea.setLineWrap(true);
        stepsArea.setWrapStyleWord(true);

        textFIeldHints(titleArea, "Enter post title");
        textFIeldHints(bodyArea, "Enter recipe description");
        textFIeldHints(ingredientsListArea, "Enter list of ingredients separated by commas");
        textFIeldHints(stepsArea, "Enter steps to make the yum yum");
        textFIeldHints(cuisinesArea, "Enter cuisines and tags separated by commas if u want");
        textFIeldHints(imagesArea, "Enter link to images, separated by commas, must end in .jpg, .png, etc");

        contentPanel.add(titleArea);
        contentPanel.add(bodyArea);
        contentPanel.add(imagesArea);
        contentPanel.add(ingredientsListArea);
        contentPanel.add(stepsArea);
        contentPanel.add(cuisinesArea);

        JButton okButton = new JButton("send it \uD83E\uDEE3");
        okButton.setFont(new Font("papyrus", Font.BOLD, 20));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String title = titleArea.getText();
                String body = bodyArea.getText();
                ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(ingredientsListArea.getText().split(",")));
                String steps = stepsArea.getText();
                ArrayList<String> tags = new ArrayList<>();

                if (!cuisinesArea.getText().equals("Enter cuisines and tags separated by commas if u want")) {
                    tags = new ArrayList<>(Arrays.asList(cuisinesArea.getText().split(",")));
                }
                ArrayList<String> imagesList = new ArrayList<>();
                if (!imagesArea.getText().equals("Enter link to images, separated by commas, must end in .jpg, .png, etc")) {
                    imagesList = new ArrayList<>(Arrays.asList(imagesArea.getText().split(",")));
                }

                CreatePostInputData postData = new CreatePostInputData(
                        Session.getCurrentAccount(),
                        title,
                        "recipe",
                        body,
                        ingredients,
                        steps,
                        tags,
                        imagesList,
                        new ArrayList<>()
                );
                if (club != null) {
                    postData.setClubId(club.getId());
                }
                handlePostCreation(postData);
            }
        });
        contentPanel.add(okButton);


    }

    private void generalPostView() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JTextArea titleArea = new JTextArea("Enter post title", 2, 20);
        JTextArea bodyArea = new JTextArea("Enter post description", 6, 80);
        bodyArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea imagesArea = new JTextArea("Enter link to images, separated by commas, must end in .jpg, .png, etc", 3, 80);
        imagesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea tagsArea = new JTextArea("Enter cuisines and tags separated by commas if u want", 1, 80);
        tagsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        //set wrapping
        titleArea.setLineWrap(true);
        titleArea.setWrapStyleWord(true);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        imagesArea.setLineWrap(true);
        tagsArea.setLineWrap(true);

        textFIeldHints(titleArea, "Enter post title");
        textFIeldHints(bodyArea, "Enter post description");
        textFIeldHints(imagesArea, "Enter link to images, separated by commas, must end in .jpg, .png, etc");
        textFIeldHints(tagsArea, "Enter cuisines and tags separated by commas if u want");

        contentPanel.add(titleArea);
        contentPanel.add(bodyArea);
        contentPanel.add(imagesArea);
        contentPanel.add(tagsArea);

        JButton okButton = new JButton("send it \uD83E\uDEE3");
        okButton.setFont(new Font("papyrus", Font.BOLD, 20));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String title = titleArea.getText();
                String body = bodyArea.getText();
                ArrayList<String> tags = new ArrayList<>();
                ArrayList<String> imagesList = new ArrayList<>();

                if (!imagesArea.getText().equals("Enter link to images, separated by commas, must end in .jpg, .png, etc")) {
                    imagesList = new ArrayList<>(Arrays.asList(imagesArea.getText().split(",")));
                }

                CreatePostInputData postData = new CreatePostInputData(
                        Session.getCurrentAccount(),
                        title,
                        "general",
                        body,
                        new ArrayList<>(),
                        "",
                        tags,
                        imagesList,
                        new ArrayList<>()
                );
                if (club != null) {
                    postData.setClubId(club.getId());
                }
                handlePostCreation(postData);
            }
        });
        contentPanel.add(okButton);
    }

    private void announcementPostView() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JTextArea titleArea = new JTextArea("Enter announcement title", 2, 20);
        JTextArea bodyArea = new JTextArea("Enter announcement content", 6, 80);
        bodyArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea imagesArea = new JTextArea("Enter link to images, separated by commas, must end in .jpg, .png, etc", 3, 80);
        imagesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Set wrapping
        titleArea.setLineWrap(true);
        titleArea.setWrapStyleWord(true);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        imagesArea.setLineWrap(true);

        textFIeldHints(titleArea, "Enter announcement title");
        textFIeldHints(bodyArea, "Enter announcement content");
        textFIeldHints(imagesArea, "Enter link to images, separated by commas, must end in .jpg, .png, etc");

        contentPanel.add(titleArea);
        contentPanel.add(bodyArea);
        contentPanel.add(imagesArea);

        JButton okButton = new JButton("Post Announcement");
        okButton.setFont(new Font("Arial", Font.BOLD, 16));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String title = titleArea.getText();
                String body = bodyArea.getText();
                ArrayList<String> imagesList = new ArrayList<>();
                if (!imagesArea.getText().equals("Enter link to images, separated by commas, must end in .jpg, .png, etc")) {
                    imagesList = new ArrayList<>(Arrays.asList(imagesArea.getText().split(",")));
                }

                CreatePostInputData postData = new CreatePostInputData(
                        Session.getCurrentAccount(),
                        title,
                        "announcement",
                        body,
                        new ArrayList<>(),
                        "",
                        new ArrayList<>(),
                        imagesList,
                        new ArrayList<>()
                );

                if (club != null) {
                    postData.setClubId(club.getId());
                }
                handlePostCreation(postData);
            }
        });

        contentPanel.add(okButton);
    }

    /**
     * Function for TextField/TextAreas to display a grey hint message when no text has been entered.
     *
     * @param titleField The text component to add hint to
     * @param hint       the hint message
     */
    private void textFIeldHints(JTextComponent titleField, String hint) {

        titleField.setForeground(Color.GRAY);

        titleField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (titleField.getText().equals(hint)) {
                    titleField.setText("");
                    titleField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (titleField.getText().isEmpty()) {
                    titleField.setForeground(Color.GRAY);
                    titleField.setText(hint);
                }
            }
        });
    }

//end of recipe new post view
//---------------------------------------------------------------------------------------------------------------------

    public String getViewName() {
        return viewName;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            CreatePostState state = (CreatePostState) evt.getNewValue();
            // Clear text fields when state changes
            contentPanel.removeAll();
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    public void setCreatePostController(CreatePostController controller) {
        System.out.println("Setting CreatePostController: " + (controller != null ? "not null" : "null"));
        this.createPostController = controller;
        if (controller == null) {
            System.err.println("Warning: Setting null controller in CreateNewPostView");
        }
    }

    public void setClub(Club club) {
        this.club = club;
        removeAll();  // Clear current components
        this.contentPanel.removeAll();

        setSize(1300, 800);
        setLayout(new BorderLayout());

        // If this is a club post, only show relevant options
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(recipes);
        buttonGroup.add(generalPost);
        buttonGroup.add(announcementPost);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Add spacing between buttons

        // Make announcement option more prominent for club posts
        announcementPost.setFont(new Font("Arial", Font.BOLD, 14));
        announcementPost.setForeground(new Color(0, 100, 0));  // Dark green color

        radioPanel.add(recipes);
        radioPanel.add(generalPost);
        radioPanel.add(announcementPost);

        JLabel label = new JLabel("Select what type of post you want to make", SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add padding
        topPanel.add(label);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing
        topPanel.add(radioPanel);

        add(topPanel, BorderLayout.NORTH);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        menuBar.setPreferredSize(new Dimension(1200, 100));

        add(contentPanel, BorderLayout.CENTER);
        add(menuBar, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void handlePostCreation(CreatePostInputData postData) {
        final CreatePostController controller = this.createPostController;
        if (controller == null) {
            System.err.println("Error: CreatePostController is null when trying to create post");
            JOptionPane.showMessageDialog(this,
                "Unable to create post at this time. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        controller.execute(postData);
        viewManagerModel.setState("homepage view");
        HomePageView homePageView = viewManagerModel.getHomePageView();
        homePageView.updateHomeFeed();
    }
}
