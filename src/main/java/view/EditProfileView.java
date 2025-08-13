package view;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfileState;
import interface_adapter.edit_profile.EditProfileViewModel;
import interface_adapter.view_profile.ProfileController;
import view.ui_components.GeneralJLabel;
import view.ui_components.LabelTextPanel;
import view.ui_components.ProfilePictureLabel;

public class EditProfileView extends JPanel implements PropertyChangeListener {
    private static final String LINE_BREAK = "\n";
    private final String viewName = "edit profile";
    private final EditProfileViewModel editProfileViewModel;
    private EditProfileController editProfileController;
    private ProfileController profileController;

    private final ProfilePictureLabel profilePicture;
    private final JLabel displayName;
    private final JLabel username;
    private final JTextArea bio;
    private final JTextField nameInputField;
    private final JTextArea bioInputField;
    private final JTextField uploadProfilePicture;
    private final JTextArea preferencesField;
    private final JLabel location;
    private final JTextField locationInputField;
    private final JButton saveChangesButton;
    private final JButton backButton;

    public EditProfileView(EditProfileViewModel editProfileViewModel) {
        this.editProfileViewModel = editProfileViewModel;
        this.editProfileViewModel.addPropertyChangeListener(this);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // instantiate variables
        profilePicture = new ProfilePictureLabel(this.editProfileViewModel.getState().getProfilePictureUrl(),
                EditProfileViewModel.PFP_WIDTH, EditProfileViewModel.PFP_HEIGHT);
        displayName = new GeneralJLabel(this.editProfileViewModel.getState().getDisplayName(), GUIConstants.TEXT_SIZE,
                GUIConstants.WHITE);
        username = new GeneralJLabel(this.editProfileViewModel.getState().getUsername(), GUIConstants.SMALL_TEXT_SIZE,
                GUIConstants.WHITE);
        bio = createBioPreview();
        backButton = createBackButton();
        nameInputField = new JTextField();
        nameInputField.setColumns(EditProfileViewModel.INPUT_FIELD_COL_NUM);
        nameInputField.setText(this.editProfileViewModel.getState().getNewDisplayName());
        bioInputField = new JTextArea(EditProfileViewModel.BIO_ROW_NUM, EditProfileViewModel.INPUT_FIELD_COL_NUM);
        bioInputField.setText(this.editProfileViewModel.getState().getNewBio());
        bioInputField.setLineWrap(true);
        bioInputField.setWrapStyleWord(true);
        uploadProfilePicture = new JTextField(this.editProfileViewModel.getState().getNewProfilePictureUrl());
        uploadProfilePicture.setColumns(EditProfileViewModel.INPUT_FIELD_COL_NUM);
        uploadProfilePicture.setText(this.editProfileViewModel.getState().getNewProfilePictureUrl());
        preferencesField = new JTextArea(EditProfileViewModel.PREF_ROW_NUM, EditProfileViewModel.INPUT_FIELD_COL_NUM);
        location = new GeneralJLabel(this.editProfileViewModel.getState().getLocation(), GUIConstants.SMALL_TEXT_SIZE,
                GUIConstants.WHITE);
        locationInputField = new JTextField();
        locationInputField.setColumns(EditProfileViewModel.INPUT_FIELD_COL_NUM);
        locationInputField.setText(this.editProfileViewModel.getState().getNewLocation());
        saveChangesButton = new JButton(EditProfileViewModel.SAVE_CHANGES_BUTTON_LABEL);
        saveChangesButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        saveChangesButton.setFont(GUIConstants.FONT_TEXT);
        saveChangesButton.setForeground(GUIConstants.PINK);

        createView();

        // add listeners
        addNameInputFieldListener();
        addBioInputFieldListener();
        addUploadProfilePictureFieldListener();
        addPreferencesFieldListener();
        addLocationInputFieldListener();
        addSaveChangesButtonListener();
        addBackButtonListener();
    }

    private JTextArea createBioPreview() {
        final JTextArea bioArea = new JTextArea(EditProfileViewModel.BIO_ROW_NUM,
                EditProfileViewModel.INPUT_FIELD_COL_NUM);
        bioArea.setText(this.editProfileViewModel.getState().getBio());
        bioArea.setEditable(false);
        bioArea.setMaximumSize(new Dimension(EditProfileViewModel.PFP_WIDTH, EditProfileViewModel.PFP_HEIGHT));
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        return bioArea;
    }

