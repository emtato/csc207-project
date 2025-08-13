package view;

import app.Session;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_club.CreateClubController;
import interface_adapter.create_club.CreateClubState;
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
    private final DefaultListModel<String> currentMembersModel = new DefaultListModel<>();

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
            currentMembersModel.addElement(currentUser.getUsername());
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
        memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.Y_AXIS));

        JPanel topRow = new JPanel();
        topRow.setLayout(new BoxLayout(topRow, BoxLayout.X_AXIS));

        JButton addMembersButton = new JButton("Select Members");
        addMembersButton.addActionListener(e -> handleAddMembers());

        JButton removeSelectedButton = new JButton("Remove Selected");
        JList<String> membersList = new JList<>(currentMembersModel);
        membersList.setVisibleRowCount(4);
        membersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane membersScroll = new JScrollPane(membersList);
        membersScroll.setPreferredSize(new Dimension(250, 80));

        removeSelectedButton.addActionListener(e -> {
            java.util.List<String> selected = membersList.getSelectedValuesList();
            if (!selected.isEmpty()) {
                members.removeIf(a -> selected.contains(a.getUsername()));
                for (String s : selected) {
                    currentMembersModel.removeElement(s);
                }
                memberCountLabel.setText(members.size() + " members");
            }
        });

        topRow.add(addMembersButton);
        topRow.add(Box.createHorizontalStrut(10));
        topRow.add(removeSelectedButton);
        topRow.add(Box.createHorizontalStrut(10));
        topRow.add(memberCountLabel);

        memberPanel.add(topRow);
        memberPanel.add(Box.createVerticalStrut(5));
        memberPanel.add(membersScroll);
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
        Account runtimeCurrent = Session.getCurrentAccount();
        if (runtimeCurrent == null) {
            JOptionPane.showMessageDialog(this, "Please log in to add members to the club");
            return;
        }
        if (createClubController != null) {
            createClubController.showMemberSelection(new ArrayList<>(members), runtimeCurrent.getUsername());
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
            CreateClubState state = createClubViewModel.getState();
            if (state.isSelectionMode()) {
                // Show selection dialog with available usernames
                java.util.List<String> available = state.getMemberUsernames();
                if (available.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No additional users available");
                } else {
                    JList<String> selectable = new JList<>(available.toArray(new String[0]));
                    selectable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    int result = JOptionPane.showConfirmDialog(this, new JScrollPane(selectable),
                            "Select Members", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        for (String chosen : selectable.getSelectedValuesList()) {
                            boolean already = false;
                            for (Account a : members) { if (a.getUsername().equals(chosen)) { already = true; break; } }
                            if (!already) {
                                members.add(new Account(chosen, "")); // lightweight placeholder; real Account fetched in interactor
                                currentMembersModel.addElement(chosen);
                            }
                        }
                        memberCountLabel.setText(members.size() + " members");
                    }
                }
                state.setSelectionMode(false); // reset to avoid re-trigger
            } else if (error != null) {
                JOptionPane.showMessageDialog(this, error);
            } else {
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
        currentMembersModel.clear();
        Account runtimeCurrent = Session.getCurrentAccount();
        if (runtimeCurrent != null) {
            members.add(runtimeCurrent);
            currentMembersModel.addElement(runtimeCurrent.getUsername());
        } else if (currentUser != null) { // fallback
            members.add(currentUser);
            currentMembersModel.addElement(currentUser.getUsername());
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