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
import interface_adapter.profile.ProfileController;
import view.ui_components.GeneralJLabel;
import view.ui_components.LabelTextPanel;
import view.ui_components.ProfilePictureLabel;

public class EditProfileView extends JPanel implements PropertyChangeListener {
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
    private final JButton saveChangesButton;
    private final JButton backButton;

    public EditProfileView(EditProfileViewModel editProfileViewModel) {
        this.editProfileViewModel = editProfileViewModel;
        this.editProfileViewModel.addPropertyChangeListener(this);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title;
        title = new GeneralJLabel(EditProfileViewModel.TITLE_LABEL, GUIConstants.TITLE_SIZE, GUIConstants.RED);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        final JPanel profilePreviewPanel = new JPanel();
        profilePreviewPanel.setLayout(new BoxLayout(profilePreviewPanel, BoxLayout.Y_AXIS));
        profilePreviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePreviewPanel.setBackground(GUIConstants.RED);

        profilePicture = new ProfilePictureLabel(this.editProfileViewModel.getState().getProfilePictureUrl(),
                EditProfileViewModel.PFP_WIDTH, EditProfileViewModel.PFP_HEIGHT);
        profilePreviewPanel.add(profilePicture);

        profilePreviewPanel.add(Box.createRigidArea(new Dimension(EditProfileViewModel.PFP_WIDTH,
                GUIConstants.COMPONENT_GAP_SIZE)));

        displayName = new GeneralJLabel(this.editProfileViewModel.getState().getDisplayName(), GUIConstants.TEXT_SIZE,
                GUIConstants.WHITE);
        profilePreviewPanel.add(displayName);

        username = new GeneralJLabel(this.editProfileViewModel.getState().getUsername(), GUIConstants.SMALL_TEXT_SIZE,
                GUIConstants.WHITE);
        profilePreviewPanel.add(username);

        bio = new JTextArea(EditProfileViewModel.BIO_ROW_NUM,EditProfileViewModel.INPUT_FIELD_COL_NUM);
        bio.setText(this.editProfileViewModel.getState().getBio());
        bio.setEditable(false);
        bio.setMaximumSize(new Dimension(EditProfileViewModel.PFP_WIDTH, EditProfileViewModel.PFP_HEIGHT));
        bio.setLineWrap(true);
        bio.setWrapStyleWord(true);
        profilePreviewPanel.add(bio);

        profilePreviewPanel.add(Box.createVerticalGlue());

        backButton = new JButton(EditProfileViewModel.BACK_BUTTON_LABEL);
        backButton.setFont(GUIConstants.FONT_TEXT);
        backButton.setForeground(GUIConstants.PINK);
        profilePreviewPanel.add(backButton);

        mainPanel.add(profilePreviewPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(GUIConstants.COMPONENT_GAP_SIZE,
                EditProfileViewModel.EDIT_PANEL_HEIGHT)));

        final JPanel editProfilePanel = new JPanel();
        final Dimension panelSize = new Dimension(EditProfileViewModel.EDIT_PANEL_WIDTH,
                EditProfileViewModel.EDIT_PANEL_HEIGHT);
        editProfilePanel.setMaximumSize(panelSize);
        editProfilePanel.setMinimumSize(panelSize);
        editProfilePanel.setLayout(new BoxLayout(editProfilePanel, BoxLayout.Y_AXIS));

        final JLabel editNameLabel = new JLabel(EditProfileViewModel.EDIT_NAME_LABEL);
        nameInputField = new JTextField();
        nameInputField.setColumns(EditProfileViewModel.INPUT_FIELD_COL_NUM);
        nameInputField.setText(this.editProfileViewModel.getState().getNewDisplayName());
        final JPanel editNamePanel = new LabelTextPanel(editNameLabel, nameInputField);
        editProfilePanel.add(editNamePanel);