    private JButton createBackButton() {
        final JButton button = new JButton(EditProfileViewModel.BACK_BUTTON_LABEL);
        button.setFont(GUIConstants.FONT_TEXT);
        button.setForeground(GUIConstants.PINK);
        return button;
    }

    private void createView() {
        // add title
        final JLabel title;
        title = new GeneralJLabel(EditProfileViewModel.TITLE_LABEL, GUIConstants.TITLE_SIZE, GUIConstants.RED);
        this.add(title);
        // add the main panel
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        // profile preview panel on the left side of main panel
        final JPanel profilePreviewPanel = createProfilePreviewPanel();
        mainPanel.add(profilePreviewPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(GUIConstants.COMPONENT_GAP_SIZE,
                EditProfileViewModel.EDIT_PANEL_HEIGHT)));
        // edit profile panel on right side of main panel
        final JPanel editProfilePanel = createEditProfilePanel();

        final JLabel editBioLabel = new JLabel(EditProfileViewModel.EDIT_BIO_LABEL);
        final JPanel editBioPanel = new LabelTextPanel(editBioLabel, bioInputField);
        editProfilePanel.add(editBioPanel);

        final JLabel pfpLabel = new JLabel(EditProfileViewModel.UPLOAD_PFP_LABEL);
        final JPanel uploadProfilePicturePanel = new LabelTextPanel(pfpLabel, uploadProfilePicture);
        editProfilePanel.add(uploadProfilePicturePanel);

        final JLabel editPreferencesLabel = new JLabel(EditProfileViewModel.PREFERENCE_TAGS_LABEL);
        final StringBuilder preferences = new StringBuilder();
        for (String pref : this.editProfileViewModel.getState().getPreferences()) {
            preferences.append(pref).append(LINE_BREAK);
        }
        preferencesField.setText(preferences.toString());
        final JPanel editPrefsPanel = new LabelTextPanel(editPreferencesLabel, preferencesField);
        editProfilePanel.add(editPrefsPanel);

        final JLabel editLocationLabel = new JLabel(EditProfileViewModel.EDIT_LOCATION_LABEL);
        final JPanel editLocationPanel = new LabelTextPanel(editLocationLabel, locationInputField);
        editProfilePanel.add(editLocationPanel);

        final JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.setLayout(new BoxLayout(saveButtonPanel, BoxLayout.X_AXIS));
        saveButtonPanel.add(Box.createHorizontalGlue());
        saveButtonPanel.add(saveChangesButton);
        editProfilePanel.add(saveButtonPanel);

