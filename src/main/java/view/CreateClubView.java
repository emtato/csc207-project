package view;

import app.Session;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_club.CreateClubController;
import interface_adapter.create_club.CreateClubViewModel;
import entity.Account;
import view.ui_components.MenuBarPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class CreateClubView extends JPanel implements PropertyChangeListener {
    private final String viewName = "create club view";
    private final ViewManagerModel viewManagerModel;
    private CreateClubController createClubController;
    private final CreateClubViewModel createClubViewModel;
    private final Account currentUser; // may be null at construction; runtime user retrieved via Session when creating
    private final ArrayList<Account> members = new ArrayList<>();

    private final JTextArea titleArea;
    private final JTextArea bodyArea;
    private final JTextArea imagesArea;
    private final JTextArea tagsArea;
    private final JLabel memberCountLabel;

    public CreateClubView(ViewManagerModel viewManagerModel,
                         CreateClubController createClubController,
                         CreateClubViewModel createClubViewModel,
                         Account currentUser) {
        this.viewManagerModel = viewManagerModel;
        this.createClubController = createClubController;
        this.createClubViewModel = createClubViewModel;
        this.currentUser = currentUser;

        if (currentUser != null) {
            members.add(currentUser);
        }

        createClubViewModel.addPropertyChangeListener(this);

        // Initialize components
        titleArea = new JTextArea(2, 20);
        bodyArea = new JTextArea(6, 80);
        imagesArea = new JTextArea(3, 80);
        tagsArea = new JTextArea(1, 80);
        memberCountLabel = new JLabel(members.size() + " members");

        setupUI();
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Create a Club");
        titleLabel.setFont(GUIConstants.FONT_TITLE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        MenuBarPanel menuBar = new MenuBarPanel(viewManagerModel);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        setupTextAreas();

        contentPanel.add(titleArea);
        contentPanel.add(bodyArea);
        contentPanel.add(imagesArea);
        contentPanel.add(tagsArea);

        JPanel memberPanel = createMemberPanel();
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(memberPanel);

        JButton createButton = new JButton("Create Club");
        createButton.addActionListener(e -> handleCreateClub());

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createButton);

        return contentPanel;
    }

    private void setupTextAreas() {
        setupTextArea(titleArea, "Enter club title");
        setupTextArea(bodyArea, "Enter club description");
        setupTextArea(imagesArea, "Enter link to club profile image");
        setupTextArea(tagsArea, "Enter club tags separated by commas");

        bodyArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        tagsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private JPanel createMemberPanel() {
        JPanel memberPanel = new JPanel();
        memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.X_AXIS));

        JButton addMembersButton = new JButton("Add Members");
        addMembersButton.addActionListener(e -> handleAddMembers());

        memberPanel.add(addMembersButton);
        memberPanel.add(Box.createHorizontalStrut(10));
        memberPanel.add(memberCountLabel);

        return memberPanel;
    }

    private void handleCreateClub() {
        String title = titleArea.getText();
        String description = bodyArea.getText();
        String tagsText = tagsArea.getText();

        if (isDefaultText(title) || isDefaultText(description) || isDefaultText(tagsText)) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields");
            return;
        }

        ArrayList<String> tags = new ArrayList<>();
        for (String tag : tagsText.split("\\s*,\\s*")) {
            if (!tag.isEmpty()) {
                tags.add(tag);
            }
        }

        // Build member username list based on current members plus the runtime current user
        ArrayList<String> memberUsernames = new ArrayList<>();
        for (Account member : members) {
            memberUsernames.add(member.getUsername());
        }
        String runtimeCurrentUsername = Session.getCurrentUsername();
        if (runtimeCurrentUsername != null && !memberUsernames.contains(runtimeCurrentUsername)) {
            memberUsernames.add(runtimeCurrentUsername);
        }

        createClubController.createClub(title, description, memberUsernames, tags);
    }

    private void handleAddMembers() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to add members to the club");
            return;
        }

        // For now, just show a simple dialog to add members
        String username = JOptionPane.showInputDialog(this, "Enter username to add:");
        if (username != null && !username.trim().isEmpty()) {
            Account newMember = new Account(username.trim(), "");
            if (!members.contains(newMember)) {
                members.add(newMember);
                memberCountLabel.setText(members.size() + " members");
            }
        }
    }

    private void setupTextArea(JTextArea textArea, String hint) {
        textArea.setText(hint);
        textArea.setForeground(Color.GRAY);

        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(hint)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setForeground(Color.GRAY);
                    textArea.setText(hint);
                }
            }
        });
    }

    private boolean isDefaultText(String text) {
        return text.equals("Enter club title") ||
               text.equals("Enter club description") ||
               text.equals("Enter club tags separated by commas") ||
               text.equals("Enter link to club profile image");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            String error = createClubViewModel.getState().getError();
            if (error != null) {
                JOptionPane.showMessageDialog(this, error);
            } else {
                // Club creation was successful
                clearFields();
                JOptionPane.showMessageDialog(this, "Club created successfully!");
                viewManagerModel.setState("club view");
            }
        }
    }

    private void clearFields() {
        titleArea.setText("");
        bodyArea.setText("");
        imagesArea.setText("");
        tagsArea.setText("");

        // Reset to default state
        titleArea.dispatchEvent(new FocusEvent(titleArea, FocusEvent.FOCUS_LOST));
        bodyArea.dispatchEvent(new FocusEvent(bodyArea, FocusEvent.FOCUS_LOST));
        imagesArea.dispatchEvent(new FocusEvent(imagesArea, FocusEvent.FOCUS_LOST));
        tagsArea.dispatchEvent(new FocusEvent(tagsArea, FocusEvent.FOCUS_LOST));

        members.clear();
        Account runtimeCurrent = Session.getCurrentAccount();
        if (runtimeCurrent != null) {
            members.add(runtimeCurrent);
        } else if (currentUser != null) { // fallback
            members.add(currentUser);
        }
        memberCountLabel.setText(members.size() + " members");
    }

    public void setCreateClubController(CreateClubController controller) {
        this.createClubController = controller;
    }

    public String getViewName() {
        return viewName;
    }

}