        final JLabel editBioLabel = new JLabel(EditProfileViewModel.EDIT_BIO_LABEL);
        bioInputField = new JTextArea(EditProfileViewModel.BIO_ROW_NUM,EditProfileViewModel.INPUT_FIELD_COL_NUM);
        bioInputField.setText(this.editProfileViewModel.getState().getNewBio());
        bioInputField.setLineWrap(true);
        bioInputField.setWrapStyleWord(true);
        final JPanel editBioPanel = new LabelTextPanel(editBioLabel, bioInputField);
        editProfilePanel.add(editBioPanel);

        final JLabel pfpLabel = new JLabel(EditProfileViewModel.UPLOAD_PFP_LABEL);
        uploadProfilePicture = new JTextField(this.editProfileViewModel.getState().getNewProfilePictureUrl());
        uploadProfilePicture.setColumns(EditProfileViewModel.INPUT_FIELD_COL_NUM);
        uploadProfilePicture.setText(this.editProfileViewModel.getState().getNewProfilePictureUrl());
        final JPanel uploadProfilePicturePanel = new LabelTextPanel(pfpLabel, uploadProfilePicture);
        editProfilePanel.add(uploadProfilePicturePanel);

        final JLabel editPreferencesLabel = new JLabel(EditProfileViewModel.PREFERENCE_TAGS_LABEL);
        preferencesField = new JTextArea(EditProfileViewModel.PREF_ROW_NUM, EditProfileViewModel.INPUT_FIELD_COL_NUM);
        final StringBuilder preferences = new StringBuilder();
        for (String pref : this.editProfileViewModel.getState().getPreferences()){
            preferences.append(pref+"\n");
        }
        preferencesField.setText(preferences.toString());
        final JPanel editPrefsPanel = new LabelTextPanel(editPreferencesLabel, preferencesField);
        editProfilePanel.add(editPrefsPanel);

        JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.setLayout(new BoxLayout(saveButtonPanel, BoxLayout.X_AXIS));
        saveButtonPanel.add(Box.createHorizontalGlue());
        saveChangesButton = new JButton(EditProfileViewModel.SAVE_CHANGES_BUTTON_LABEL);
        saveChangesButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        saveChangesButton.setFont(GUIConstants.FONT_TEXT);
        saveChangesButton.setForeground(GUIConstants.PINK);
        saveButtonPanel.add(saveChangesButton);
        editProfilePanel.add(saveButtonPanel);

        mainPanel.add(editProfilePanel);

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

        preferencesField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final EditProfileState currentState = editProfileViewModel.getState();

                ArrayList<String> list = new ArrayList<>(Arrays.asList(preferencesField.getText().split("\n")));
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

        saveChangesButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(saveChangesButton)) {
                        final EditProfileState currentState = editProfileViewModel.getState();

                        this.editProfileController.execute(
                                currentState.getUsername(),
                                currentState.getNewDisplayName(),
                                currentState.getNewBio(),
                                currentState.getNewProfilePictureUrl(),
                                currentState.getNewPreferences()
                        );
                    }
                }
        );

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                        final EditProfileState currentState = editProfileViewModel.getState();
                        this.profileController.executeViewProfile(currentState.getUsername());
                        this.editProfileController.switchToProfileView();
                    }
                }
        );

        this.add(title);
        this.add(mainPanel);
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
            final StringBuilder preferences = new StringBuilder();
            for (String pref : this.editProfileViewModel.getState().getPreferences()){
                preferences.append(pref+"\n");
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
            for (String pref : this.editProfileViewModel.getState().getPreferences()){
                preferences.append(pref+"\n");
            }
            preferencesField.setText(preferences.toString());
        }
        if (evt.getPropertyName().equals("profilePicture")) {
            this.profilePicture.updateIcon(this.editProfileViewModel.getState().getNewProfilePictureUrl());
            this.uploadProfilePicture.setText(this.editProfileViewModel.getState().getProfilePictureUrl());
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