        mainPanel.add(editProfilePanel);
        this.add(mainPanel);
    }

    private JPanel createProfilePreviewPanel() {
        final JPanel profilePreviewPanel = new JPanel();
        profilePreviewPanel.setLayout(new BoxLayout(profilePreviewPanel, BoxLayout.Y_AXIS));
        profilePreviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePreviewPanel.setBackground(GUIConstants.RED);
        profilePreviewPanel.add(profilePicture);
        profilePreviewPanel.add(Box.createRigidArea(new Dimension(EditProfileViewModel.PFP_WIDTH,
                GUIConstants.COMPONENT_GAP_SIZE)));
        profilePreviewPanel.add(displayName);
        profilePreviewPanel.add(username);
        profilePreviewPanel.add(bio);
        profilePreviewPanel.add(Box.createVerticalGlue());
        profilePreviewPanel.add(backButton);
        return profilePreviewPanel;
    }

    private JPanel createEditProfilePanel() {
        final JPanel editProfilePanel = new JPanel();
        final Dimension panelSize = new Dimension(EditProfileViewModel.EDIT_PANEL_WIDTH,
                EditProfileViewModel.EDIT_PANEL_HEIGHT);
        editProfilePanel.setMaximumSize(panelSize);
        editProfilePanel.setMinimumSize(panelSize);
        editProfilePanel.setLayout(new BoxLayout(editProfilePanel, BoxLayout.Y_AXIS));
        final JLabel editNameLabel = new JLabel(EditProfileViewModel.EDIT_NAME_LABEL);
        final JPanel editNamePanel = new LabelTextPanel(editNameLabel, nameInputField);
        editProfilePanel.add(editNamePanel);
        return editProfilePanel;
    }

    private void addNameInputFieldListener() {
        nameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final EditProfileState currentState = editProfileViewModel.getState();
                currentState.setNewDisplayName(nameInputField.getText());
                editProfileViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    private void addBioInputFieldListener() {
        bioInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final EditProfileState currentState = editProfileViewModel.getState();
                currentState.setNewBio(bioInputField.getText());
                editProfileViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    private void addUploadProfilePictureFieldListener() {
        uploadProfilePicture.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final EditProfileState currentState = editProfileViewModel.getState();
                currentState.setNewProfilePictureUrl(uploadProfilePicture.getText());
                editProfileViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    private void addPreferencesFieldListener() {
        preferencesField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final EditProfileState currentState = editProfileViewModel.getState();

                final ArrayList<String> list = new ArrayList<>(Arrays.asList(preferencesField.getText().split("\n")));
                currentState.setNewPreferences(list);
                editProfileViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    private void addLocationInputFieldListener() {
        locationInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final EditProfileState currentState = editProfileViewModel.getState();
                currentState.setNewLocation(locationInputField.getText());
                editProfileViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    private void addSaveChangesButtonListener() {
        saveChangesButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(saveChangesButton)) {
                        final EditProfileState currentState = editProfileViewModel.getState();
                        this.editProfileController.execute(currentState.getUsername(), currentState.getNewDisplayName(),
                                currentState.getNewBio(), currentState.getNewProfilePictureUrl(),
                                currentState.getNewPreferences(), currentState.getNewLocation()
                        );
                    }
                }
        );
    }

    private void addBackButtonListener() {
        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                        final EditProfileState currentState = editProfileViewModel.getState();
                        this.profileController.executeViewProfile(currentState.getUsername(),
                                currentState.getUsername());
                        this.editProfileController.switchToProfileView();
                    }
                }
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            this.profilePicture.updateIcon(this.editProfileViewModel.getState().getNewProfilePictureUrl());
            this.username.setText(this.editProfileViewModel.getState().getUsername());
            this.displayName.setText(this.editProfileViewModel.getState().getDisplayName());
            this.bio.setText(this.editProfileViewModel.getState().getBio());
            this.nameInputField.setText(this.editProfileViewModel.getState().getNewDisplayName());
            this.bioInputField.setText(this.editProfileViewModel.getState().getNewBio());
            this.uploadProfilePicture.setText(this.editProfileViewModel.getState().getProfilePictureUrl());
            this.location.setText(this.editProfileViewModel.getState().getLocation());
            this.locationInputField.setText(this.editProfileViewModel.getState().getNewLocation());
            final StringBuilder preferences = new StringBuilder();
            for (String pref : this.editProfileViewModel.getState().getPreferences()) {
                preferences.append(pref).append(LINE_BREAK);
            }
            preferencesField.setText(preferences.toString());
        }
        if (evt.getPropertyName().equals("displayName")) {
            this.displayName.setText(this.editProfileViewModel.getState().getDisplayName());
            this.nameInputField.setText(this.editProfileViewModel.getState().getNewDisplayName());
        }
        if (evt.getPropertyName().equals("bio")) {
            this.bio.setText(this.editProfileViewModel.getState().getBio());
            this.bioInputField.setText(this.editProfileViewModel.getState().getNewBio());
        }
        if (evt.getPropertyName().equals("preferences")) {
            final StringBuilder preferences = new StringBuilder();
            for (String pref : this.editProfileViewModel.getState().getPreferences()) {
                preferences.append(pref).append(LINE_BREAK);
            }
            preferencesField.setText(preferences.toString());
        }
        if (evt.getPropertyName().equals("profilePicture")) {
            this.profilePicture.updateIcon(this.editProfileViewModel.getState().getNewProfilePictureUrl());
            this.uploadProfilePicture.setText(this.editProfileViewModel.getState().getProfilePictureUrl());
        }
        if (evt.getPropertyName().equals("location")) {
            this.location.setText(this.editProfileViewModel.getState().getLocation());
            this.locationInputField.setText(this.editProfileViewModel.getState().getNewLocation());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setEditProfileController(EditProfileController controller) {
        this.editProfileController = controller;
    }

    public void setProfileController(ProfileController controller) {
        this.profileController = controller;
    }
}
