package view;

import interface_adapter.ViewManagerModel;
import data_access.FileClubsDataAccessObject;
import data_access.FileUserDataAccessObject;
import entity.Account;
import entity.Post;
import org.json.JSONObject;
import view.ui_components.MenuBarPanel;
import view.GUIConstants;

import javax.swing.*;
import javax.swing.JLabel;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class CreateClubView extends JPanel {
    private final String viewName = "create club view";
    private final ViewManagerModel viewManagerModel;
    private final FileClubsDataAccessObject clubsDAO;
    private final Account currentUser;
    private ArrayList<Account> members = new ArrayList<>();
    private final FileUserDataAccessObject userDataAccessObject = (FileUserDataAccessObject) FileUserDataAccessObject.getInstance();

    public CreateClubView(ViewManagerModel viewManagerModel, FileClubsDataAccessObject clubsDAO, Account currentUser) {
        this.viewManagerModel = viewManagerModel;
        this.clubsDAO = clubsDAO;
        this.currentUser = currentUser;
        if (currentUser != null) {
            members.add(currentUser);
        }

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

        JPanel memberPanel = new JPanel();
        memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.X_AXIS));

        JButton addMembersButton = new JButton("Add Members");
        JLabel memberCountLabel = new JLabel(members.size() + " members");

        addMembersButton.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please log in to add members to the club");
                return;
            }

            JDialog dialog = new JDialog();
            dialog.setTitle("Add Members");
            dialog.setModal(true);
            dialog.setLayout(new BorderLayout());

            JPanel memberListPanel = new JPanel();
            memberListPanel.setLayout(new BoxLayout(memberListPanel, BoxLayout.Y_AXIS));

            ArrayList<JCheckBox> checkBoxes = new ArrayList<>();

            try {
                String content = new String(Files.readAllBytes(Paths.get("src/main/java/data_access/user_data.json")));
                JSONObject data = new JSONObject(content);
                if (data.has("users")) {
                    JSONObject users = data.getJSONObject("users");
                    for (String username : users.keySet()) {
                        if (!username.equals(currentUser.getUsername())) {
                            JCheckBox checkBox = new JCheckBox(username);
                            // Pre-select if user is already a member
                            checkBox.setSelected(members.stream().anyMatch(m -> m.getUsername().equals(username)));
                            checkBoxes.add(checkBox);
                            memberListPanel.add(checkBox);
                        }
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
                return;
            }

            if (checkBoxes.isEmpty()) {
                JLabel noUsersLabel = new JLabel("No other users available to add");
                noUsersLabel.setHorizontalAlignment(SwingConstants.CENTER);
                memberListPanel.add(noUsersLabel);
            }

            JScrollPane scrollPane = new JScrollPane(memberListPanel);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton okButton = new JButton("OK");
            okButton.addActionListener(ev -> {
                members.clear();
                if (currentUser != null) {
                    members.add(currentUser);
                }

                for (JCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        Account member = (Account) userDataAccessObject.get(checkBox.getText());
                        if (member != null) {
                            members.add(member);
                        }
                    }
                }

                memberCountLabel.setText(members.size() + " members");
                dialog.dispose();
            });

            dialog.add(okButton, BorderLayout.SOUTH);
            dialog.setSize(GUIConstants.STANDARD_PANEL_WIDTH, GUIConstants.STANDARD_PANEL_HEIGHT);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        memberPanel.add(addMembersButton);
        memberPanel.add(Box.createHorizontalStrut(10));
        memberPanel.add(memberCountLabel);

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(memberPanel);

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

            ArrayList<Post> posts = new ArrayList<>();

            // Generate a unique club ID using current timestamp
            long clubId = System.currentTimeMillis();

            try {
                clubsDAO.writeClub(clubId, members, title, description, posts, tags);
                JOptionPane.showMessageDialog(this, "Club created successfully!");
                clearFields(titleArea, bodyArea, tagsArea, imagesArea);
                members.clear();
                if (currentUser != null) {
                    members.add(currentUser);
                }
                memberCountLabel.setText(members.size() + " members");
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
