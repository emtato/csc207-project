package view;

import interface_adapter.ViewManagerModel;
import data_access.DBClubsDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;
import view.ui_components.MenuBarPanel;

import javax.swing.*;
import javax.swing.JLabel;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateClubView extends JPanel {
    private final String viewName = "create club view";
    private final ViewManagerModel viewManagerModel;
    private final DBClubsDataAccessObject clubsDAO;
    private final Account currentUser;

    public CreateClubView(ViewManagerModel viewManagerModel, DBClubsDataAccessObject clubsDAO, Account currentUser) {
        this.viewManagerModel = viewManagerModel;
        this.clubsDAO = clubsDAO;
        this.currentUser = currentUser;

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Create a Club");
        titleLabel.setFont(GUIConstants.FONT_TITLE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JTextArea titleArea = new JTextArea("Enter club title", 2,20);
        JTextArea bodyArea = new JTextArea("Enter club description", 6, 80);
        bodyArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea imagesArea = new JTextArea("Enter link to club profile image", 3, 80);
        imagesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea tagsArea = new JTextArea("Enter club tags seperated by commas", 1, 80);
        tagsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        textFIeldHints(titleArea, "Enter club title");
        textFIeldHints(bodyArea, "Enter club description");
        textFIeldHints(tagsArea, "Enter club tags seperated by commas");
        textFIeldHints(imagesArea, "Enter link to club profile image");

        contentPanel.add(titleArea);
        contentPanel.add(bodyArea);
        contentPanel.add(imagesArea);
        contentPanel.add(tagsArea);

        JButton createButton = new JButton("Create Club");
        createButton.addActionListener(e -> {
            String title = titleArea.getText();
            String description = bodyArea.getText();
            String tagsText = tagsArea.getText();

            if (title.equals("Enter club title") || description.equals("Enter club description")
                || tagsText.equals("Enter club tags seperated by commas")) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }

            ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagsText.split("\\s*,\\s*")));

            ArrayList<Account> members = new ArrayList<>();
            // Only add the current user if not null
            if (currentUser != null) {
                members.add(currentUser);
            }

            // Create empty posts list
            ArrayList<Post> posts = new ArrayList<>();

            // Generate a unique club ID using current timestamp
            long clubId = System.currentTimeMillis();

            try {
                clubsDAO.writeClub(clubId, members, title, description, posts, tags);
                JOptionPane.showMessageDialog(this, "Club created successfully!");
                clearFields(titleArea, bodyArea, tagsArea, imagesArea);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating club: " + ex.getMessage());
            }
        });

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createButton);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    /**
     * Function for TextField/TextAreas to display a grey hint message when no text has been entered.
     *
     * @param titleField The text component to add hint to
     * @param hint       the hint message
     */
    private void textFIeldHints(JTextComponent titleField, String hint) {

        titleField.setForeground(Color.GRAY);

        titleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (titleField.getText().equals(hint)) {
                    titleField.setText("");
                    titleField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (titleField.getText().isEmpty()) {
                    titleField.setForeground(Color.GRAY);
                    titleField.setText(hint);
                }
            }
        });
    }

    private void clearFields(JTextComponent... fields) {
        for (JTextComponent field : fields) {
            field.setText("");
            field.dispatchEvent(new java.awt.event.FocusEvent(field, java.awt.event.FocusEvent.FOCUS_LOST));
        }
    }

    public String getViewName() {
        return viewName;
    }

}
