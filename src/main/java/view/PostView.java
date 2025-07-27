package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.post_view.PostViewModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.JLabel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;


/* Description:
 * ^ • ω • ^
 */

public class PostView extends JPanel {

    private final String viewName = "recipe view";
    private final PostViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    //fonts & styles
    private final Font Title = new Font("Roboto", Font.BOLD, 20);
    private final Font subtite = new Font("Roboto", Font.PLAIN, 16);
    private final Font text = new Font("Roboto", Font.PLAIN, 15);
    //middle
    private final JTextArea recipeText = new JTextArea();
    //bottom
    private final JButton backButton = new JButton("Back");
    private final JButton mapsButton = new JButton("Maps");
    private final JButton slopButton = new JButton("Feed");
    private final JButton settingsButton = new JButton("Settings");
    private final JButton profileButton = new JButton("Profile");
    //right
    private final JButton likeButton = new JButton("Like");
    private final JButton analyzeButton = new JButton("Analyze");
    private final JButton saveButton = new JButton("Add to list");
    private final JButton shareButton = new JButton("Share");

    public PostView(PostViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        //this.viewModel.addPropertyChangeListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel bottomPanel = new JPanel(new FlowLayout()); //pannel for main ui buttons (persists across views)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JPanel centerPanel = new JPanel();

        //top
        JLabel title = new JLabel("HELLLOOOO aaiaiaiee"); //recipe/post title
        title.setFont(Title);

        topPanel.add(title);
        JLabel subtitle = new JLabel("meowers"); // post author and date
        subtitle.setFont(subtite);
        subtitle.setForeground(Color.GRAY);
        JLabel tags = new JLabel("tags");
        tags.setFont(text);
        tags.setForeground(Color.LIGHT_GRAY);

        topPanel.add(subtitle);
        topPanel.add(tags);

        //middle
        recipeText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(recipeText);
        JTextArea comments = new JTextArea();
        scrollPane.add(comments);
        scrollPane.setPreferredSize(new Dimension(1300, 800));
        centerPanel.add(scrollPane);
        comments.setBackground(Color.PINK);
        comments.setOpaque(true);


        //bottom
        ArrayList<JButton> bottomButtons = new ArrayList<>();
        bottomButtons.add(backButton);
        bottomButtons.add(mapsButton);
        bottomButtons.add(slopButton);
        bottomButtons.add(settingsButton);
        bottomButtons.add(profileButton);
        for (JButton button : bottomButtons) {
            button.setFont(text);
            bottomPanel.add(button);
        }

        //right
        ArrayList<JButton> rightButtons = new ArrayList<>();
        rightButtons.add(likeButton);
        rightButtons.add(analyzeButton);
        rightButtons.add(saveButton);
        rightButtons.add(shareButton);
        for (JButton button : rightButtons) {
            button.setFont(text);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBackground(Color.PINK);
            button.setOpaque(true);
            rightPanel.add(button);
        }


        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        add(menuBar, BorderLayout.NORTH);

    }

    //@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == analyzeButton) {
            System.out.println("analyzeButton");
        }
    }

    public String getViewName() {
        return viewName;
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new PostView(new PostViewModel()));
//        frame.setPreferredSize(new Dimension(1920, 1080));
//        frame.pack();
//        frame.setVisible(true);
//    }
}
